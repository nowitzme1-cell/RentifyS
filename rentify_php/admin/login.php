<?php
require_once __DIR__ . '/../includes/db.php';

$error = '';
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $email = cleanInput($_POST['email'] ?? '');
    $password = $_POST['password'] ?? '';

    // Direct credentials or DB check
    if (($email === 'admin@rentify.pk' && $password === 'admin123') || isset($_POST['demo_login'])) {
        $_SESSION['admin_logged_in'] = true;
        $_SESSION['admin_email'] = 'admin@rentify.pk';
        header('Location: index.php');
        exit;
    } else {
        $error = 'Invalid credentials. Default: admin@rentify.pk / admin123';
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login - Rentify.pk</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/app.css">
</head>
<body style="background: #0F0F0F; display:flex; align-items:center; justify-content:center; min-height: 100vh; padding: 20px;">
    <div style="background: #FFFFFF; border-radius: 24px; max-width: 380px; width: 100%; padding: 32px; box-shadow: 0 8px 32px rgba(0,0,0,0.4);">
        <div style="text-align: center; margin-bottom: 24px;">
            <h1 style="font-size: 26px; font-weight: 700; color: #0F0F0F; margin: 0;">rentify<span style="color:#635BFF;">. admin</span></h1>
            <p style="font-size: 13px; color: #71717A; margin-top: 6px;">Secret Access Only • Kashmore HQ</p>
        </div>

        <?php if ($error): ?>
            <div style="background: #FEE2E2; color: #991B1B; padding: 12px; border-radius: 12px; font-size: 13px; margin-bottom: 16px;">
                <?= $error ?>
            </div>
        <?php endif; ?>

        <form method="POST">
            <div style="margin-bottom: 14px;">
                <label style="display:block; font-size: 13px; font-weight: 500; margin-bottom: 6px;">Admin Email</label>
                <input type="email" name="email" value="admin@rentify.pk" class="input-pill" required>
            </div>
            <div style="margin-bottom: 20px;">
                <label style="display:block; font-size: 13px; font-weight: 500; margin-bottom: 6px;">Password</label>
                <input type="password" name="password" value="admin123" class="input-pill" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block" style="margin-bottom: 10px;">Login to Admin Dashboard</button>
            <button type="submit" name="demo_login" value="1" class="btn btn-secondary btn-block">1-Click Instant Demo Login</button>
        </form>

        <div style="text-align: center; margin-top: 20px;">
            <a href="../index.php" style="font-size: 13px; color: #71717A; text-decoration: none;">← Back to Rentify Marketplace</a>
        </div>
    </div>
</body>
</html>
