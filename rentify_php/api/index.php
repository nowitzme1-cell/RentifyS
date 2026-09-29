<?php
require_once __DIR__ . '/../includes/db.php';

header('Content-Type: application/json; charset=utf-8');

$action = $_GET['action'] ?? '';
$currentUser = getCurrentUser();

try {
    $pdo = DB::connect();

    switch ($action) {
        // 1. Get filtered items
        case 'items':
            $city = $_GET['city'] ?? 'All Cities';
            $category = $_GET['category'] ?? 'All';
            $search = $_GET['search'] ?? '';

            $sql = "SELECT * FROM `items` WHERE `status` = 'approved'";
            $params = [];

            if ($city !== 'All Cities' && !empty($city)) {
                $sql .= " AND `city` = ?";
                $params[] = $city;
            }

            if ($category !== 'All' && !empty($category)) {
                $sql .= " AND `category` = ?";
                $params[] = $category;
            }

            if (!empty($search)) {
                $sql .= " AND (`title` LIKE ? OR `description` LIKE ? OR `location_details` LIKE ?)";
                $wildcard = "%$search%";
                $params[] = $wildcard;
                $params[] = $wildcard;
                $params[] = $wildcard;
            }

            $sql .= " ORDER BY `id` ASC";
            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);
            $items = $stmt->fetchAll();

            jsonResponse(['status' => 'success', 'data' => $items]);
            break;

        // 2. Get single item detail
        case 'item':
            $id = intval($_GET['id'] ?? 0);
            $stmt = $pdo->prepare("SELECT * FROM `items` WHERE `id` = ? LIMIT 1");
            $stmt->execute([$id]);
            $item = $stmt->fetch();

            if (!$item) {
                jsonResponse(['status' => 'error', 'message' => 'Item not found'], 404);
            }
            jsonResponse(['status' => 'success', 'data' => $item]);
            break;

        // 3. Send rent request (with 10% commission calculation)
        case 'send_request':
            $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
            $itemId = intval($data['item_id'] ?? 0);
            $days = max(1, intval($data['days'] ?? 2));
            $startDate = cleanInput($data['start_date'] ?? '15 Nov 2026');
            $endDate = cleanInput($data['end_date'] ?? '16 Nov 2026');
            $message = cleanInput($data['message'] ?? '');

            $stmt = $pdo->prepare("SELECT * FROM `items` WHERE `id` = ? LIMIT 1");
            $stmt->execute([$itemId]);
            $item = $stmt->fetch();

            if (!$item) {
                jsonResponse(['status' => 'error', 'message' => 'Item not found'], 404);
            }

            $totalRent = $item['price_per_day'] * $days;
            $commission = round(($totalRent * COMMISSION_RATE) / 100);
            $deposit = $item['deposit'];
            $totalPayable = $totalRent + $commission + $deposit;

            $insertStmt = $pdo->prepare("INSERT INTO `requests`
                (`item_id`, `item_title`, `item_image`, `owner_name`, `owner_phone`, `renter_name`, `renter_phone`, `start_date`, `end_date`, `days`, `daily_rate`, `total_rent`, `commission`, `deposit`, `total_payable`, `message`, `status`)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'pending')");

            $insertStmt->execute([
                $item['id'],
                $item['title'],
                $item['image_url'],
                $item['owner_name'],
                $item['owner_phone'],
                $currentUser['name'],
                $currentUser['phone'],
                $startDate,
                $endDate,
                $days,
                $item['price_per_day'],
                $totalRent,
                $commission,
                $deposit,
                $totalPayable,
                $message
            ]);

            $requestId = $pdo->lastInsertId();

            // Initial Chat Message
            $chatStmt = $pdo->prepare("INSERT INTO `chats` (`request_id`, `sender_name`, `message`, `is_owner`) VALUES (?, ?, ?, 0)");
            $chatStmt->execute([
                $requestId,
                $currentUser['name'],
                $message ?: "Hello {$item['owner_name']}, I sent a rental request for {$item['title']} for $days days."
            ]);

            jsonResponse([
                'status' => 'success',
                'message' => 'Rental request sent successfully!',
                'request_id' => $requestId
            ]);
            break;

        // 4. Get requests
        case 'requests':
            $stmt = $pdo->query("SELECT * FROM `requests` ORDER BY `id` DESC");
            $requests = $stmt->fetchAll();
            jsonResponse(['status' => 'success', 'data' => $requests]);
            break;

        // 5. Update request status (accept/reject)
        case 'update_request_status':
            $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
            $requestId = intval($data['request_id'] ?? 0);
            $newStatus = cleanInput($data['status'] ?? 'accepted');

            $stmt = $pdo->prepare("UPDATE `requests` SET `status` = ? WHERE `id` = ?");
            $stmt->execute([$newStatus, $requestId]);

            // Add notification message in chat
            $chatMsg = $newStatus === 'accepted'
                ? "Request Accepted! Contact me at {$currentUser['phone']} to coordinate meetup in {$currentUser['city']}."
                : "Request was declined.";
            
            $chatStmt = $pdo->prepare("INSERT INTO `chats` (`request_id`, `sender_name`, `message`, `is_owner`) VALUES (?, ?, ?, 1)");
            $chatStmt->execute([$requestId, $currentUser['name'], $chatMsg]);

            jsonResponse(['status' => 'success', 'message' => "Request #$requestId updated to $newStatus"]);
            break;

        // 6. Get Chat Messages
        case 'chats':
            $requestId = intval($_GET['request_id'] ?? 0);
            $stmt = $pdo->prepare("SELECT * FROM `chats` WHERE `request_id` = ? ORDER BY `id` ASC");
            $stmt->execute([$requestId]);
            $chats = $stmt->fetchAll();
            jsonResponse(['status' => 'success', 'data' => $chats]);
            break;

        // 7. Send Chat Message
        case 'send_chat':
            $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
            $requestId = intval($data['request_id'] ?? 0);
            $msg = cleanInput($data['message'] ?? '');

            if (!empty($msg)) {
                $stmt = $pdo->prepare("INSERT INTO `chats` (`request_id`, `sender_name`, `message`, `is_owner`) VALUES (?, ?, ?, ?)");
                $isOwner = ($currentUser['role'] === 'Owner') ? 1 : 0;
                $stmt->execute([$requestId, $currentUser['name'], $msg, $isOwner]);
            }
            jsonResponse(['status' => 'success']);
            break;

        // 8. Post new item
        case 'post_item':
            $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
            $title = cleanInput($data['title'] ?? '');
            $category = cleanInput($data['category'] ?? 'Camera');
            $pricePerDay = intval($data['price_per_day'] ?? 2000);
            $deposit = intval($data['deposit'] ?? 5000);
            $city = cleanInput($data['city'] ?? $currentUser['city']);
            $locationDetails = cleanInput($data['location_details'] ?? "$city Main City");
            $description = cleanInput($data['description'] ?? '');
            $specs = cleanInput($data['specs'] ?? 'Condition: 9/10');
            $imageUrl = cleanInput($data['image_url'] ?? 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800');

            $stmt = $pdo->prepare("INSERT INTO `items`
                (`title`, `category`, `price_per_day`, `deposit`, `city`, `location_details`, `owner_name`, `owner_avatar`, `owner_phone`, `owner_rating`, `owner_reviews`, `owner_items_count`, `is_owner_verified`, `image_url`, `description`, `specs`, `status`)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 5.0, 1, 3, 1, ?, ?, ?, 'approved')");

            $stmt->execute([
                $title,
                $category,
                $pricePerDay,
                $deposit,
                $city,
                $locationDetails,
                $currentUser['name'],
                'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150',
                $currentUser['phone'],
                $imageUrl,
                $description,
                $specs
            ]);

            jsonResponse(['status' => 'success', 'message' => 'Item posted and published live!']);
            break;

        // 9. Switch user demo persona
        case 'switch_user':
            $persona = $_GET['user'] ?? 'bilal';
            if ($persona === 'salman') {
                $_SESSION['user'] = [
                    'id' => 2,
                    'name' => 'Salman Khan',
                    'phone' => '0300-8392104',
                    'city' => 'Kashmore',
                    'role' => 'Owner',
                    'is_verified' => 1,
                    'items_count' => 12,
                    'rating' => 4.9
                ];
            } else {
                $_SESSION['user'] = [
                    'id' => 1,
                    'name' => 'Bilal Ahmed',
                    'phone' => '0301-7654321',
                    'city' => 'Kashmore',
                    'role' => 'Renter',
                    'is_verified' => 1,
                    'items_count' => 2,
                    'rating' => 4.9
                ];
            }
            jsonResponse(['status' => 'success', 'user' => $_SESSION['user']]);
            break;

        // 10. Submit CNIC verification
        case 'verify_cnic':
            $data = json_decode(file_get_contents('php://input'), true) ?? $_POST;
            $cnic = cleanInput($data['cnic_number'] ?? '43102-1234567-1');
            $stmt = $pdo->prepare("INSERT INTO `verifications` (`user_name`, `phone`, `city`, `cnic_number`, `status`) VALUES (?, ?, ?, ?, 'pending')");
            $stmt->execute([$currentUser['name'], $currentUser['phone'], $currentUser['city'], $cnic]);
            jsonResponse(['status' => 'success', 'message' => 'CNIC verification submitted!']);
            break;

        default:
            jsonResponse(['status' => 'error', 'message' => 'Invalid action'], 400);
    }
} catch (Exception $e) {
    jsonResponse(['status' => 'error', 'message' => $e->getMessage()], 500);
}
