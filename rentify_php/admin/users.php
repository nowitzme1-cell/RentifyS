<?php
require_once __DIR__ . '/header.php';

$users = [
    ['name' => 'Salman Khan', 'phone' => '0300-8392104', 'city' => 'Kashmore', 'role' => 'Owner', 'items' => 12, 'rating' => 4.9, 'verified' => 1],
    ['name' => 'Bilal Ahmed', 'phone' => '0301-7654321', 'city' => 'Kashmore', 'role' => 'Renter', 'items' => 2, 'rating' => 4.9, 'verified' => 1],
    ['name' => 'Ahmed Ali', 'phone' => '0302-1144778', 'city' => 'Kashmore', 'role' => 'Owner', 'items' => 4, 'rating' => 4.8, 'verified' => 1],
    ['name' => 'Farooq Jamali', 'phone' => '0333-7890123', 'city' => 'Usta Muhammad', 'role' => 'Owner', 'items' => 2, 'rating' => 4.7, 'verified' => 1],
    ['name' => 'Tariq Baloch', 'phone' => '0300-3344556', 'city' => 'Kandhkot', 'role' => 'Owner', 'items' => 3, 'rating' => 4.9, 'verified' => 1],
    ['name' => 'Sarmad Malik', 'phone' => '0345-9876543', 'city' => 'Jacobabad', 'role' => 'Owner', 'items' => 8, 'rating' => 5.0, 'verified' => 1],
];
?>

<div style="margin-bottom:20px;">
    <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F;">Registered Members</h1>
    <p style="font-size: 13px; color: #71717A;">Owners and Renters across Kashmore, Usta Muhammad and Sindh</p>
</div>

<div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; overflow:hidden;">
    <table style="width:100%; border-collapse:collapse; text-align:left; font-size:13px;">
        <thead>
            <tr style="background:#F4F4F5; color:#71717A; font-weight:600;">
                <th style="padding:14px 16px;">User</th>
                <th style="padding:14px 16px;">Mobile</th>
                <th style="padding:14px 16px;">City</th>
                <th style="padding:14px 16px;">Role</th>
                <th style="padding:14px 16px;">Active Items</th>
                <th style="padding:14px 16px;">Rating</th>
                <th style="padding:14px 16px;">CNIC Status</th>
            </tr>
        </thead>
        <tbody>
            <?php foreach ($users as $u): ?>
                <tr style="border-bottom:1px solid #F4F4F5;">
                    <td style="padding:14px 16px; font-weight:700; color:#0F0F0F;">
                        <?= htmlspecialchars($u['name']) ?>
                        <?php if ($u['verified']): ?><span class="blue-tick">✓</span><?php endif; ?>
                    </td>
                    <td style="padding:14px 16px;"><?= htmlspecialchars($u['phone']) ?></td>
                    <td style="padding:14px 16px;">📍 <?= htmlspecialchars($u['city']) ?></td>
                    <td style="padding:14px 16px;"><?= htmlspecialchars($u['role']) ?></td>
                    <td style="padding:14px 16px; font-weight:600;"><?= $u['items'] ?></td>
                    <td style="padding:14px 16px;">★ <?= $u['rating'] ?></td>
                    <td style="padding:14px 16px;">
                        <span class="status-pill status-approved">Verified Blue Tick</span>
                    </td>
                </tr>
            <?php endforeach; ?>
        </tbody>
    </table>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
