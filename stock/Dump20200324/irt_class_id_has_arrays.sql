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
-- Table structure for table `class_id_has_arrays`
--

DROP TABLE IF EXISTS `class_id_has_arrays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_id_has_arrays` (
  `class_id` int(10) unsigned NOT NULL,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`class_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `class_id_has_arrays`
--

LOCK TABLES `class_id_has_arrays` WRITE;
/*!40000 ALTER TABLE `class_id_has_arrays` DISABLE KEYS */;
INSERT INTO `class_id_has_arrays` VALUES (61,'1'),(49,'11'),(3,'15'),(60,'2'),(2,'71'),(1,'92b18dfa'),(4,'92b4325a'),(8,'92b43338'),(10,'92b433f1'),(12,'92b43469'),(14,'92b43529'),(17,'92b43589'),(20,'92b435e5'),(21,'92b4368b'),(23,'92b436e8'),(24,'92b4373e'),(25,'92b4379a'),(28,'92b437f0'),(30,'92b4384d'),(32,'92b438a7'),(34,'92b43900'),(35,'92b43956'),(37,'92b439af'),(39,'92b43a09'),(41,'92b43a62'),(42,'92b43abc'),(45,'92b43b15'),(46,'92b43b6b'),(47,'92b43bc4'),(48,'92b43c1e'),(51,'92b43c77'),(54,'92b43cd7'),(55,'92b43d34'),(56,'92b43d8d'),(57,'92b43df1'),(58,'92b43df2'),(59,'92b43df3'),(0,'unused');
/*!40000 ALTER TABLE `class_id_has_arrays` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:15:59
