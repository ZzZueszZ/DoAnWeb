-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: ecommerce_store_1
-- ------------------------------------------------------
-- Server version	8.0.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (2,1,18,3),(3,1,17,1);
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_image` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `parent_id` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (3,'Công nghệ thông tin','/uploads/categorys/IPOS2.png',1,NULL,'2024-12-06 16:50:57','2024-12-07 07:07:22'),(4,'Marketing','/uploads/categorys/logo-kase-2.png',1,NULL,'2024-12-06 17:32:36','2024-12-07 07:07:32');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chat`
--

DROP TABLE IF EXISTS `chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `message` text NOT NULL,
  `sent_by` enum('CONSULTANT','USER') NOT NULL,
  `sent_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_read` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `chat_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,1,'asdsa','USER','2024-12-07 08:39:51',0),(2,1,'test ae oiw','USER','2024-12-07 08:40:03',0),(3,1,'hello em','CONSULTANT','2024-12-07 08:43:57',0),(4,1,'dạ chào anh, em muốn  mua khóa học','USER','2024-12-07 08:44:17',0),(5,1,'oke em, em cần khóa gì?','CONSULTANT','2024-12-07 08:44:25',0),(6,5,'Giúp em với anh iw','USER','2024-12-07 08:45:47',0),(7,5,'sao vậy em?','CONSULTANT','2024-12-07 08:45:59',0),(8,5,'Em cần tư vấn mua khóa học á','USER','2024-12-07 08:46:15',0),(9,5,'oke em','CONSULTANT','2024-12-07 08:46:17',0);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `total_price` double NOT NULL,
  `status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PENDING',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` int NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_title` varchar(500) DEFAULT NULL,
  `product_description` text,
  `category_id` bigint DEFAULT NULL,
  `product_price` double DEFAULT NULL,
  `product_stock` int DEFAULT NULL,
  `product_image` varchar(255) DEFAULT NULL,
  `discount` int DEFAULT '0',
  `discount_price` double DEFAULT NULL,
  `discount_start_date` timestamp NULL DEFAULT NULL,
  `discount_end_date` timestamp NULL DEFAULT NULL,
  `is_discount_active` tinyint(1) DEFAULT '0',
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (6,'Khoa học 24h','tét',3,5000,1,'/uploads/products/JS.png',0,5000,NULL,NULL,NULL,1,'2024-12-07 06:04:07','2024-12-07 06:38:13'),(7,'Khóa dạy thực chiến marketing','test',4,5000,2,'/uploads/products/logo-kase-2.png',0,5000,NULL,NULL,NULL,1,'2024-12-07 06:05:03','2024-12-07 06:38:04'),(8,'HoiDanit','Khoá Nextjs14',3,10000,1,'/uploads/products/python.jpeg',0,10000,NULL,NULL,0,1,'2024-12-07 06:35:08','2024-12-07 06:35:08'),(9,'Gaming Laptop','High-performance laptop for gaming and work.',3,1500,10,'gaming_laptop.jpg',10,1350,'2024-11-30 17:00:00','2024-12-14 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 06:52:02'),(10,'Wireless Mouse','Ergonomic wireless mouse with fast response.',3,25,50,'wireless_mouse.jpg',0,5,NULL,NULL,0,1,'2024-12-07 06:52:02','2024-12-07 07:14:22'),(11,'Mechanical Keyboard','RGB mechanical keyboard for enthusiasts.',3,70,20,'mechanical_keyboard.jpg',15,59.5,'2024-12-04 17:00:00','2024-12-19 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 06:52:02'),(12,'4K Monitor','Ultra HD 27-inch monitor for professionals.',3,300,15,'4k_monitor.jpg',5,285,'2024-12-09 17:00:00','2024-12-24 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 06:52:02'),(13,'Gaming Headset','High-quality surround sound gaming headset.',3,120,30,'gaming_headset.jpg',20,96,'2024-12-04 17:00:00','2024-12-19 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 06:52:02'),(14,'External SSD','1TB portable SSD with fast read/write speeds.',3,150,25,'external_ssd.jpg',10,135,'2024-11-30 17:00:00','2024-12-09 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 06:52:02'),(15,'Webcam','1080p HD webcam with built-in microphone.',3,60,40,'webcam.jpg',0,25,NULL,NULL,0,1,'2024-12-07 06:52:02','2024-12-07 07:14:22'),(16,'Office Chair','Comfortable ergonomic office chair.',3,180,12,'office_chair.jpg',25,25,'2024-11-30 17:00:00','2024-12-30 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 07:14:22'),(17,'Graphic Tablet','Professional graphic tablet for designers.',3,250,8,'graphic_tablet.jpg',10,25,'2024-11-30 17:00:00','2024-12-14 17:00:00',1,1,'2024-12-07 06:52:02','2024-12-07 07:14:22'),(18,'Smartphone Stand','Adjustable stand for smartphones and tablets.',3,20,100,'smartphone_stand.jpg',0,25,NULL,NULL,0,1,'2024-12-07 06:52:02','2024-12-07 07:14:22');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `review_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `address` text,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `pin_code` varchar(10) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `role` enum('ROLE_USER','ROLE_CONSULTANT','ROLE_ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'ROLE_USER',
  `is_enable` tinyint(1) DEFAULT '1',
  `account_status_non_locked` tinyint(1) DEFAULT '1',
  `account_failed_attempt_count` int DEFAULT '0',
  `account_lock_time` datetime DEFAULT NULL,
  `reset_tokens` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'TEST','0876134948','0905071704b@gmail.com','Hiep Binh Chanh Ward, Thủ Đức, Ho Chi Minh City, 72309, Vietnam','test','test','0123321','$2a$10$rBjYTI0aP00oWJ2bJ94uIOa1WBEVXV3GwVxmYJcvU49iuu3f2YJ0G','/uploads/profile/45 (1).JPG','ROLE_USER',1,1,2,NULL,NULL,'2024-12-06 16:34:33','2024-12-07 08:32:10'),(2,'TEST','0876134948','22162022@student.hcmute.edu.vn','Hiep Binh Chanh Ward, Thủ Đức, Ho Chi Minh City, 72309, Vietnam','test','test','0123321','$2a$10$euBGSFvGkiOeMudxsGapSOOto85oVE1Kq2BqOJdgALLLYch6S7Jl2','default.jpg','ROLE_ADMIN',1,1,1,NULL,NULL,'2024-12-06 16:36:44','2024-12-07 07:07:05'),(3,'TEST','0876134948','ngthanhloc304@gmail.com','Hiep Binh Chanh Ward, Thủ Đức, Ho Chi Minh City, 72309, Vietnam','test','test','0123321','$2a$10$qzdWEsdfsz/O7Cal5E0Vd.zLowwmaO0HKxFB36uOH.VHjEMvrba3u','/uploads/profile/Kase edu2.jpg','ROLE_CONSULTANT',1,1,2,NULL,NULL,'2024-12-07 07:52:42','2024-12-07 09:01:26'),(4,'TEST','0876134948','0905071704d@gmail.com','Hiep Binh Chanh Ward, Thủ Đức, Ho Chi Minh City, 72309, Vietnam','test','test','0123321','$2a$10$KRYJOvCAkLB9lfmhgSHJMOsgzx6A2tj33J5A0k81.lxdoiXCWWcdG','default.jpg','ROLE_USER',1,1,2,NULL,NULL,'2024-12-07 08:45:04','2024-12-07 08:45:22'),(5,'TEST','0876134948','0905071704a@gmail.com','Hiep Binh Chanh Ward, Thủ Đức, Ho Chi Minh City, 72309, Vietnam','test','test','0123321','$2a$10$LNHRz5ZmfpX6lveUPOfpO.pZal1b55O2BsYQvUd7HrN2qymcCaanS','default.jpg','ROLE_USER',1,1,0,NULL,NULL,'2024-12-07 08:45:32','2024-12-07 08:45:32');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'ecommerce_store_1'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-07 16:02:04
