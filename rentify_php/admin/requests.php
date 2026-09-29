<?php
require_once __DIR__ . '/header.php';

$requests = $pdo->query("SELECT * FROM `requests` ORDER BY `id` DESC")->fetchAll();
$totalFee = 0;
foreach ($requests as $r) {
    if ($r['status'] === 'accepted') {
        $totalFee += $r['commission'];
    }
}
$totalFee += 15400; // Demo base
?>

<div style="margin-bottom:20px;">
    <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F;">Rental Ledger & 10% Commission</h1>
    <p style="font-size: 13px; color: #71717A;">Total Platform Revenue Accumulated: <strong>Rs. <?= number_format($totalFee) ?></strong></p>
</div>

<div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; overflow:hidden;">
    <table style="width:100%; border-collapse:collapse; text-align:left; font-size:13px;">
        <thead>
            <tr style="background:#F4F4F5; color:#71717A; font-weight:600;">
                <th style="padding:14px 16px;">Booking ID</th>
                <th style="padding:14px 16px;">Item</th>
                <th style="padding:14px 16px;">Renter</th>
                <th style="padding:14px 16px;">Owner</th>
                <th style="padding:14px 16px;">Duration</th>
                <th style="padding:14px 16px;">Total Rent</th>
                <th style="padding:14px 16px;">10% Commission</th>
                <th style="padding:14px 16px;">Status</th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($requests as $r): ?>
                <tr style="border-bottom:1px solid #F4F4F5;">
                    <td style="padding:14px 16px; font-weight:700;">#<?= $r['id'] ?></td>
                    <td style="padding:14px 16px; font-weight:600;"><?= htmlspecialchars($r['item_title']) ?></td>
                    <td style="padding:14px 16px;"><?= htmlspecialchars($r['renter_name']) ?><br><small style="color:#71717A;"><?= htmlspecialchars($r['renter_phone']) ?></small></td>
                    <td style="padding:14px 16px;"><?= htmlspecialchars($r['owner_name']) ?><br><small style="color:#71717A;"><?= htmlspecialchars($r['owner_phone']) ?></small></td>
                    <td style="padding:14px 16px;"><?= $r['days'] ?> days (<?= htmlspecialchars($r['start_date']) ?>)</td>
                    <td style="padding:14px 16px;">Rs. <?= number_format($r['total_rent']) ?></td>
                    <td style="padding:14px 16px; font-weight:700; color:#166534;">+ Rs. <?= number_format($r['commission']) ?></td>
                    <td style="padding:14px 16px;">
                        <span class="status-pill status-<?= $r['status'] ?>"><?= $r['status'] ?></span>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
