<?php
require_once __DIR__ . '/../includes/db.php';
unset($_SESSION['admin_logged_in']);
unset($_SESSION['admin_email']);
header('Location: login.php');
exit;
