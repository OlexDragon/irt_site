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
-- Table structure for table `html_options`
--

DROP TABLE IF EXISTS `html_options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `html_options` (
  `class_id` int(10) unsigned NOT NULL,
  `array_sequence` tinyint(4) NOT NULL,
  `array_name` varchar(20) NOT NULL,
  `position` int(10) unsigned DEFAULT NULL COMMENT 'Start Position in the part number without dashes.',
  `size` int(10) unsigned DEFAULT NULL,
  `html_input_max_length` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`class_id`,`array_sequence`,`array_name`),
  CONSTRAINT `fk_htm_options_class_id_has_arrays1` FOREIGN KEY (`class_id`) REFERENCES `class_id_has_arrays` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `html_options`
--

LOCK TABLES `html_options` WRITE;
/*!40000 ALTER TABLE `html_options` DISABLE KEYS */;
INSERT INTO `html_options` VALUES (0,0,'Description',0,NULL,50),(0,0,'Leads Number',0,NULL,3),(0,0,'Mfr P/N',0,NULL,35),(2,2,'mfr',3,2,NULL),(2,3,'ic_package',9,2,NULL),(2,4,'leads_number',11,3,NULL),(49,1,'cab_con_type',3,2,NULL),(49,2,'cab_con_type',5,2,NULL),(49,3,'cable_type',7,2,NULL),(49,4,'Length(cm)',9,4,10),(49,7,'mfr',3,2,NULL);
/*!40000 ALTER TABLE `html_options` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:16:06
