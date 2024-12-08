-- Tắt kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS=0;

-- Tạo cơ sở dữ liệu nếu chưa tồn tại
CREATE DATABASE IF NOT EXISTS ecoommerce_store2 CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- Sử dụng cơ sở dữ liệu vừa tạo
USE ecoommerce_store2;

--- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: ecoommerce_store
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
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
                        CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
INSERT INTO `cart` VALUES (1,1,1,1);
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
                            `category_name` varchar(255) NOT NULL,
                            `category_image` varchar(255) DEFAULT NULL,
                            `is_active` tinyint(1) DEFAULT '1',
                            `parent_id` bigint DEFAULT NULL,
                            `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            PRIMARY KEY (`id`),
                            KEY `parent_id` (`parent_id`),
                            CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'IT Software','/uploads/categorys/software-development-specialist.jpg',1,NULL,'2024-12-08 16:38:34','2024-12-08 17:38:37'),(2,'Business','/uploads/categorys/images.jpg',1,NULL,'2024-12-08 16:48:42','2024-12-08 17:39:29'),(3,'Design Creativity','/uploads/categorys/online-graphic-design-courses-the-most-comprehensive-guide-11.jpg',1,NULL,'2024-12-08 16:48:54','2024-12-08 17:40:49'),(4,'Personal Development','/uploads/categorys/best-personal-development-courses.jpg',1,NULL,'2024-12-08 16:49:03','2024-12-08 17:42:45'),(5,'Language','/uploads/categorys/1593692498phpqoP7Fd.jpeg',1,NULL,'2024-12-08 16:49:14','2024-12-08 17:42:52');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chat`
--

