<?php
require_once __DIR__ . '/header.php';

if (isset($_GET['action']) && isset($_GET['id'])) {
    $verId = intval($_GET['id']);
    if ($_GET['action'] === 'approve') {
        $stmt = $pdo->prepare("UPDATE `verifications` SET `status` = 'approved' WHERE `id` = ?");
        $stmt->execute([$verId]);
        $flash = "User CNIC verified! Blue tick badge awarded.";
    }
}

$verifications = $pdo->query("SELECT * FROM `verifications` ORDER BY `id` DESC")->fetchAll();
?>

<div style="margin-bottom:20px;">
    <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F;">CNIC Blue Tick Verifications</h1>
    <p style="font-size: 13px; color: #71717A;">Review citizen NADRA credentials before granting blue tick status</p>
</div>

<?php if (!empty($flash)): ?>
    <div style="background:#DCFCE7; color:#166534; padding:12px 16px; border-radius:12px; font-size:13px; font-weight:600; margin-bottom:16px;">
        ✓ <?= htmlspecialchars($flash) ?>
    </div>
<?php endif; ?>

<div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 16px;">
    <?php foreach ($verifications as $v): ?>
        <div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:20px;">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:12px;">
                <div style="font-size:16px; font-weight:700; color:#0F0F0F;"><?= htmlspecialchars($v['user_name']) ?></div>
                <span class="status-pill status-<?= $v['status'] ?>"><?= $v['status'] ?></span>
            </div>

            <div style="font-size:13px; color:#71717A; line-height:1.7; margin-bottom:14px;">
                <div><strong>CNIC:</strong> <?= htmlspecialchars($v['cnic_number']) ?></div>
                <div><strong>Phone:</strong> <?= htmlspecialchars($v['phone']) ?></div>
                <div><strong>City:</strong> 📍 <?= htmlspecialchars($v['city']) ?></div>
            </div>

            <div style="display:flex; gap:8px; margin-bottom:14px;">
                <div style="flex:1; background:#F4F4F5; border-radius:8px; padding:10px; text-align:center; font-size:11px; color:#166534; font-weight:600;">
                    ✓ CNIC Front Attached
                </div>
                <div style="flex:1; background:#F4F4F5; border-radius:8px; padding:10px; text-align:center; font-size:11px; color:#166534; font-weight:600;">
                    ✓ CNIC Back Attached
                </div>
            </div>

            <?php if ($v['status'] === 'pending'): ?>
                <a href="verifications.php?action=approve&id=<?= $v['id'] ?>" class="btn btn-primary btn-block" style="background:#0095F6; font-size:13px;">
                    Approve & Award Blue Tick Badge
                </a>
            <?php else: ?>
                <div style="color:#0095F6; font-size:12px; font-weight:700; text-align:center; padding:8px;">
                    ✓ Blue Tick Active on Profile
                </div>
            <?php endif; ?>
        </div>
    <?php endforeach; ?>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
