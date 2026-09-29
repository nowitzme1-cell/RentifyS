<?php
/**
 * Rentify.pk - Database & Environment Configuration
 * Ready for Hostinger public_html and InfinityFree htdocs
 */

// Database Credentials (Update with your Hostinger / InfinityFree MySQL details)
define('DB_HOST', 'localhost');
define('DB_NAME', 'rentify_db');
define('DB_USER', 'root');
define('DB_PASS', '');

// App Settings
define('APP_NAME', 'rentify.');
define('APP_TITLE', 'Rentify.pk - Pakistan\'s First P2P Rental Marketplace');
define('DEFAULT_CITY', 'Kashmore');
define('COMMISSION_RATE', 10); // 10% platform fee
define('CURRENCY', 'Rs. ');

// Timezone
date_default_timezone_set('Asia/Karachi');

// Session Start
if (session_status() === PHP_SESSION_NONE) {
    session_start();
}