LOCK TABLES `chat` WRITE;
/*!40000 ALTER TABLE `chat` DISABLE KEYS */;
INSERT INTO `chat` VALUES (1,1,'Hello, how can I help you?','CONSULTANT','2024-12-08 16:38:34',0);
/*!40000 ALTER TABLE `chat` ENABLE KEYS */;
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
                              CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                              CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,1,1,699.99);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `user_id` bigint NOT NULL,
                          `total_price` double NOT NULL,
                          `status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PENDING',
                          `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          PRIMARY KEY (`id`),
                          KEY `user_id` (`user_id`),
                          CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,699.99,'PENDING','2024-12-08 16:38:34','2024-12-08 16:38:34');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
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
                           CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Python for Data Science','Become a data analysis expert with Python, NumPy, Pandas, and Matplotlib',1,150,70,'/uploads/products/Python-Data-Science-Tutorial.jpg',30,105,NULL,NULL,NULL,1,'2024-12-08 16:38:34','2024-12-08 18:47:55'),(2,'Full-Stack Web Development','Learn how to build web applications from scratch using React, Node.js, Express, and MongoDB',1,200,50,'/uploads/products/full-stack-web-development.jpg',20,160,NULL,NULL,NULL,1,'2024-12-08 16:52:02','2024-12-08 18:48:25'),(3,'Ethical Hacking Basics','Explore fundamental hacking techniques and learn how to secure systems',1,74.99,30,'/uploads/products/Ethical-Hacking-Course-Image-GoEdu.jpg',0,74.99,NULL,NULL,NULL,1,'2024-12-08 17:03:24','2024-12-08 18:48:38'),(4,'Cloud Computing with AWS','Understand how cloud computing works and deploy applications using AWS',1,249.99,40,'/uploads/products/AWS-Cloud-Computing-Course-1200x900.jpg',0,249.99,NULL,NULL,NULL,1,'2024-12-08 17:04:30','2024-12-08 18:48:48'),(5,'Machine Learning A-Z','Learn popular machine learning algorithms using Python and Scikit-learn',1,299.99,25,'/uploads/products/ml_.jpg',0,299.99,NULL,NULL,NULL,1,'2024-12-08 17:04:56','2024-12-08 18:48:58'),(6,'Introduction to Cybersecurity','Master basic cybersecurity concepts and essential security tools',1,129.99,60,'/uploads/products/sec.jpg',0,129.99,NULL,NULL,NULL,1,'2024-12-08 17:05:34','2024-12-08 18:48:07'),(7,'Java Programming Masterclass','Learn Java programming from basics to advanced, including Spring Boot',1,199.99,35,'/uploads/products/images.jpg',0,199.99,NULL,NULL,NULL,1,'2024-12-08 17:06:38','2024-12-08 18:50:39'),(8,'DevOps Essentials','Discover essential DevOps tools like Docker, Kubernetes, and Jenkins',1,229.99,42,'/uploads/products/devops-essential-training.jpg',0,229.99,NULL,NULL,NULL,1,'2024-12-08 17:07:06','2024-12-08 18:49:07'),(9,'Artificial Intelligence Fundamentals','Gain a solid understanding of AI, from neural networks to real-world applications',1,349.99,32,'/uploads/products/ai.jpg',0,349.99,NULL,NULL,NULL,1,'2024-12-08 17:07:36','2024-12-08 18:48:16'),(10,'SQL and Database Management','Master SQL and database management skills with MySQL and PostgreSQL',1,99.99,78,'/uploads/products/news-sql-full-course.jpg',0,99.99,NULL,NULL,NULL,1,'2024-12-08 17:08:07','2024-12-08 18:47:47'),(11,'Business Analytics Essentials','Learn how to analyze and interpret business data to make informed decisions',2,200,52,'/uploads/products/anal.jpg',13,174,NULL,NULL,NULL,1,'2024-12-08 17:09:20','2024-12-08 18:40:14'),(12,'Digital Marketing Masterclass','Master SEO, Google Ads, and social media marketing to grow your business online',2,149,59,'/uploads/products/mar.jpg',16,125.16,NULL,NULL,NULL,1,'2024-12-08 17:09:58','2024-12-08 18:39:52'),(13,'Financial Accounting Basics','Learn accounting principles and how to manage financial statements effectively',2,129.99,13,'/uploads/products/Financial.jpg',0,129.99,NULL,NULL,NULL,1,'2024-12-08 17:11:12','2024-12-08 18:39:14'),(14,'Entrepreneurship 101','Discover the fundamentals of starting and running your own business successfully',2,199.99,20,'/uploads/products/entrepreneur3.jpg',0,199.99,NULL,NULL,NULL,1,'2024-12-08 17:11:35','2024-12-08 18:39:32'),(15,'Project Management Professional','Prepare for the PMP certification and master project management frameworks',2,249.99,25,'/uploads/products/project.jpg',0,249.99,NULL,NULL,NULL,1,'2024-12-08 17:12:03','2024-12-08 18:38:59'),(16,'Sales Strategies for Success','Learn effective sales techniques and strategies to close more deals',2,99.99,23,'/uploads/products/Sales Startegy courses.jpg',0,99.99,NULL,NULL,NULL,1,'2024-12-08 17:12:31','2024-12-08 18:38:50'),(17,'Leadership and Management Skills','Develop leadership skills to inspire and manage teams effectively',2,179.99,50,'/uploads/products/Management-and-Leadership-Training.jpg',0,179.99,NULL,NULL,NULL,1,'2024-12-08 17:13:09','2024-12-08 18:38:37'),(18,'Human Resource Management','Learn HR principles, hiring strategies, and employee management techniques',2,129.99,24,'/uploads/products/Advance-Your-Career-With-Distance-Learning-Courses-in-Human-Resource-Management.jpg',0,129.99,NULL,NULL,NULL,1,'2024-12-08 17:13:33','2024-12-08 18:38:27'),(19,'Strategic Planning for Businesses','Master the art of creating and implementing strategic plans for growth',2,219.99,40,'/uploads/products/strategic.jpg',0,219.99,NULL,NULL,NULL,1,'2024-12-08 17:14:01','2024-12-08 18:38:13'),(20,'E-Commerce Essentials','Learn how to build, manage, and scale an online store for your business',2,129.99,36,'/uploads/products/e-com.jpg',0,129.99,NULL,NULL,NULL,1,'2024-12-08 17:14:22','2024-12-08 18:38:02'),(21,'Graphic Design for Beginners','Learn the basics of graphic design using tools like Adobe Photoshop and Illustrator',3,119.67,12,'/uploads/products/graphic-design-course-beg-selar.co-64a2d2cc0dc41.jpg',0,119.67,NULL,NULL,NULL,1,'2024-12-08 17:16:09','2024-12-08 18:27:58'),(22,'UI/UX Design Fundamentals','Master user interface and user experience design to create intuitive applications',3,117,29,'/uploads/products/00960_AIM_Screens_UXDesignFundamentals_640x360.jpg',16,98.28,NULL,NULL,NULL,1,'2024-12-08 17:16:41','2024-12-08 18:27:49'),(23,'Digital Illustration Masterclass','Create stunning digital illustrations using tools like Procreate and Adobe Fresco',3,149.98,42,'/uploads/products/digital.jpg',0,149.98,NULL,NULL,NULL,1,'2024-12-08 17:17:23','2024-12-08 18:27:40'),(24,'Photography Composition Techniques','Enhance your photography skills by mastering composition and storytelling',3,199.99,79,'/uploads/products/photo.jpg',0,199.99,NULL,NULL,NULL,1,'2024-12-08 17:17:55','2024-12-08 18:27:30'),(25,'Motion Graphics Essentials','Learn to create motion graphics and animations using After Effects',3,165.45,10,'/uploads/products/motion.jpg',0,165.45,NULL,NULL,NULL,1,'2024-12-08 17:18:27','2024-12-08 18:27:21'),(26,'Creative Writing Workshop','Unlock your creativity and learn to write compelling stories and content',3,76.49,9,'/uploads/products/creative-writing-workshop.jpg',0,76.49,NULL,NULL,NULL,1,'2024-12-08 17:18:57','2024-12-08 18:27:13'),(27,'Logo Design Secrets','Learn the principles of logo design to create memorable brand identities',3,80.23,24,'/uploads/products/logo.jpg',0,80.23,NULL,NULL,NULL,1,'2024-12-08 17:19:25','2024-12-08 18:27:02'),(28,'Interior Design Basics','Explore the fundamentals of interior design to create functional and beautiful spaces',3,90.13,43,'/uploads/products/inter.jpg',0,90.13,NULL,NULL,NULL,1,'2024-12-08 17:19:50','2024-12-08 18:26:54'),(29,'Video Editing Masterclass','Master video editing techniques using Premiere Pro and Final Cut Pro',3,139.99,50,'/uploads/products/professional-video-editin-selar.co-6446fffb25430.jpg',0,139.99,NULL,NULL,NULL,1,'2024-12-08 17:20:23','2024-12-08 18:26:43'),(30,'Fashion Design Essentials','Learn the basics of fashion design and create your own clothing line',3,169.72,25,'/uploads/products/Title-Image-6-626x410.jpg',0,169.72,NULL,NULL,NULL,1,'2024-12-08 17:20:53','2024-12-08 18:26:33'),(31,'Time Management for Professionals','Learn effective time management strategies to increase productivity and achieve your goals',4,79.9,21,'/uploads/products/Effective-Time-Management-Skills-Course.jpg',0,79.9,NULL,NULL,NULL,1,'2024-12-08 17:23:55','2024-12-08 18:15:21'),(32,'Mindfulness and Meditation Mastery','Master mindfulness techniques and meditation practices to reduce stress and improve focus',4,69.9,56,'/uploads/products/mindful.jpg',13,60.813,NULL,NULL,NULL,1,'2024-12-08 17:24:25','2024-12-08 18:15:14'),(33,'Building Self-Confidence','Discover techniques to boost your self-esteem and increase your confidence in any situation',4,89.99,70,'/uploads/products/confi.jpg',0,89.99,NULL,NULL,NULL,1,'2024-12-08 17:25:01','2024-12-08 18:15:03'),(34,'Public Speaking Mastery','Become a confident public speaker by learning presentation skills and overcoming stage fright',4,119.37,34,'/uploads/products/speak.jpg',0,119.37,NULL,NULL,NULL,1,'2024-12-08 17:25:36','2024-12-08 18:14:42'),(35,'Leadership Skills Development','Learn the essential skills needed to become an effective and inspiring leader',4,89.24,12,'/uploads/products/leader.jpg',0,89.24,NULL,NULL,NULL,1,'2024-12-08 17:25:59','2024-12-08 18:14:18'),(36,'Goal Setting and Achievement','Master the art of setting realistic goals and taking actionable steps to achieve them',4,56.13,22,'/uploads/products/goal.jpg',0,56.13,NULL,NULL,NULL,1,'2024-12-08 17:26:19','2024-12-08 18:14:28'),(37,'Emotional Intelligence and Relationships','Understand and improve your emotional intelligence to build stronger personal and professional relationships',4,89.21,67,'/uploads/products/Emotional-Intelligence-and-Academi.jpg',0,89.21,NULL,NULL,NULL,1,'2024-12-08 17:26:43','2024-12-08 18:14:09'),(38,'Stress Management Techniques','Learn proven methods to manage and reduce stress, promoting mental and physical well-being',4,79.99,34,'/uploads/products/images.jpg',0,79.99,NULL,NULL,NULL,1,'2024-12-08 17:27:08','2024-12-08 18:18:35'),(39,'Financial Literacy for Beginners','Gain a strong foundation in personal finance, budgeting, and investment strategies',4,82.57,44,'/uploads/products/Financial-Literacy-Education.jpg',0,82.57,NULL,NULL,NULL,1,'2024-12-08 17:27:34','2024-12-08 18:13:52'),(40,'Self-Discipline and Habits for Success','Develop self-discipline and productive habits to succeed in all aspects of life',4,56.84,28,'/uploads/products/61ZaycMFs1L._AC_UF1000,1000_QL80_.jpg',0,56.84,NULL,NULL,NULL,1,'2024-12-08 17:28:02','2024-12-08 18:13:43'),(41,'Learn Spanish for Beginners','Start your journey to learn Spanish with this comprehensive beginner\'s course. Improve your vocabulary, grammar, and speaking skills',5,79.99,15,'/uploads/products/learn-spanish-for-beginners-course-1.jpg',5,75.9905,NULL,NULL,NULL,1,'2024-12-08 17:30:33','2024-12-08 17:55:51'),(42,'Advanced French Conversation','Master French conversation skills with this advanced-level course. Expand your vocabulary and fluency for real-world conversations',5,75.24,43,'/uploads/products/FrenchClassImageWEB.jpg',0,75.24,NULL,NULL,NULL,1,'2024-12-08 17:31:10','2024-12-08 17:55:40'),(43,'English Grammar Essentials','Learn the fundamental grammar rules of English to speak and write with confidence and accuracy',5,58.02,52,'/uploads/products/eng.jpg',0,58.02,NULL,NULL,NULL,1,'2024-12-08 17:31:44','2024-12-08 17:55:27'),(44,'Mandarin Chinese for Beginners','Start learning Mandarin Chinese with this beginner-friendly course that covers essential phrases, pronunciation, and grammar',5,99.75,31,'/uploads/products/chin.jpg',0,99.75,NULL,NULL,NULL,1,'2024-12-08 17:33:12','2024-12-08 17:55:17'),(45,'Japanese for Beginners','Learn the basics of Japanese, including writing systems, vocabulary, and essential grammar for everyday conversation',5,67.82,36,'/uploads/products/japanese-beginners-learn-language-your-ownr-own-time-lessons-courses-classes-group-individual-conversation-teacher-241084630.jpg',0,67.82,NULL,NULL,NULL,1,'2024-12-08 17:33:33','2024-12-08 17:54:55'),(46,'Italian for Travelers','This course teaches you practical Italian phrases and expressions that will help you during your travels in Italy',5,64.56,2,'/uploads/products/Learn-Italian-1-DP.jpg',0,64.56,NULL,NULL,NULL,1,'2024-12-08 17:34:07','2024-12-08 17:54:43'),(47,'German Language Basics','Master the fundamentals of the German language, including pronunciation, vocabulary, and basic conversational skills',5,25.78,29,'/uploads/products/ger.jpg',0,25.78,NULL,NULL,NULL,1,'2024-12-08 17:34:30','2024-12-08 17:54:34'),(48,'Arabic for Beginners','Begin your Arabic learning journey with essential phrases, pronunciation, and grammar to help you communicate effectively',5,99.99,45,'/uploads/products/arabic.jpg',0,99.99,NULL,NULL,NULL,1,'2024-12-08 17:34:59','2024-12-08 17:54:25'),(49,'Russian Language Fundamentals','Learn the basics of the Russian language, including the alphabet, common phrases, and essential grammar',5,36.97,27,'/uploads/products/russisches-sprachstudio-jetzt-neu-beim-bahnhof-stadelhofen.jpg',0,36.97,NULL,NULL,NULL,1,'2024-12-08 17:35:28','2024-12-08 17:54:05'),(50,'Portuguese for Beginners','Get started with learning Portuguese through an introductory course that covers basic grammar, vocabulary, and useful phrases',5,80.99,61,'/uploads/products/por.jpg',0,80.99,NULL,NULL,NULL,1,'2024-12-08 17:35:53','2024-12-08 17:53:33');
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
  CONSTRAINT `review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `review_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,1,1,5,'Great product!','2024-12-08 16:38:34');
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
  `email` varchar(255) NOT NULL,
  `address` text,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `pin_code` varchar(10) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `profile_image` varchar(255) DEFAULT NULL,
  `role` enum('ROLE_USER','ROLE_CONSULTANT','ROLE_ADMIN') DEFAULT 'ROLE_USER',
  `is_enable` tinyint(1) DEFAULT '1',
  `account_status_non_locked` tinyint(1) DEFAULT '1',
  `account_failed_attempt_count` int DEFAULT '0',
  `account_lock_time` datetime DEFAULT NULL,
  `reset_tokens` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'TEST','0876134948','test@example.com','Address 1','City1','State1','12345','password',NULL,'ROLE_USER',1,1,0,NULL,NULL,'2024-12-08 16:38:34','2024-12-08 16:38:34'),(2,'Nguyen Sy The Anh','111','theanh@gmail.com','429','Thủ Đức','Thành phố Hồ Chí Minh — Ho Chi Minh City','71207','$2a$10$4f7f9.a6GHEeTm4bvXJ/eO7ARXQLC32m8trPQTg285PSe2tqNiW9S','default.jpg','ROLE_ADMIN',1,1,0,NULL,NULL,'2024-12-08 16:39:49','2024-12-08 16:45:19'),(3,'Nguyen Sy The Anh','111','theanh1@gmail.com','429','Thủ Đức','Thành phố Hồ Chí Minh — Ho Chi Minh City','71207','$2a$10$JqdRu60yAmXrJpAafaljcOLBEMop8lWnWMK1wbOQk/2jZZslmUyGq','default.jpg','ROLE_USER',1,1,0,NULL,NULL,'2024-12-08 16:40:03','2024-12-08 16:40:03');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-09  2:35:48
