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
-- Table structure for table `companies_fax`
--

DROP TABLE IF EXISTS `companies_fax`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies_fax` (
  `id_companies` int(10) unsigned NOT NULL,
  `fax` varchar(45) NOT NULL,
  PRIMARY KEY (`id_companies`,`fax`),
  CONSTRAINT `fk_companies_fax_companies1` FOREIGN KEY (`id_companies`) REFERENCES `companies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies_fax`
--

LOCK TABLES `companies_fax` WRITE;
/*!40000 ALTER TABLE `companies_fax` DISABLE KEYS */;
INSERT INTO `companies_fax` VALUES (4,'2186813380'),(5,'18178043898'),(6,'(514) 457-4847'),(7,'4016835571'),(11,'8129485047'),(13,'5143334642'),(14,'8475932285'),(16,'6317271387'),(17,'7189347092'),(18,'6133845026'),(19,'7202940998'),(23,'4506616560'),(25,'5143354358'),(27,'4087272689'),(28,'9054755097'),(30,'(781) 751-9066'),(32,'5146858439'),(34,'5146941933'),(37,'5761191190'),(38,'5165131421'),(39,'3158531000'),(41,'5146338347'),(42,'6036734512'),(43,'7819612845'),(44,'7273232376'),(45,'5083781529'),(46,'5142880382'),(48,'8665106562'),(53,'3306309855'),(55,'7024627362'),(57,'6312428158'),(58,'9495468001'),(59,'5144288894'),(64,'6035980075'),(65,'8006432661'),(68,'5146304849'),(73,'5146945899'),(74,'3447111112'),(75,'9057521273'),(79,'9494622200'),(85,'9784629512'),(88,'6048212801'),(90,'2155228041'),(92,'6263335668'),(93,'4084141461'),(97,'0146072578'),(100,'5106838899'),(101,'7049195496'),(108,'8148332712');
/*!40000 ALTER TABLE `companies_fax` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:16:00