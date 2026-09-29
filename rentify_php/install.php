<?php
/**
 * Rentify.pk - 1-Click Database Installer
 * Run once on your Hostinger/InfinityFree server by visiting yourdomain.com/install.php
 */
require_once __DIR__ . '/config.php';

$message = '';
$error = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $dbHost = $_POST['db_host'] ?? DB_HOST;
    $dbName = $_POST['db_name'] ?? DB_NAME;
    $dbUser = $_POST['db_user'] ?? DB_USER;
    $dbPass = $_POST['db_pass'] ?? DB_PASS;

    try {
        // Connect to server (without DB initially in case it needs creation)
        $pdo = new PDO("mysql:host=$dbHost;charset=utf8mb4", $dbUser, $dbPass, [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION
        ]);

        $pdo->exec("CREATE DATABASE IF NOT EXISTS `$dbName` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
        $pdo->exec("USE `$dbName`;");

        // 1. Items Table
        $pdo->exec("CREATE TABLE IF NOT EXISTS `items` (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `title` VARCHAR(255) NOT NULL,
            `category` VARCHAR(50) NOT NULL,
            `price_per_day` INT NOT NULL,
            `deposit` INT NOT NULL,
            `city` VARCHAR(100) NOT NULL DEFAULT 'Kashmore',
            `location_details` VARCHAR(255) NOT NULL,
            `owner_name` VARCHAR(100) NOT NULL,
            `owner_avatar` VARCHAR(255) NULL,
            `owner_phone` VARCHAR(50) NOT NULL,
            `owner_rating` DECIMAL(3,1) DEFAULT 4.9,
            `owner_reviews` INT DEFAULT 18,
            `owner_items_count` INT DEFAULT 5,
            `is_owner_verified` TINYINT(1) DEFAULT 1,
            `image_url` TEXT NOT NULL,
            `secondary_images` TEXT NULL,
            `description` TEXT NOT NULL,
            `specs` TEXT NOT NULL,
            `status` ENUM('approved', 'pending', 'rejected') DEFAULT 'approved',
            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        // 2. Requests Table
        $pdo->exec("CREATE TABLE IF NOT EXISTS `requests` (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `item_id` INT NOT NULL,
            `item_title` VARCHAR(255) NOT NULL,
            `item_image` TEXT NOT NULL,
            `owner_name` VARCHAR(100) NOT NULL,
            `owner_phone` VARCHAR(50) NOT NULL,
            `renter_name` VARCHAR(100) NOT NULL,
            `renter_phone` VARCHAR(50) NOT NULL,
            `start_date` VARCHAR(50) NOT NULL,
            `end_date` VARCHAR(50) NOT NULL,
            `days` INT NOT NULL,
            `daily_rate` INT NOT NULL,
            `total_rent` INT NOT NULL,
            `commission` INT NOT NULL,
            `deposit` INT NOT NULL,
            `total_payable` INT NOT NULL,
            `message` TEXT NOT NULL,
            `status` ENUM('pending', 'accepted', 'rejected', 'completed') DEFAULT 'pending',
            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        // 3. Chats Table
        $pdo->exec("CREATE TABLE IF NOT EXISTS `chats` (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `request_id` INT NOT NULL,
            `sender_name` VARCHAR(100) NOT NULL,
            `message` TEXT NOT NULL,
            `is_owner` TINYINT(1) DEFAULT 0,
            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        // 4. Verifications Table
        $pdo->exec("CREATE TABLE IF NOT EXISTS `verifications` (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `user_name` VARCHAR(100) NOT NULL,
            `phone` VARCHAR(50) NOT NULL,
            `city` VARCHAR(100) NOT NULL,
            `cnic_number` VARCHAR(50) NOT NULL,
            `status` ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
            `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        // 5. Admins Table
        $pdo->exec("CREATE TABLE IF NOT EXISTS `admins` (
            `id` INT AUTO_INCREMENT PRIMARY KEY,
            `email` VARCHAR(100) UNIQUE NOT NULL,
            `password` VARCHAR(255) NOT NULL,
            `name` VARCHAR(100) NOT NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");

        // Seed Admin (admin@rentify.pk / admin123)
        $adminStmt = $pdo->prepare("INSERT IGNORE INTO `admins` (`id`, `email`, `password`, `name`) VALUES (1, 'admin@rentify.pk', :pass, 'Admin Sarmad')");
        $adminStmt->execute(['pass' => password_hash('admin123', PASSWORD_DEFAULT)]);

        // Seed 10 Real Pakistan Items
        $checkItems = $pdo->query("SELECT COUNT(*) FROM `items`")->fetchColumn();
        if ($checkItems == 0) {
            $items = [
                [
                    'Sony Alpha A6400 with 16-50mm Lens - Excellent Condition',
                    'Camera', 2000, 5000, 'Kashmore', 'Kashmore City, Near Bus Stand • 1.2km away',
                    'Salman Khan', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150',
                    '0300-8392104', 4.9, 23, 12, 1,
                    'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800',
                    'https://images.unsplash.com/photo-1502920917128-1aa500764cbd?w=800,https://images.unsplash.com/photo-1512790182412-b19e6d62bc39?w=800',
                    'Sony A6400 in excellent condition, bought 6 months ago from Karachi, used only for weddings, includes 1 battery, charger, original bag, 32GB card, bill available. No scratches, shutter count 2500 only.',
                    'Brand: Sony|Model: A6400|Condition: 9.5/10|Sensor: 24.2MP APS-C|4K Video: Yes|Pickup: Kashmore City',
                    'approved'
                ],
                [
                    'Canon EOS 200D DSLR Camera with 18-55mm STM',
                    'Camera', 1500, 4000, 'Kashmore', 'Kashmore Main Bazaar • 0.8km away',
                    'Ahmed Ali', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150',
                    '0302-1144778', 4.8, 14, 4, 1,
                    'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=800',
                    'https://images.unsplash.com/photo-1495745966610-2a67f2297e5e?w=800',
                    'Canon 200D lightweight DSLR with dual pixel CMOS AF. Perfect for vlogging and wedding photography.',
                    'Brand: Canon|Model: EOS 200D|Lens: 18-55mm|Condition: 9/10|Includes: Bag & 64GB Card',
                    'approved'
                ],
                [
                    'Dawlance Inverter AC 1.5 Ton - Quick Cooling',
                    'AC', 1000, 6000, 'Usta Muhammad', 'Usta Muhammad Railway Road • 3.5km away',
                    'Farooq Jamali', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150',
                    '0333-7890123', 4.7, 8, 2, 1,
                    'https://images.unsplash.com/photo-1614633833026-062015a77f98?w=800',
                    '', 'Dawlance DC Inverter 1.5 Ton. Clean indoor and outdoor units with copper pipes. Ideal for summer functions.',
                    'Brand: Dawlance|Capacity: 1.5 Ton|Type: DC Inverter|Gas: R410A Eco',
                    'approved'
                ],
                [
                    'Gree Eco Inverter 1 Ton AC (Ready for Setup)',
                    'AC', 1200, 5000, 'Kashmore', 'Near Kashmore Colony • 1.9km away',
                    'Waqas Brohi', 'https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150',
                    '0312-5558901', 4.6, 5, 1, 0,
                    'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=800',
                    '', 'Gree 1 Ton high efficiency split AC, cold air within 3 minutes. Remote control and pipe fittings included.',
                    'Brand: Gree|Capacity: 1.0 Ton|Power: Inverter|Condition: 8.5/10',
                    'approved'
                ],
                [
                    'Royal Velvet 5-Seater Luxury Sofa Set with Cushions',
                    'Furniture', 800, 3000, 'Jacobabad', 'Jacobabad Civil Hospital Road • 2.1km away',
                    'Sarmad Malik', 'https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150',
                    '0345-9876543', 5.0, 31, 8, 1,
                    'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800',
                    '', 'Velvet upholstered 3+1+1 luxury sofa set with soft cushions. Perfect for wedding receptions & mehndi night.',
                    'Material: Velvet & Wood|Seating: 5 Persons|Color: Deep Royal Navy',
                    'approved'
                ],
                [
                    'Suzuki Mehran 2019 White - Neat & Clean',
                    'Cars', 2500, 10000, 'Kandhkot', 'Kandhkot Bypass • 0.5km away',
                    'Tariq Baloch', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150',
                    '0300-3344556', 4.9, 42, 3, 1,
                    'https://images.unsplash.com/photo-1549399542-7e3f8b79c341?w=800',
                    '', 'Suzuki Mehran 2019 Euro II model, chilled AC, new tyres, clean interior. Petrol driven.',
                    'Model: Suzuki Mehran 2019|Fuel: Petrol|AC: Chilled|Docs: Smart Card Available',
                    'approved'
                ],
                [
                    'Honda EU30is 3KV Heavy Silent Generator',
                    'Generators', 1500, 8000, 'Usta Muhammad', 'Usta Muhammad Main Chowk • 1.1km away',
                    'Liaquat Ali', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=150',
                    '0301-4455667', 4.8, 19, 6, 1,
                    'https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800',
                    '', '3KVA heavy duty silent petrol generator with self-start key. Runs 1.5 ton AC + fans during load shedding.',
                    'Capacity: 3.0 KVA|Fuel: Petrol|Start: Self Key & Recoil|Noise: Silent Canopy',
                    'approved'
                ],
                [
                    'Bosch Professional Impact Drill Machine 750W',
                    'Tools', 300, 1500, 'Kashmore', 'Near Kashmore Power Plant • 2.0km away',
                    'Zahid Hussain', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150',
                    '0305-6677889', 4.9, 12, 7, 1,
                    'https://images.unsplash.com/photo-1504148455328-c376907d081c?w=800',
                    '', 'Bosch GSB professional impact drill 750W with 13mm chuck, forward/reverse, variable speed and bit set.',
                    'Brand: Bosch|Power: 750W|Speed: 0-2800 RPM|Bits: 10 Piece Set Included',
                    'approved'
                ],
                [
                    'Bridal Maroon Velvet Embroidered Designer Lehenga',
                    'Dresses', 5000, 15000, 'Shikarpur', 'Shikarpur Station Road • 1.5km away',
                    'Zainab Bibi', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150',
                    '0342-9988771', 5.0, 11, 4, 1,
                    'https://images.unsplash.com/photo-1518049362265-d5b2a6467637?w=800',
                    '', 'Heavy handcrafted zardozi and dabka work bridal lehenga in royal crimson maroon. Dry-cleaned.',
                    'Type: Bridal Barat Lehenga|Fabric: Micro Velvet|Condition: Worn Once (10/10)',
                    'approved'
                ],
                [
                    'Wedding Warm Fairy Lights & 500W Halogens Set',
                    'Wedding', 1800, 4000, 'Dera Allah Yar', 'Dera Allah Yar Bazaar • 0.9km away',
                    'Noor Stage Decors', 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150',
                    '0331-2233445', 4.8, 27, 9, 1,
                    'https://images.unsplash.com/photo-1519741497674-611481863552?w=800',
                    '', 'Complete festive lighting kit for home wedding decoration: 10 bundles warm fairy lights + four 500W halogens.',
                    'Contents: 10x Fairy String (500ft), 4x 500W Halogen, 50m Heavy Cable',
                    'approved'
                ]
            ];

            $insertStmt = $pdo->prepare("INSERT INTO `items` 
                (`title`, `category`, `price_per_day`, `deposit`, `city`, `location_details`, `owner_name`, `owner_avatar`, `owner_phone`, `owner_rating`, `owner_reviews`, `owner_items_count`, `is_owner_verified`, `image_url`, `secondary_images`, `description`, `specs`, `status`)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            foreach ($items as $item) {
                $insertStmt->execute($item);
            }
        }

        // Seed Salman & Bilal's rental request
        $checkReqs = $pdo->query("SELECT COUNT(*) FROM `requests`")->fetchColumn();
        if ($checkReqs == 0) {
            $pdo->exec("INSERT INTO `requests` 
                (`item_id`, `item_title`, `item_image`, `owner_name`, `owner_phone`, `renter_name`, `renter_phone`, `start_date`, `end_date`, `days`, `daily_rate`, `total_rent`, `commission`, `deposit`, `total_payable`, `message`, `status`)
                VALUES (1, 'Sony Alpha A6400 with 16-50mm Lens - Excellent Condition', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=800', 'Salman Khan', '0300-8392104', 'Bilal Ahmed', '0301-7654321', '15 Nov 2026', '16 Nov 2026', 2, 2000, 4000, 400, 5000, 9400, 'Hi Salman, I need camera for my sister wedding on 15-16 Nov, will take care...', 'accepted');");

            // Seed chats
            $pdo->exec("INSERT INTO `chats` (`request_id`, `sender_name`, `message`, `is_owner`) VALUES
                (1, 'Bilal Ahmed', 'As-salamu alaykum Salman bhai, is the camera available for 15-16 Nov for sister wedding?', 0),
                (1, 'Salman Khan', 'Wa alaykum as-salam Bilal! Yes brother, it is free. 2 batteries and 32GB card included.', 1),
                (1, 'Bilal Ahmed', 'Zabardast! I have sent the booking request for Rs. 4,000 + Rs. 5,000 refundable deposit.', 0),
                (1, 'Salman Khan', 'I accepted! Let us meet at Kashmore city bus stand on 14th evening. I will test buttons in front of you.', 1);");

            // Seed verification
            $pdo->exec("INSERT INTO `verifications` (`user_name`, `phone`, `city`, `cnic_number`, `status`) VALUES
                ('Salman Khan', '0300-8392104', 'Kashmore', '43102-1234567-1', 'approved');");
        }

        $message = "Database installed successfully! Admin credentials: admin@rentify.pk / admin123";
    } catch (Exception $e) {
        $error = "Installation Error: " . $e->getMessage();
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Install Rentify.pk Database</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/app.css">
</head>
<body style="background: #FAFAFA; display:flex; align-items:center; justify-content:center; min-height: 100vh; padding: 20px;">
    <div style="background: #FFFFFF; border: 1px solid #F0F0F0; border-radius: 20px; max-width: 440px; width: 100%; padding: 32px; box-shadow: 0 4px 20px rgba(0,0,0,0.06);">
        <div style="text-align: center; margin-bottom: 24px;">
            <h1 style="font-size: 28px; font-weight: 700; letter-spacing: -0.5px; margin: 0; color: #0F0F0F;">rentify<span style="color:#635BFF;">.</span></h1>
            <p style="font-size: 13px; color: #71717A; margin-top: 6px;">1-Click Database Setup for Hostinger / InfinityFree</p>
        </div>

        <?php if ($message): ?>
            <div style="background: #DCFCE7; color: #166534; padding: 14px; border-radius: 12px; font-size: 13px; margin-bottom: 20px; font-weight: 500;">
                ✓ <?= $message ?><br><br>
                <a href="index.php" class="btn btn-primary" style="display:block; text-align:center; text-decoration:none; margin-top: 8px;">Open Rentify Marketplace →</a>
            </div>
        <?php endif; ?>

        <?php if ($error): ?>
            <div style="background: #FEE2E2; color: #991B1B; padding: 14px; border-radius: 12px; font-size: 13px; margin-bottom: 20px;">
                ⚠ <?= $error ?>
            </div>
        <?php endif; ?>

        <form method="POST">
            <div style="margin-bottom: 14px;">
                <label style="display:block; font-size: 13px; font-weight: 500; margin-bottom: 6px; color: #0F0F0F;">MySQL Host</label>
                <input type="text" name="db_host" value="<?= htmlspecialchars(DB_HOST) ?>" class="input-pill" required>
            </div>
            <div style="margin-bottom: 14px;">
                <label style="display:block; font-size: 13px; font-weight: 500; margin-bottom: 6px; color: #0F0F0F;">Database Name</label>
                <input type="text" name="db_name" value="<?= htmlspecialchars(DB_NAME) ?>" class="input-pill" required>
            </div>
            <div style="margin-bottom: 14px;">
                <label style="display:block; font-size: 13px; font-weight: 500; margin-bottom: 6px; color: #0F0F0F;">Database User</label>
                <input type="text" name="db_user" value="<?= htmlspecialchars(DB_USER) ?>" class="input-pill" required>
            </div>
            <div style="margin-bottom: 24px;">
                <label style="display:block; font-size: 13px; font-weight: 500; margin-bottom: 6px; color: #0F0F0F;">Database Password</label>
                <input type="password" name="db_pass" value="<?= htmlspecialchars(DB_PASS) ?>" class="input-pill">
            </div>

            <button type="submit" class="btn btn-primary btn-block">Install Database & Seed Kashmore Data</button>
        </form>
    </div>
</body>
</html>
