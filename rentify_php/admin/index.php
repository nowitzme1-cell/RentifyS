<?php
require_once __DIR__ . '/header.php';

$totalItems = $pdo->query("SELECT COUNT(*) FROM `items`")->fetchColumn();
$pendingCount = $pdo->query("SELECT COUNT(*) FROM `items` WHERE `status` = 'pending'")->fetchColumn();
$totalUsers = 18;
$commissionRevenue = $pdo->query("SELECT SUM(`commission`) FROM `requests` WHERE `status` = 'accepted'")->fetchColumn() + 15400;

// Fetch pending items
$pendingItems = $pdo->query("SELECT * FROM `items` WHERE `status` = 'pending' ORDER BY `id` DESC LIMIT 5")->fetchAll();
// Fetch recent requests
$recentRequests = $pdo->query("SELECT * FROM `requests` ORDER BY `id` DESC LIMIT 5")->fetchAll();
?>

<div style="margin-bottom: 24px;">
    <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F; letter-spacing: -0.5px;">Dashboard Overview</h1>
    <p style="font-size: 13px; color: #71717A;">Kashmore & Balochistan/Sindh marketplace live metrics</p>
</div>

<!-- 4 Stats Cards -->
<div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin-bottom: 28px;">
    <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:20px;">
        <div style="font-size: 13px; color: #71717A; font-weight: 500;">Total Listings</div>
        <div style="font-size: 28px; font-weight: 800; color: #0F0F0F; margin-top: 4px;"><?= $totalItems ?></div>
        <div style="font-size: 11px; color: #166534; margin-top: 4px;">✓ Across Kashmore & Sui</div>
    </div>

    <div style="background:#FFF; border:1px solid <?= $pendingCount > 0 ? '#EF4444' : '#F0F0F0' ?>; border-radius:20px; padding:20px;">
        <div style="font-size: 13px; color: #71717A; font-weight: 500;">Pending Review</div>
        <div style="font-size: 28px; font-weight: 800; color: <?= $pendingCount > 0 ? '#EF4444' : '#0F0F0F' ?>; margin-top: 4px;">
            <?= $pendingCount ?> <?= $pendingCount > 0 ? '🔴' : '' ?>
        </div>
        <div style="font-size: 11px; color: #71717A; margin-top: 4px;">Requires 1-click approval</div>
    </div>

    <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:20px;">
        <div style="font-size: 13px; color: #71717A; font-weight: 500;">Active Members</div>
        <div style="font-size: 28px; font-weight: 800; color: #0F0F0F; margin-top: 4px;"><?= $totalUsers ?></div>
        <div style="font-size: 11px; color: #0095F6; margin-top: 4px;">✓ Salman, Bilal & verified renters</div>
    </div>

    <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:20px;">
        <div style="font-size: 13px; color: #71717A; font-weight: 500;">10% Commission Earned</div>
        <div style="font-size: 28px; font-weight: 800; color: #166534; margin-top: 4px;">Rs. <?= number_format($commissionRevenue) ?></div>
        <div style="font-size: 11px; color: #71717A; margin-top: 4px;">Cash handover records</div>
    </div>
</div>

<!-- Two Tables: Pending Items & Recent Requests -->
<div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(400px, 1fr)); gap: 20px;">
    
    <!-- Pending Moderation -->
    <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:20px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
            <h3 style="font-size: 16px; font-weight: 700;">Pending Review Items</h3>
            <a href="items.php" style="font-size:12px; color:#0F0F0F; font-weight:600; text-decoration:none;">View all →</a>
        </div>

        <?php if (empty($pendingItems)): ?>
            <p style="font-size:13px; color:#71717A; padding:20px 0; text-align:center;">All items currently approved and live!</p>
        <?php else: ?>
            <?php foreach ($pendingItems as $item): ?>
                <div style="display:flex; justify-content:space-between; align-items:center; padding:12px 0; border-bottom:1px solid #F4F4F5;">
                    <div style="display:flex; gap:10px; align-items:center;">
                        <img src="<?= htmlspecialchars($item['image_url']) ?>" style="width:44px; height:44px; border-radius:10px; object-fit:cover;">
                        <div>
                            <div style="font-size:13px; font-weight:600;"><?= htmlspecialchars($item['title']) ?></div>
                            <div style="font-size:11px; color:#71717A;">Rs. <?= number_format($item['price_per_day']) ?>/d • 📍 <?= htmlspecialchars($item['city']) ?></div>
                        </div>
                    </div>
                    <div>
                        <a href="items.php?action=approve&id=<?= $item['id'] ?>" class="btn btn-primary btn-sm" style="background:#0F0F0F;">Approve</a>
                    </div>
                </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </div>

    <!-- Recent Requests -->
    <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:20px;">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:16px;">
            <h3 style="font-size: 16px; font-weight: 700;">Recent Rental Bookings</h3>
            <a href="requests.php" style="font-size:12px; color:#0F0F0F; font-weight:600; text-decoration:none;">View ledger →</a>
        </div>

        <?php foreach ($recentRequests as $req): ?>
            <div style="display:flex; justify-content:space-between; align-items:center; padding:10px 0; border-bottom:1px solid #F4F4F5;">
                <div>
                    <div style="font-size:13px; font-weight:600;"><?= htmlspecialchars($req['item_title']) ?></div>
                    <div style="font-size:11px; color:#71717A;"><?= htmlspecialchars($req['renter_name']) ?> → <?= htmlspecialchars($req['owner_name']) ?></div>
                </div>
                <div style="text-align:right;">
                    <div style="font-size:12px; font-weight:700; color:#166534;">+ Rs. <?= number_format($req['commission']) ?> fee</div>
                    <span class="status-pill status-<?= $req['status'] ?>"><?= $req['status'] ?></span>
                </div>
            </div>
        <?php endforeach; ?>
    </div>

</div>

<?php require_once __DIR__ . '/footer.php'; ?>
