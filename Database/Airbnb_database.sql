CREATE DATABASE  IF NOT EXISTS `airbnbdatabase` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `airbnbdatabase`;
-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: airbnbdatabase
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amenities`
--

DROP TABLE IF EXISTS `amenities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `amenities` (
  `amenity_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`amenity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amenities`
--

LOCK TABLES `amenities` WRITE;
/*!40000 ALTER TABLE `amenities` DISABLE KEYS */;
/*!40000 ALTER TABLE `amenities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bookings` (
  `booking_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `fk_Bookings_Users1_idx` (`user_id`),
  KEY `fk_Bookings_Rooms1_idx` (`room_id`),
  CONSTRAINT `fk_Bookings_Rooms1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `fk_Bookings_Users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cities`
--

DROP TABLE IF EXISTS `cities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `cities` (
  `city_id` int(11) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cities`
--

LOCK TABLES `cities` WRITE;
/*!40000 ALTER TABLE `cities` DISABLE KEYS */;
/*!40000 ALTER TABLE `cities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favourites`
--

DROP TABLE IF EXISTS `favourites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `favourites` (
  `rooms_room_id` int(11) NOT NULL,
  `users_user_id` int(11) NOT NULL,
  KEY `fk_table1_rooms1_idx` (`rooms_room_id`),
  KEY `fk_table1_users1_idx` (`users_user_id`),
  CONSTRAINT `fk_table1_rooms1` FOREIGN KEY (`rooms_room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `fk_table1_users1` FOREIGN KEY (`users_user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favourites`
--

LOCK TABLES `favourites` WRITE;
/*!40000 ALTER TABLE `favourites` DISABLE KEYS */;
/*!40000 ALTER TABLE `favourites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `review` (
  `review_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `text` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  ` stars` int(11) NOT NULL,
  PRIMARY KEY (`review_id`),
  KEY `fk_Review_Users1_idx` (`user_id`),
  KEY `fk_Review_Rooms1_idx` (`room_id`),
  CONSTRAINT `fk_Review_Rooms1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `fk_Review_Users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `rooms` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(45) NOT NULL,
  `guests` int(11) DEFAULT NULL,
  `bedrooms` int(11) DEFAULT NULL,
  `beds` int(11) DEFAULT NULL,
  `baths` int(11) DEFAULT NULL,
  `photos` varchar(45) DEFAULT NULL,
  `price` varchar(45) NOT NULL,
  `details` varchar(100) NOT NULL,
  `city_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`room_id`),
  KEY `fk_Rooms_Cities1_idx` (`city_id`),
  KEY `fk_Rooms_Users1_idx` (`user_id`),
  CONSTRAINT `fk_Rooms_Cities1` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`),
  CONSTRAINT `fk_Rooms_Users1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roomsamenities`
--

DROP TABLE IF EXISTS `roomsamenities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `roomsamenities` (
  `amenity_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  KEY `fk_RoomsAmenities_Amenities1_idx` (`amenity_id`),
  KEY `fk_RoomsAmenities_Rooms1_idx` (`room_id`),
  CONSTRAINT `fk_RoomsAmenities_Amenities1` FOREIGN KEY (`amenity_id`) REFERENCES `amenities` (`amenity_id`),
  CONSTRAINT `fk_RoomsAmenities_Rooms1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomsamenities`
--

LOCK TABLES `roomsamenities` WRITE;
/*!40000 ALTER TABLE `roomsamenities` DISABLE KEYS */;
/*!40000 ALTER TABLE `roomsamenities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `pasword` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `birth_date` date DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'airbnbdatabase'
--

--
-- Dumping routines for database 'airbnbdatabase'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-25  9:38:12
