-- MySQL dump 10.13  Distrib 8.0.17, for Win64 (x86_64)
--
-- Host: 192.168.2.241    Database: irt
-- ------------------------------------------------------
-- Server version	5.5.30

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
-- Table structure for table `sequential_number`
--

DROP TABLE IF EXISTS `sequential_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sequential_number` (
  `class_id` int(10) unsigned NOT NULL,
  `start` int(10) unsigned NOT NULL COMMENT 'Sequential number start position',
  `size` int(10) unsigned NOT NULL COMMENT 'Sequential numbe length',
  PRIMARY KEY (`class_id`),
  KEY `fk_sequential_namber_class_id_has_arrays1_idx` (`class_id`),
  CONSTRAINT `fk_sequential_namber_class_id_has_arrays1` FOREIGN KEY (`class_id`) REFERENCES `class_id_has_arrays` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sequential_number`
--

LOCK TABLES `sequential_number` WRITE;
/*!40000 ALTER TABLE `sequential_number` DISABLE KEYS */;
INSERT INTO `sequential_number` VALUES (1,0,0),(2,5,4),(4,0,0),(8,0,0),(10,0,0),(12,0,0),(14,0,0),(17,0,0),(20,0,0),(21,0,0),(23,0,0),(24,0,0),(25,0,0),(28,0,0),(30,0,0),(32,0,0),(34,0,0),(35,0,0),(37,0,0),(39,0,0),(41,0,0),(42,0,0),(45,0,0),(46,0,0),(47,0,0),(48,0,0),(49,13,3),(51,0,0),(54,0,0),(55,0,0),(56,0,0),(57,0,0);
/*!40000 ALTER TABLE `sequential_number` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:16:04
