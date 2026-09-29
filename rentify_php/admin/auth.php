<?php
require_once __DIR__ . '/../includes/db.php';

function checkAdminAuth(): void {
    if (empty($_SESSION['admin_logged_in'])) {
        header('Location: login.php');
        exit;
    }
}
