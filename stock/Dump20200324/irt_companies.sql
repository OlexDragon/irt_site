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
-- Table structure for table `companies`
--

DROP TABLE IF EXISTS `companies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `company` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `e-mail` varchar(45) NOT NULL,
  `type` tinyint(4) unsigned NOT NULL COMMENT '1 - Stock\n4 - CO Manufacture\n5 - Vandor',
  `status` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY (`company`,`name`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=115 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies`
--

LOCK TABLES `companies` WRITE;
/*!40000 ALTER TABLE `companies` DISABLE KEYS */;
INSERT INTO `companies` VALUES (54,'3Gmetalworx Inc.',' ',' ',5,1),(58,'ABRACOn',' ',' ',5,1),(35,'AK Machining Inc.',' ','mahesh739@msn.com',5,1),(61,'ALLIED',' ',' ',5,1),(91,'ALTITEC ELECTRONICS INC','null','null',5,1),(36,'America II Electronics Inc.',' ',' ',5,1),(50,'America II Inc.',' ',' ',5,0),(33,'APOLLO Microwaves LTD.',' ',' ',5,1),(26,'ARROW ELECTRONICS',' ',' ',5,1),(114,'ASB','null','null',5,1),(18,'AST',' ',' ',5,1),(3,'Atelier D\'Usinage Y. Boucher',' ',' ',5,1),(2,'Avnet','Jason Helik','jason.helik@avnet.com',5,1),(48,'BIP Corporation',' ',' ',5,1),(108,'BLILEY','Brandon Lewis','SZimny@bliley.com',5,1),(112,'BLILEY','null','SZimny@bliley.com',5,0),(29,'CMR CIRCUITS LTD.',' ',' ',5,1),(24,'COILCRAFT INC.',' ',' ',5,1),(19,'Component Distributors Inc.',' ','ar@cdiweb.com',5,1),(110,'DC Stock','null','null',5,1),(104,'DENZA','null','null',5,1),(66,'DHILLON MASHINE SHOP',' ',' ',5,1),(105,'DIAMEX','null','null',5,1),(30,'DIAMOND ADVANCED COMPONENTS',' ',' ',5,1),(4,'Digi-Key','online','webmaster@digikey.com',5,1),(76,'DIVERSE Electronics','null','null',5,1),(95,'ECS Inc.International','null','null',5,1),(43,'Emerson&Cuming Microwave',' ',' ',5,1),(31,'ETG Canada Inc.',' ',' ',5,1),(74,'FARNELL','null','null',5,1),(32,'Fastbolt','Bruce Leibner','leibnerb@fastboltcorp.com',5,1),(13,'FLUIDE DYNAMIQUE','DAVID ARONOVITCH',' ',5,1),(6,'Future electronics','Raphael','Raphael.Badaoui@Future.ca',5,1),(96,'GALAXY','null','null',5,1),(68,'GFI',' ',' ',5,1),(99,'Global Electrical Testing','Peter Yan','peter@gets-china.com',5,1),(75,'HEILIND','KERRY-ANN WRIGHT','kwright@heilind.com',5,1),(10,'Hittite Microwave International LTD','Jeffry Jack',' ',5,1),(98,'ICAPE','Nancy Zhang','nancy.zhang@icape.cn',5,1),(87,'ICC','null','cemsales@icc107.com',5,1),(38,'ICONIX INC.',' ',' ',5,1),(56,'IKtech Corporation',' ',' ',5,1),(7,'IMS','Mary Reando','mary.reando@rdassociates.ca',5,1),(39,'INDIUM Corporation',' ','webbiz@indium.com',5,1),(97,'IQD Frequency Products','null','null',5,1),(0,'IRT ','Amir','amir@irttechnologies.com',5,1),(84,'IRT Assembled','Assembled','elena@irttechnologies.com',3,1),(83,'IRT Bulk','bulk','yelena@irttechnologies.com',7,1),(82,'IRT Stock','Elena','elena@irttechnologies.com',1,1),(102,'ISOTEC Corporation','null','null',5,1),(28,'ITL CIRCUITS',' ',' ',5,1),(46,'J.B. Techology',' ',' ',5,1),(77,'KK Technologies','null','null',5,1),(70,'KLFLO',' ',' ',5,1),(86,'Kynix','null','sales@kynix.com',5,1),(73,'LCS Images','null','jantonio@videotron.ca',5,1),(23,'LP TECHNOLOGIES INC.',' ','lptechnologie@bellnet.ca',5,1),(41,'LUMAR Electronique Inc.',' ',' ',5,1),(40,'M.E.A..Tec','Mr.Lee',' ',4,1),(113,'M.E.A.Tec','null','null',5,1),(12,'M2 Global Technology','Mark Ramirez','customerservice@m2global.com',5,1),(15,'McMASTER-CARR',' ','cle.sales@mcmaster.com',5,1),(100,'MEAN WELL USA, INC.','null','info@meanwellusa.com',5,1),(106,'METALEDGE','null','null',5,1),(17,'Mini-Circuits','Pierre Tournay','pierret@aei.ca',5,1),(51,'Mitacor Industrie Inc.',' ',' ',5,1),(27,'MITSUBISHI',' ',' ',5,1),(5,'Mouser Electronics','online','canadasales@mouser.com',5,1),(71,'Newark',' ',' ',5,1),(14,'NORITAKE',' ',' ',5,1),(88,'Norsat','Sam Fasullo','null',5,1),(8,'Nu Horizons','Ana Guiomar','ana.guiomar@nuhorizons.com',5,0),(37,'NU HORIZONS electronics',' ',' ',5,1),(55,'Online components',' ',' ',5,1),(90,'PEI-Genesis','null','null',5,1),(92,'Quest componentd','null','null',5,1),(42,'RESIN SYSREM CORPORATION',' ',' ',5,1),(93,'RFMW LTD','null','AR@rfmw.com',5,1),(9,'Richardson','Christine Saik','csaik@richardsonrfpd.com',5,1),(85,'Rochester Electronics','Vendor','sales@rocelec.com',5,1),(89,'RT Technologies','null','null',5,1),(67,'SAGER',' ',' ',5,1),(11,'SAMTEC',' ',' ',5,1),(16,'SAS Indastries, Inc.',' ',' ',5,1),(72,'Sealing devices',' ',' ',5,1),(57,'SECTORMICROWAVE',' ',' ',5,1),(52,'Shenzhen Strongd model',' ',' ',5,1),(53,'Shin-Etsu Silicones',' ',' ',5,1),(101,'SIERRA IC Inc.','Joseph Satalino','null',5,1),(1,'SMT','Michael Simpson','michael@smttechnology.com',4,1),(69,'SMT-ASSY',' ',' ',5,1),(59,'SMT-ASSY','VEZIO','vsacratini@smt-assy.com',4,0),(62,'SourceRF',' ',' ',5,1),(44,'Space Machine & Engineering',' ',' ',5,1),(78,'SPACEPATH COMMUNICATIONS','null','SALES@SPACE-PATH.COM',5,1),(94,'STEALTH','null','null',5,1),(22,'TDK LAMBDA','Adam ALATIF',' ',5,1),(45,'Thorndike Corporetion',' ',' ',5,1),(79,'TOSHIBA America','null','null',5,1),(20,'TTI, INC.',' ',' ',5,1),(21,'TTI, INC. CONNECTORS',' ',' ',5,1),(49,'TTM Tec.',' ',' ',5,1),(111,'UNITRON','null','null',5,1),(25,'USINAGE D.D.R. INC.',' ',' ',5,1),(103,'USINAGE DENZA','null','null',5,0),(64,'VECTRON',' ',' ',5,1),(81,'VERICAL','null','support@verical.com',5,1),(47,'VYRIAN',' ','sales@vyrian.com',5,1),(34,'WERNER',' ',' ',5,1),(65,'Wurth Elektronik','Marios Lteif',' ',5,1),(60,'Xelsat',' ',' ',5,1),(109,'XR technology','null','null',5,1),(107,'YOUTHEN','null','null',5,1);
/*!40000 ALTER TABLE `companies` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:15:57
