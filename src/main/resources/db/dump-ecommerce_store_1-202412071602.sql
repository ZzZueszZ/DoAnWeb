-- Xoá các bảng nếu đã tồn tại (theo thứ tự phụ thuộc)
DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `review`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `chat`;
DROP TABLE IF EXISTS `user`;

-- Tạo bảng `user`
CREATE TABLE `user` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `name` VARCHAR(255) DEFAULT NULL,
                        `mobile` VARCHAR(20) DEFAULT NULL,
                        `email` VARCHAR(255) NOT NULL,
                        `address` TEXT,
                        `city` VARCHAR(100) DEFAULT NULL,
                        `state` VARCHAR(100) DEFAULT NULL,
                        `pin_code` VARCHAR(10) DEFAULT NULL,
                        `password` VARCHAR(255) DEFAULT NULL,
                        `profile_image` VARCHAR(255) DEFAULT NULL,
                        `role` ENUM('ROLE_USER', 'ROLE_CONSULTANT', 'ROLE_ADMIN') DEFAULT 'ROLE_USER',
                        `is_enable` TINYINT(1) DEFAULT '1',
                        `account_status_non_locked` TINYINT(1) DEFAULT '1',
                        `account_failed_attempt_count` INT DEFAULT '0',
                        `account_lock_time` DATETIME DEFAULT NULL,
                        `reset_tokens` VARCHAR(255) DEFAULT NULL,
                        `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                        `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        PRIMARY KEY (`id`),
                        KEY `idx_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `category`
CREATE TABLE `category` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `category_name` VARCHAR(255) NOT NULL,
                            `category_image` VARCHAR(255) DEFAULT NULL,
                            `is_active` TINYINT(1) DEFAULT '1',
                            `parent_id` BIGINT DEFAULT NULL,
                            `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `parent_id` (`parent_id`),
                            CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `product`
CREATE TABLE `product` (
                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                           `product_title` VARCHAR(500) DEFAULT NULL,
                           `product_description` TEXT,
                           `category_id` BIGINT DEFAULT NULL,
                           `product_price` DOUBLE DEFAULT NULL,
                           `product_stock` INT DEFAULT NULL,
                           `product_image` VARCHAR(255) DEFAULT NULL,
                           `discount` INT DEFAULT '0',
                           `discount_price` DOUBLE DEFAULT NULL,
                           `discount_start_date` TIMESTAMP NULL DEFAULT NULL,
                           `discount_end_date` TIMESTAMP NULL DEFAULT NULL,
                           `is_discount_active` TINYINT(1) DEFAULT '0',
                           `is_active` TINYINT(1) DEFAULT '1',
                           `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                           `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (`id`),
                           KEY `category_id` (`category_id`),
                           CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `cart`
CREATE TABLE `cart` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `user_id` BIGINT DEFAULT NULL,
                        `product_id` BIGINT DEFAULT NULL,
                        `quantity` INT DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `user_id` (`user_id`),
                        KEY `product_id` (`product_id`),
                        CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                        CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `orders`
CREATE TABLE `orders` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `total_price` DOUBLE NOT NULL,
                          `status` ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
                          `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          KEY `user_id` (`user_id`),
                          CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `order_item`
CREATE TABLE `order_item` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT,
                              `order_id` BIGINT NOT NULL,
                              `product_id` BIGINT NOT NULL,
                              `quantity` INT NOT NULL,
                              `price` DOUBLE NOT NULL,
                              PRIMARY KEY (`id`),
                              KEY `order_id` (`order_id`),
                              KEY `product_id` (`product_id`),
                              CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                              CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `review`
CREATE TABLE `review` (
                          `id` BIGINT NOT NULL AUTO_INCREMENT,
                          `user_id` BIGINT NOT NULL,
                          `product_id` BIGINT NOT NULL,
                          `rating` INT NOT NULL CHECK (`rating` BETWEEN 1 AND 5),
                          `comment` TEXT,
                          `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          KEY `user_id` (`user_id`),
                          KEY `product_id` (`product_id`),
                          CONSTRAINT `review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                          CONSTRAINT `review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Tạo bảng `chat`
CREATE TABLE `chat` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `user_id` BIGINT NOT NULL,
                        `message` TEXT NOT NULL,
                        `sent_by` ENUM('CONSULTANT', 'USER') NOT NULL,
                        `sent_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
                        `is_read` TINYINT(1) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `user_id` (`user_id`),
                        CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dữ liệu mẫu
INSERT INTO `user` VALUES
    (1, 'TEST', '0876134948', 'test@example.com', 'Address 1', 'City1', 'State1', '12345', 'password', NULL, 'ROLE_USER', 1, 1, 0, NULL, NULL, NOW(), NOW());

INSERT INTO `category` VALUES
    (1, 'Electronics', NULL, 1, NULL, NOW(), NOW());

INSERT INTO `product` VALUES
    (1, 'Phone', 'Smartphone', 1, 699.99, 50, NULL, 0, 699.99, NULL, NULL, 0, 1, NOW(), NOW());

INSERT INTO `orders` VALUES
    (1, 1, 699.99, 'PENDING', NOW(), NOW());

INSERT INTO `order_item` VALUES
    (1, 1, 1, 1, 699.99);

INSERT INTO `cart` VALUES
    (1, 1, 1, 1);

INSERT INTO `chat` VALUES
    (1, 1, 'Hello, how can I help you?', 'CONSULTANT', NOW(), 0);

INSERT INTO `review` VALUES
    (1, 1, 1, 5, 'Great product!', NOW());
