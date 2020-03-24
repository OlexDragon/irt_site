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
-- Table structure for table `companies_address`
--

DROP TABLE IF EXISTS `companies_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies_address` (
  `id_companies` int(11) unsigned NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id_companies`,`address`),
  CONSTRAINT `fk_companies_address_companies1` FOREIGN KEY (`id_companies`) REFERENCES `companies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies_address`
--

LOCK TABLES `companies_address` WRITE;
/*!40000 ALTER TABLE `companies_address` DISABLE KEYS */;
INSERT INTO `companies_address` VALUES (3,'888 Rte Cite des Jeunes, Local B\nSaint Lazare, Quebec, Canada\nJ7T 2B5'),(4,'701 Brooks Avenue South,\nThief River Falls,\nMN 56701 USA'),(5,'Mouser Electronics\n1000 North Main Street\nMansfield, TX 76063\nUSA'),(6,'237, boul. Hymus\r\nPointe-Claire QC H9R5C7\r\n'),(7,'International Manufacturing Services (IMS)\r\n50 Schoolhouse Ln.\r\nPortsmouth, RI 02871'),(10,'20 ALPHA RD CHELMSFORD, MA 01824 US'),(11,'520 PARK EAST BLVD. P.O. BOX1147\r\nNEW ALBANY, IN 47151-1147'),(12,'5714 Epsilon\r\nSam Antonio\r\nTX 78349\r\nUSA'),(13,'863 Montee de Liesse St-Laurent,Quebec H4T1P5 Canada'),(14,'2635 CLEARBROOK DR.ARLINGTON HEIGHTS, IL 60005'),(15,'200 AURORA INDUSTRIAL PKWY AURORA OH 44202-8087'),(16,'939 Wading River Manor Rd.\r\nManorville NY 11949 US\r\n'),(17,'13 Neptune avenue Brooklyn, New York\r\n11235-0003 USA'),(18,'Advanced Switch Technology\r\n754 Fortune Crescent\r\nKingston, On K7P2T3 Canada'),(19,'P/O. Box 13017 Denver,CO \r\n80201-3017 USA'),(20,'2441 Northeast PKWY\r\nFT.Worth  76106-1896\r\nUSA'),(21,'2601 Sylvania Cross\r\nFT.WORTH TX 76137\r\nUSA'),(22,'ADAM ALATIF\r\nCSC MONTREAL\r\n6600 TRANS CANADA HWY SUITE 120 UNIT 15\r\nPOINTE-CLAIR QC H9R 4S2\r\nCANADA'),(23,'4747 Boul.Lite Laval, Quebec, H7C1A7'),(24,'COILCRAFT INC. 222 AVENUE E HAWARDEN\r\nIA 51023-00 USA'),(25,'1274 Montee de Liesse\r\nSt-Laurent, QC H4S1J4'),(26,'665 MAESTRO DRIVE\r\nRENO NV, 89511-2206'),(27,'c/o MENLO WORLDWIDE LOGISTICS\r\n6120 STEWART AVE FREMONT, CALIFORNIA\r\n94538'),(28,'90 DON PARK ROAD MARKHAM,\r\nON, L3R 1C4 CANADA'),(29,'850 SELKIRK POINTE-CLAIRE, \r\nQC H9R 3S3 CANADA'),(30,'450 WASHINGTON ST STE308\r\nDEDHAM MA 02026 \r\nUSA'),(31,'438 Aime-Vincent, Vaudreuil-Dorion\r\nQc, Canada J7V 5V5'),(32,'1980 Hymus Boul.\r\nDorval, QC H9P 1J7'),(33,'1650 Rte Transcanadienne Dorval\r\nQC H9P 1H7 Canada'),(34,'149 ALSTON, POINTE-CLAIRE QC,\r\nH9R 5V8'),(35,'2600 Brabant-Marineau St.Laurent, \r\nQC, H4S 1L1\r\nCanada'),(36,'11701 28th Street \r\nNorth St.Petersburg FL 33716\r\nUSA'),(37,'51ACALDARI ROAD-UNIT 1B\r\nVAUGHAN, ONTARIO CANADA L4K 4G3'),(38,'315 W. ROSLYN ROAD\r\nMINEOLA, NY 11501'),(39,'BOX 347268\r\nPITTSBURGH, PA 15251-4268'),(41,'492 avenue Lepine,\r\nDorval H9P 2V6\r\nCanada'),(42,'62 ROUTE 101-A AMHERST, NH 03031-2295'),(43,'62722 Collections Center DR.\r\nChicago, IL 60693-0627\r\nUSA'),(44,'2327 16th Avenue North\r\nSaint Petersburg, FL 33713'),(45,'680 North Bedford Street\r\nEast Bridgewater, MA 02333'),(46,'4174 Rivard MONTREAL,QC\r\nH2L 4H9'),(47,'9894 Bissonnet St., Suite 488\r\nHouston, TX 77036'),(48,'2951 NORMAN Strasse Rd.,San Marcos,\r\n CA 92069\r\n'),(49,'8150 Sheppard Ave.E\r\nToronto, ON M1B5K2 '),(50,'11701 28th Street North\r\nSt.Petersburg FL 33716\r\nUSA'),(51,'189 Ave Labrosse Suite#300\r\nPointe-Claire H9R 1A3'),(52,'Building B, Yilai Industrial District, Bai Mang, Xili Town, NanShan, ShenZhen, China\r\nTel. 0755-23708312\r\nFax. 0755-29810720'),(53,'1150 Damar Drive, Akron OH 44305'),(54,'90 Snow boulevard CONCORD ON L4K4A2\r\nCANADA'),(55,'11125 S.Eastern Ave. Ste 120,\r\nHenderson, NV 89052'),(56,'3125 Scott St. Vista,\r\nCA 92081, USA'),(57,'999 GRAND BLVD.\r\nDEER PARK N.Y. 11729'),(58,'5101 Hidden Creek Lane, Spicewood\r\nTX 78669 USA'),(59,'125 Hymus Blvd. Pointe-Claire\r\nH9R 1E6 CANADA'),(60,'9845 Shoup Ave\r\nChatsworth,\r\nCA 91311'),(61,'7151 Jack Newell Blvd.S \r\nFT WORTH, TX 76118\r\nUSA'),(62,'44 Dragon Kirkland, \r\nQC H9J3B1 Canada'),(64,'267 LOWELL ROAD\r\nHUDSON, NEW HAMPSHISE'),(65,'121 Airport Drive P.O. Box 1330\r\nWater town, SD 57201-6330, USA'),(66,'7675 THIMENS BOUL.ST.LAURENT, \r\nQC H4S 2E5'),(67,'19 LEONA DRIVE \r\nMIDDLEBORO, MA 02346'),(68,'180 AVENUE LABROSSE\r\nPOINTE-CLAIRE, QC H9R 1A1\r\nCANADA'),(72,'4400 Walden Ave,\r\n Lancaster, NY 14086'),(73,'244 Strathcona Ave. POINTE-CLAIRE\r\nQUEBEC, H9R 5Y3'),(74,' \r\nUK\r\n'),(75,'HEILIND ELECTRONICS , INC\r\n7100 Woodbine Ave, Suite 313\r\nMarkham , Ontario L3R5J2\r\nCanada'),(79,'5231 California Avenue\r\nIrvin, CA 92617'),(85,'16 Malcolm Hoyt Drive\r\nNewburyport,\r\nMA 01950\r\nUSA'),(86,'Room 10E Hangdu Building HuaFu Road\r\nN. 1006 Futian Dis. Shenzhen , China. 518031'),(87,'215 McCormick Drive\r\nBohemia NY 111716'),(88,'Norsat Canada\r\n110-4020  Viking Way\r\nRichmond , BC\r\nV6V 2L4, Canada'),(89,'8 Parklane, Dollar-des-Ormeaux\r\nH9G 1B9'),(90,'2180 HORNIG ROAD \r\nPHILADELPHIA PA 19116-4289\r\nUSA'),(91,'7240 Blvd. Maurice Duplessis #102\r\nMontreal, Qc H1E 4A7'),(92,'14711 Clark Avenue, Industry, CA 91745'),(93,'188 Martinvale Lane, \r\nSan Jose,CA,95119-1356'),(94,'Stealth Components Inc.\n15801 Brixham Hill Av., Suite 175\nCharlotte, NC 28277'),(95,'15351 W. 109th St. Lenexa, KS 66219'),(96,'3600 F TESSIER UNIT H VAUDREUIL-DORION \nQC  J7V5V5'),(97,'Station Road, Crewkerne Somerset,\r\nTA18 8AR\r\nUnited Kingdom'),(98,'Chaina Representative Office:\r\n20F, Parkway Center#121,\r\nDong Men Zhong Lu Change An, Dong Guan,\r\nGuang Dong P.R.C.'),(99,'Room 703, 7/F, New Lee Wah Centre 88,\r\nTo Kwa Wan Road,Kowloon\r\nHong Kong'),(100,'44030 Fremont Blvd.\r\nFremont , CA 94538'),(101,'16151 Lancaster HWY Suite E\r\nCharlotte, NC 282773882\r\nUSA\r\n'),(102,'Suit-110, 10 Bamgogae-ro 1 Gil,\r\nGangnam-Gu, Seoul 06349\r\nKorea'),(108,'2545 West Grandview, BLVD, Erie, PA\r\nUSA'),(109,'No.4 Xinye Road, Gaoxin Disctrict, Chengdu City, Sichuan Province,China\r\nPostal code 610000'),(110,'650 N Rose Driver Unit 213,\r\nPlacentia, CA 92870'),(112,'2545 West Grondview Blvd.\r\nErie,PA 16506');
/*!40000 ALTER TABLE `companies_address` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:15:58
