<?php
require_once __DIR__ . '/auth.php';
checkAdminAuth();

$pdo = DB::connect();
$pendingCount = $pdo->query("SELECT COUNT(*) FROM `items` WHERE `status` = 'pending'")->fetchColumn();
$currentPage = basename($_SERVER['PHP_SELF']);
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rentify Admin Panel</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/app.css">
    <style>
        .admin-layout {
            display: flex;
            min-height: 100vh;
            background: #0F0F0F;
        }
        .admin-sidebar {
            width: 260px;
            background: #0F0F0F;
            border-right: 1px solid #27272A;
            padding: 24px 16px;
            display: flex;
            flex-direction: column;
        }
        .admin-nav-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 12px 16px;
            border-radius: 999px;
            color: #A1A1AA;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 6px;
            transition: all 0.15s ease;
        }
        .admin-nav-item:hover {
            color: #FFFFFF;
            background: #18181B;
        }
        .admin-nav-item.active {
            background: #FFFFFF;
            color: #0F0F0F;
            font-weight: 700;
        }
        .admin-main {
            flex: 1;
            background: #FAFAFA;
            padding: 28px;
            border-top-left-radius: 24px;
            overflow-y: auto;
        }
        @media (max-width: 768px) {
            .admin-layout {
                flex-direction: column;
            }
            .admin-sidebar {
                width: 100%;
                border-right: none;
                border-bottom: 1px solid #27272A;
                padding: 16px;
            }
            .admin-nav-links {
                display: flex;
                overflow-x: auto;
                gap: 6px;
            }
            .admin-main {
                border-top-left-radius: 0;
                padding: 16px;
            }
        }
    </style>
</head>
<body>
    <div class="admin-layout">
        <!-- Sidebar -->
        <aside class="admin-sidebar">
            <div style="margin-bottom: 28px; padding-left: 8px;">
                <a href="index.php" class="logo" style="color: #FFFFFF; font-size: 22px;">
                    rentify<span class="dot">. admin</span>
                </a>
            </div>

            <nav class="admin-nav-links" style="flex: 1;">
                <a href="index.php" class="admin-nav-item <?= $currentPage === 'index.php' ? 'active' : '' ?>">
                    <span>📊 Dashboard</span>
                </a>
                <a href="items.php" class="admin-nav-item <?= $currentPage === 'items.php' ? 'active' : '' ?>">
                    <span>📦 Items Moderation</span>
                    <?php if ($pendingCount > 0): ?>
                        <span style="background: #EF4444; color: #FFF; font-size: 11px; padding: 2px 7px; border-radius: 999px;"><?= $pendingCount ?></span>
                    <?php endif; ?>
                </a>
                <a href="requests.php" class="admin-nav-item <?= $currentPage === 'requests.php' ? 'active' : '' ?>">
                    <span>💳 Rental Ledger</span>
                </a>
                <a href="verifications.php" class="admin-nav-item <?= $currentPage === 'verifications.php' ? 'active' : '' ?>">
                    <span>🛡️ CNIC Blue Ticks</span>
                </a>
                <a href="users.php" class="admin-nav-item <?= $currentPage === 'users.php' ? 'active' : '' ?>">
                    <span>👥 Users</span>
                </a>
                <a href="settings.php" class="admin-nav-item <?= $currentPage === 'settings.php' ? 'active' : '' ?>">
                    <span>⚙️ Settings</span>
                </a>
            </nav>

            <div style="padding-top: 16px; border-top: 1px solid #27272A;">
                <a href="../index.php" class="admin-nav-item" style="color: #A1A1AA;">
                    <span>← Client App</span>
                </a>
                <a href="logout.php" class="admin-nav-item" style="color: #EF4444;">
                    <span>🚪 Logout</span>
                </a>
            </div>
        </aside>

        <!-- Main Content Area -->
        <main class="admin-main">
