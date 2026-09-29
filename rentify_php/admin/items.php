<?php
require_once __DIR__ . '/header.php';

// Handle 1-click Approve / Reject actions
if (isset($_GET['action']) && isset($_GET['id'])) {
    $itemId = intval($_GET['id']);
    $action = $_GET['action'];

    if ($action === 'approve') {
        $stmt = $pdo->prepare("UPDATE `items` SET `status` = 'approved' WHERE `id` = ?");
        $stmt->execute([$itemId]);
        $flash = "Item #$itemId approved and published live!";
    } elseif ($action === 'reject') {
        $stmt = $pdo->prepare("UPDATE `items` SET `status` = 'rejected' WHERE `id` = ?");
        $stmt->execute([$itemId]);
        $flash = "Item #$itemId rejected.";
    }
}

$filter = $_GET['filter'] ?? 'All';
$sql = "SELECT * FROM `items`";
if ($filter !== 'All') {
    $sql .= " WHERE `status` = " . $pdo->quote(strtolower($filter));
}
$sql .= " ORDER BY `id` DESC";
$items = $pdo->query($sql)->fetchAll();
?>

<div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:20px;">
    <div>
        <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F;">Items Moderation</h1>
        <p style="font-size: 13px; color: #71717A;">Review, approve, or reject user listings in Kashmore</p>
    </div>
</div>

<?php if (!empty($flash)): ?>
    <div style="background:#DCFCE7; color:#166534; padding:12px 16px; border-radius:12px; font-size:13px; font-weight:600; margin-bottom:16px;">
        ✓ <?= htmlspecialchars($flash) ?>
    </div>
<?php endif; ?>

<!-- Filter Tabs -->
<div style="display:flex; gap:8px; margin-bottom:20px;">
    <?php foreach (['All', 'Pending', 'Approved', 'Rejected'] as $f): ?>
        <a href="items.php?filter=<?= $f ?>" class="btn btn-secondary btn-sm" style="<?= $filter === $f ? 'background:#0F0F0F; color:#FFF;' : '' ?>">
            <?= $f ?>
        </a>
    <?php endforeach; ?>
</div>

<!-- Items Table Card -->
<div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; overflow:hidden;">
    <table style="width:100%; border-collapse:collapse; text-align:left; font-size:13px;">
        <thead>
            <tr style="background:#F4F4F5; color:#71717A; font-weight:600;">
                <th style="padding:14px 16px;">Item</th>
                <th style="padding:14px 16px;">Category</th>
                <th style="padding:14px 16px;">Price/Day</th>
                <th style="padding:14px 16px;">Deposit</th>
                <th style="padding:14px 16px;">Owner</th>
                <th style="padding:14px 16px;">City</th>
                <th style="padding:14px 16px;">Status</th>
                <th style="padding:14px 16px; text-align:right;">Actions</th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($items as $it): ?>
                <tr style="border-bottom:1px solid #F4F4F5;">
                    <td style="padding:14px 16px;">
                        <div style="display:flex; align-items:center; gap:10px;">
                            <img src="<?= htmlspecialchars($it['image_url']) ?>" style="width:40px; height:40px; border-radius:8px; object-fit:cover;">
                            <span style="font-weight:600; color:#0F0F0F;"><?= htmlspecialchars($it['title']) ?></span>
                        </div>
                    </td>
                    <td style="padding:14px 16px;"><?= htmlspecialchars($it['category']) ?></td>
                    <td style="padding:14px 16px; font-weight:700;">Rs. <?= number_format($it['price_per_day']) ?></td>
                    <td style="padding:14px 16px; color:#71717A;">Rs. <?= number_format($it['deposit']) ?></td>
                    <td style="padding:14px 16px;"><?= htmlspecialchars($it['owner_name']) ?></td>
                    <td style="padding:14px 16px;">📍 <?= htmlspecialchars($it['city']) ?></td>
                    <td style="padding:14px 16px;">
                        <span class="status-pill status-<?= $it['status'] ?>"><?= $it['status'] ?></span>
                    </td>
                    <td style="padding:14px 16px; text-align:right;">
                        <?php if ($it['status'] !== 'approved'): ?>
                            <a href="items.php?action=approve&id=<?= $it['id'] ?>&filter=<?= $filter ?>" class="btn btn-primary btn-sm" style="background:#0F0F0F; padding:6px 12px;">Approve</a>
                        <?php endif; ?>
                        <?php if ($it['status'] !== 'rejected'): ?>
                            <a href="items.php?action=reject&id=<?= $it['id'] ?>&filter=<?= $filter ?>" class="btn btn-secondary btn-sm" style="padding:6px 12px; color:#DC2626;">Reject</a>
                        <?php endif; ?>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
