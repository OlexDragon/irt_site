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
-- Table structure for table `second_and_third_digit`
--

DROP TABLE IF EXISTS `second_and_third_digit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `second_and_third_digit` (
  `id` char(2) NOT NULL,
  `id_first_digits` int(11) unsigned NOT NULL,
  `description` varchar(45) NOT NULL,
  `class_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`,`id_first_digits`),
  UNIQUE KEY `class_id_UNIQUE` (`class_id`),
  KEY `fk_second_and_third_digit_first_digits1_idx` (`id_first_digits`),
  CONSTRAINT `fk_second_and_third_digit_class_id_has_arrays1` FOREIGN KEY (`class_id`) REFERENCES `class_id_has_arrays` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_second_and_third_digit_first_digits1` FOREIGN KEY (`id_first_digits`) REFERENCES `first_digits` (`id_first_digits`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `second_and_third_digit`
--

LOCK TABLES `second_and_third_digit` WRITE;
/*!40000 ALTER TABLE `second_and_third_digit` DISABLE KEYS */;
INSERT INTO `second_and_third_digit` VALUES ('00',1,'Other',1),('0A',1,'Amplifier',2),('0C',1,'Capacitor',3),('0D',1,'Diode',4),('0F',1,'Fan',5),('0G',1,'Gasket',55),('0I',1,'Inductor',6),('0R',1,'Resistor',7),('0T',1,'Transistors',8),('BR',3,'Bracket',10),('CA',2,'Carrier ASSY',11),('CA',3,'Carrier',12),('CA',4,'PCB Assembled',13),('CB',1,'Cables',49),('CB',2,'Converter-bias ASSY',58),('CB',4,'PCB',14),('CL',4,'PCB with Bootloader',16),('CO',1,'Connector',17),('CO',2,'Cover ASSY',53),('CR',6,'Screw',45),('CS',4,'PCB with Bootl. and Soft.',18),('CV',3,'Cover',20),('DB',3,'Device Block',21),('EN',2,'Enclosure ASSY',22),('EN',3,'Enclosure',23),('FC',7,'Converter',9),('FL',2,'FILTER ASSY',59),('GB',4,'PCB Gerber',44),('HE',3,'Heating',24),('IC',1,'IC(non-RF)',25),('IS',1,'Isolator/Circulator',26),('KT',2,'KIT ASSY ',27),('MC',1,'Microcontroller',28),('NT',6,'Nut',51),('OT',6,'Other',54),('PA',7,'SSPA',29),('PB',7,'SSPB',43),('PP',1,'Plastic Parts',56),('PR',6,'Spacer',47),('PS',1,'Power Supply',30),('PS',2,'POWER SUPPLY ASSY',60),('RF',1,'Power FET(RF)',31),('RF',2,'RF ASSY',61),('RQ',1,'IC',32),('RS',7,'Redundant System',33),('RT',4,'PCB Project',50),('SB',3,'Sheet Metal Bracket',34),('SC',4,'PCB Schematic',36),('SF',3,'Sheet Metal Flat',35),('SR',3,'Sheet Metal Enclosure',37),('SW',2,'SWall ASSY',52),('VC',1,'Varicap',38),('VV',1,'Voltage Regulator',39),('WA',6,'Washer',46),('WG',2,'WG ASSY',40),('WG',3,'Waveguide Parts',41),('WH',1,'Wire Harness',48),('WL',3,'Walls',42),('WR',5,'Wire',57);
/*!40000 ALTER TABLE `second_and_third_digit` ENABLE KEYS */;
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
