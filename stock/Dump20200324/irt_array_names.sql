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
-- Table structure for table `array_names`
--

DROP TABLE IF EXISTS `array_names`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `array_names` (
  `id_array_names` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `array_name` varchar(20) NOT NULL,
  PRIMARY KEY (`id_array_names`),
  UNIQUE KEY `array_name_UNIQUE` (`array_name`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `array_names`
--

LOCK TABLES `array_names` WRITE;
/*!40000 ALTER TABLE `array_names` DISABLE KEYS */;
INSERT INTO `array_names` VALUES (71,'ampl_titles'),(1,'assemblied'),(2,'assem_titles'),(73,'assy_titles'),(3,'band'),(4,'band_buc'),(5,'band_dc'),(6,'band_uc'),(7,'board_mat'),(8,'board_title'),(9,'board_type'),(10,'bom'),(11,'cable_titles'),(12,'cable_type'),(13,'cab_con_type'),(14,'cap_mounting'),(15,'cap_titles'),(16,'cap_type'),(17,'comp_titles'),(72,'con_titles'),(18,'con_type'),(19,'cost_class'),(20,'details'),(21,'diode_type'),(22,'fet_package'),(23,'fet_titles'),(24,'fet_type'),(25,'from_to'),(26,'gasket_type'),(27,'gskt_titles'),(28,'history_oper'),(29,'ic_package'),(30,'ic_titles'),(31,'isol_package'),(32,'isol_titles'),(33,'isol_type'),(34,'metal_f1'),(35,'metal_f2'),(36,'metal_titles'),(70,'mfr'),(37,'M_F'),(38,'pcb_as_title'),(39,'plastic_titles'),(40,'plastic_titles0'),(41,'plastic_titles1'),(42,'plastic_titles2'),(43,'plastic_titles3'),(44,'plastic_titles4'),(45,'po_page'),(46,'precision'),(47,'prec_res'),(48,'ps_input'),(49,'ps_outputs'),(50,'ps_out_v'),(51,'ps_package'),(52,'ps_type'),(53,'resistor_titles'),(54,'rm_wire_titles'),(55,'scr_number'),(56,'scr_titles'),(57,'shaw_exel'),(58,'shipping'),(59,'size'),(60,'size_res'),(61,'tax'),(62,'title'),(63,'titles'),(64,'top_config'),(65,'top_device'),(66,'top_pack'),(67,'top_titles'),(68,'tr_type'),(69,'wire_titles');
/*!40000 ALTER TABLE `array_names` ENABLE KEYS */;
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
