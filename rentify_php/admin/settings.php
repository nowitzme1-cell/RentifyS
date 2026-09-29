<?php
require_once __DIR__ . '/header.php';

$saved = false;
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $saved = true;
}
?>

<div style="margin-bottom:20px;">
    <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F;">Marketplace Settings</h1>
    <p style="font-size: 13px; color: #71717A;">Platform fee percentage, helpline, and launch cities</p>
</div>

<?php if ($saved): ?>
    <div style="background:#DCFCE7; color:#166534; padding:12px 16px; border-radius:12px; font-size:13px; font-weight:600; margin-bottom:16px;">
        ✓ Settings updated successfully!
    </div>
<?php endif; ?>

<div style="background:#FFF; border:1px solid #F0F0F0; border-radius:20px; padding:28px; max-width:640px;">
    <form method="POST">
        <div style="margin-bottom:16px;">
            <label style="display:block; font-size:13px; font-weight:600; margin-bottom:6px;">Platform Rental Commission (%)</label>
            <input type="number" name="commission_rate" value="10" class="input-pill" required>
            <small style="color:#71717A; font-size:11px;">10% automatically deducted from total rental sum upon booking.</small>
        </div>

        <div style="margin-bottom:16px;">
            <label style="display:block; font-size:13px; font-weight:600; margin-bottom:6px;">Default Marketplace City</label>
            <input type="text" name="default_city" value="Kashmore" class="input-pill" required>
        </div>

        <div style="margin-bottom:16px;">
            <label style="display:block; font-size:13px; font-weight:600; margin-bottom:6px;">Phase 1 Coverage Cities</label>
            <textarea class="textarea-pill" style="height:70px;">Kashmore, Kandhkot, Usta Muhammad, Sui, Dera Allah Yar, Jacobabad, Shikarpur</textarea>
        </div>

        <div style="margin-bottom:24px;">
            <label style="display:block; font-size:13px; font-weight:600; margin-bottom:6px;">Helpline WhatsApp Number</label>
            <input type="text" name="helpline" value="0300-RENTIFY (0300-7368439)" class="input-pill" required>
        </div>

        <button type="submit" class="btn btn-primary" style="padding:12px 28px;">Save Settings</button>
    </form>
</div>

<?php require_once __DIR__ . '/footer.php'; ?>
