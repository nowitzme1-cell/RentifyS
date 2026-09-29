<?php
require_once __DIR__ . '/../config.php';

class DB {
    private static ?PDO $instance = null;

    public static function connect(): PDO {
        if (self::$instance === null) {
            try {
                $dsn = "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=utf8mb4";
                self::$instance = new PDO($dsn, DB_USER, DB_PASS, [
                    PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
                    PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                    PDO::ATTR_EMULATE_PREPARES => false,
                ]);
            } catch (PDOException $e) {
                // If database does not exist or install needed
                if (basename($_SERVER['PHP_SELF']) !== 'install.php') {
                    header('Location: install.php');
                    exit;
                }
                throw $e;
            }
        }
        return self::$instance;
    }
}

// JSON response helper
function jsonResponse($data, int $statusCode = 200): void {
    http_response_code($statusCode);
    header('Content-Type: application/json; charset=utf-8');
    echo json_encode($data, JSON_UNESCAPED_UNICODE | JSON_PRETTY_PRINT);
    exit;
}

// Clean input helper
function cleanInput($data): string {
    return htmlspecialchars(trim((string)$data), ENT_QUOTES, 'UTF-8');
}

// Session helper for current demo persona
function getCurrentUser(): array {
    if (!isset($_SESSION['user'])) {
        $_SESSION['user'] = [
            'id' => 1,
            'name' => 'Bilal Ahmed',
            'phone' => '0301-7654321',
            'city' => 'Kashmore',
            'role' => 'Renter',
            'is_verified' => 1,
            'items_count' => 2,
            'rating' => 4.9
        ];
    }
    return $_SESSION['user'];
}
