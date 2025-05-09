-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: tourism
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
-- Table structure for table `tour_bookings`
--

DROP TABLE IF EXISTS `tour_bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tour_bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  `destination` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `is_confirmed` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `special_request` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tour_bookings`
--

LOCK TABLES `tour_bookings` WRITE;
/*!40000 ALTER TABLE `tour_bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `tour_bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tourism`
--

DROP TABLE IF EXISTS `tourism`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tourism` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) DEFAULT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `packages` text,
  `time_line` text,
  `timeline` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tourism`
--

LOCK TABLES `tourism` WRITE;
/*!40000 ALTER TABLE `tourism` DISABLE KEYS */;
INSERT INTO `tourism` VALUES (32,'Dam Tre Bay is one of the most pristine and tranquil destinations in Con Dao, located in the north of Caon Dao, about 17 km from the town center. It is an ideal place for nature lover and those looking to explore the rich marine ecosystem.',8.74763360857234,106.65596618890392,'Dam Tre Bay',890000,'<ul>\r\n<li><strong>Hotel pick-up and drop-off</strong></li>\r\n<li><strong>English-speaking tour guide</strong></li>\r\n<li><strong>National Park entrance ticket&nbsp;</strong></li>\r\n<li><strong>Snorkeling equipment</strong></li>\r\n<li><strong>Freshwater shower fee</strong></li>\r\n<li><strong>Drinking water, fruits, and a light meal</strong></li>\r\n<li><strong>Life jacket and first aid kt</strong></li>\r\n<li><strong>Travel insurance</strong></li>\r\n<li><strong>GoPro camera to capture moments</strong></li>\r\n</ul>',NULL,'<p dir=\"ltr\"><span style=\"color: #3598db;\"><strong>8:00 AM - Departure</strong></span></p>\r\n<p dir=\"ltr\">The guide picks up guests at the hotel and departs for Vong Beach (Co Ong area).</p>\r\n<p dir=\"ltr\"><span style=\"color: #3598db;\"><strong>8:30 AM - Jungle Trekking</strong></span></p>\r\n<p dir=\"ltr\">Guests walk 1.6km along the beach while the guide introduces small islands along the route.&nbsp;&nbsp;</p>\r\n<p dir=\"ltr\">Continue trekking 3km through the tropical rainforest to reach Dam Tre Bay.</p>\r\n<p dir=\"ltr\"><span style=\"color: #3598db;\"><strong>10:00 AM - Check In Dam Tre Bay</strong></span></p>\r\n<p dir=\"ltr\">Climb to the top of Yen Ngua Mountain for a panoramic view and photo opportunities.&nbsp;&nbsp;</p>\r\n<p dir=\"ltr\">Explore the unique mangrove forest.&nbsp;&nbsp;</p>\r\n<p dir=\"ltr\">Observe swiftlets nesting during the breeding season (January to August).&nbsp;&nbsp;</p>\r\n<p dir=\"ltr\">Go snorkelling to see Giant Clam specimens preserved in the bay.&nbsp;&nbsp;</p>\r\n<p dir=\"ltr\">Try stand-up paddleboarding (SUP) (Self-paid activity).</p>\r\n<p dir=\"ltr\"><span style=\"color: #3598db;\"><strong>1:30 AM - Relaxation Options</strong></span></p>\r\n<p dir=\"ltr\">Option 1: Take a freshwater shower, have lunch at the Forest Ranger Station (surcharge: 300,000 VND/person). Rest and return via the jungle trek at 1:30 PM.&nbsp;&nbsp;</p>\r\n<p dir=\"ltr\">&nbsp;Option 2:Take a freshwater shower, enjoy fresh fruits and a light meal. Rest at the Forest Ranger Station and return at 1:30 PM.</p>\r\n<p dir=\"ltr\"><span style=\"color: #3598db;\"><strong>3:00 PM - End of the Tour</strong></span></p>\r\n<p>&nbsp;</p>'),(35,'So Ray will take you through stunning forest trails, allowing you to enjoy the fresh air and admire the majestic natural scenery from an ideal altitude. It is also home to many rare wildlife species, such as the Con Dao black squirrel, long-talled macaques, and various endemic birds.',8.690804229173487,106.58911382153984,'So Ray',550000,'',NULL,''),(36,'The Elephant Mountain is one of the most captivating destinations in Con Dao. With an elevation of approximately 260 meters, its peak offers a perfect vantage point to admire the panoramic view of Con Dao from above, *immerse yourself in the serene atmosphere, and enjoy the fresh air. Along the trekking route, visitors have the opportunity to explore the diverse tropical rainforest ecosystem and encounter various species of wildlife.',8.691167668290582,106.6261239100938,'The Elephant Moutain',550000,'',NULL,'');
/*!40000 ALTER TABLE `tourism` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tourism_image`
--

DROP TABLE IF EXISTS `tourism_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tourism_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `image_path` varchar(255) DEFAULT NULL,
  `tourism_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK28l5lponne6eupbuvcns1rt5p` (`tourism_id`),
  CONSTRAINT `FK28l5lponne6eupbuvcns1rt5p` FOREIGN KEY (`tourism_id`) REFERENCES `tourism` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tourism_image`
--

LOCK TABLES `tourism_image` WRITE;
/*!40000 ALTER TABLE `tourism_image` DISABLE KEYS */;
INSERT INTO `tourism_image` VALUES (54,'/uploads/images/tourism/1746786078587-Screenshot (12).png',32),(55,'/uploads/images/tourism/1746786078602-Screenshot (13).png',32),(57,'/uploads/images/tourism/1746789359420-Screenshot (4).png',35),(58,'/uploads/images/tourism/1746790107073-Screenshot (16).png',36);
/*!40000 ALTER TABLE `tourism_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_token_expiry` datetime(6) DEFAULT NULL,
  `role` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'duyhvhe180050@fpt.edu.vn','Hoàng Văn Duy','$2a$10$jcGzRIDZon5sPmnqVz4T6uUUcP1HmPZHgm5iVZ9CCgc3Kz5hjSBhC',NULL,NULL,'ADMIN','duyhvhe180050'),(2,'hoangduy20407@gmail.com','Hoàng Văn Duy','$2a$10$8yrwiId6n8SAAt5mFv4Q6O72fUfhnH4Qiwueb5YZId44dspIpQ9pq',NULL,NULL,'USER','hoangvanduy');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-09 18:36:14
