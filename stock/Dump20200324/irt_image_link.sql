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
-- Table structure for table `image_link`
--

DROP TABLE IF EXISTS `image_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_link` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `link` varchar(250) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `link_UNIQUE` (`link`)
) ENGINE=InnoDB AUTO_INCREMENT=850 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_link`
--

LOCK TABLES `image_link` WRITE;
/*!40000 ALTER TABLE `image_link` DISABLE KEYS */;
INSERT INTO `image_link` VALUES (430,'0022232021.png'),(387,'0215012.MXP.png'),(80,'02183.15HXP.png'),(70,'0315008.HXP.png'),(72,'0324025_HXP.png'),(82,'0326020_HXP.png'),(81,'03420006Z.jpg'),(210,'0402(0.6Thickness).png'),(218,'0402-(1005-Metric) -Series.png'),(215,'0402-(1005-Metric).png'),(334,'0402-MLK.jpg'),(427,'0436500215.png'),(83,'0456020_ER.png'),(76,'0458010.DR.jpg'),(402,'0469990101.png'),(399,'0501488000.png'),(367,'0505 (1313 M).jpg'),(848,'0521160410.jpg'),(429,'0533980271.png'),(476,'0533980571.png'),(343,'0603 (1608 Metric) Wirewound.jpg'),(211,'0603 (1608 Metric).png'),(135,'0603(LQG18).png'),(845,'0603-(1608-metric)(1).png'),(216,'0603-(1608-Metric)-C-Series.png'),(214,'0603-(1608-Metric).png'),(390,'0603-(1608-Metric)_res.png'),(326,'0603CS_series_t.jpg'),(333,'0603ct.jpg'),(325,'0603hp.jpg'),(319,'0603HP_series_t.jpg'),(414,'0734152063.png'),(209,'0805 (1.30mm).jpg'),(220,'0805-(2012-Metric)-1_40mm.png'),(311,'0805-(2012-Metric)-1_45mm.png'),(217,'0805-(2012-Metric)-C-Series.png'),(139,'0805-(2012-Metric).png'),(165,'08147.png'),(167,'08149.png'),(166,'08174.jpg'),(418,'0853050232.jpg'),(482,'09454521560.png'),(437,'1-1123722-3.png'),(442,'1-1318300-3.png'),(506,'1-1734530-1.png'),(307,'10-101949-008.png'),(303,'10-101949-010.JPG'),(305,'10-101949-012.png'),(306,'10-101949-014.JPG'),(304,'10-101949-020.JPG'),(445,'10-11-2033.png'),(477,'10-11-2063.png'),(548,'10-MSOP.jpg'),(728,'100 W Ku FAN-SHROUD ASSEMBLY.png'),(738,'100 W KU MAGIC-TEE ASSEMBLY.png'),(727,'100 W Ku POWER SUPPLY ASSEMBLY.png'),(601,'100-LQFP.png'),(150,'1000dx-1-500x500.jpg'),(344,'1008cs.jpg'),(317,'1008CS_series_t.jpg'),(339,'1008hq.jpg'),(507,'10090769-P154ALF.png'),(252,'101X44W404MV4E.jpg'),(412,'1052523-1.JPG'),(408,'1052902-1.png'),(448,'1053081204.JPG'),(468,'1053081206.jpg'),(489,'1053081210.png'),(844,'1053082204.jpg'),(469,'1053082206.jpg'),(433,'1053092xxx.JPG'),(453,'1053101204.png'),(465,'1053101206.png'),(464,'1053102206.JPG'),(454,'1053102304.JPG'),(488,'105310_black_series_SPL.jpg'),(452,'1053141204.JPG'),(475,'1053141206.png'),(494,'1053141210.JPG'),(185,'1054869-1.png'),(186,'1057377-1.png'),(337,'1111sq.jpg'),(400,'1123721-2.png'),(23,'112438.jpg'),(18,'112552.jpg'),(28,'11305-3S.JPG'),(446,'12-04PFFS-SF8001.png'),(689,'12-DFN_05-08-1695 rev D.png'),(202,'12-VFQFN Exposed Pad.png'),(225,'1206-(3216-Metric)-1_80mm.jpg'),(212,'1206-(3216-Metric)-Series.png'),(133,'1206.png'),(226,'1210-(3225-Metric)-2_70mm.png'),(421,'1211.png'),(510,'122568406-40.jpg'),(43,'127-0901-811.jpg'),(44,'127-0901-822.jpg'),(615,'130103_pfe500f_pi0401.png'),(12,'132101.png'),(20,'132107.jpg'),(10,'132111.jpg'),(416,'132134.png'),(411,'132146.png'),(407,'132147.png'),(9,'132168.png'),(25,'132169.png'),(11,'132170.png'),(13,'132171.png'),(8,'132172.png'),(19,'132274.JPG'),(26,'132340.png'),(741,'1327ap1l.png'),(703,'14-QFN Exp Pad.png'),(528,'14-SOIC.png'),(567,'14-TDFN_21-0137I.png'),(531,'14-TSSOP.png'),(413,'142-0711-201.png'),(571,'150_C04-028_CH_OT_6.png'),(471,'15110062601000.png'),(498,'15110122601000.png'),(340,'1515sq.jpg'),(187,'1546307-4.JPG'),(188,'1546307-6.jpg'),(66,'1587-1.JPG'),(566,'16-TQFN Exp Pad 21-0136.png'),(582,'16-TSSOP.png'),(658,'16-VFQFN Exposed Pad.png'),(692,'161 05-08-1663 var BC FE 16.png'),(690,'161-05-08-1662-MS8E-8.png'),(691,'161-05-08-1719-DC-8.png'),(688,'161_05-08-1461_Q;5.jpg'),(693,'161_05-08-1666_MSE_12.png'),(554,'161_05-08-1668_MS_12.png'),(588,'161_05-08-1698_DD_8.png'),(557,'161_05-08-1699_DD_10.png'),(694,'161_05-08-1723_DDB_12.png'),(556,'161_05-08-1725_DD;12.png'),(281,'1655.jpg'),(730,'16W PICOBUC KU-BAND ENCLOSURE ASSEMBLY.png'),(709,'16_40 W KU RF CARR ASSY.png'),(485,'171-009-103L001.png'),(484,'171-009-203L001.png'),(406,'172117.png'),(15,'172138.png'),(16,'172169.jpg'),(561,'175-16-QSOP.png'),(558,'175-28-SSOP.png'),(604,'176-LFBGA.jpg'),(603,'176-LQFP.png'),(435,'1760490000.jpg'),(527,'178-009-513R491.JPG'),(345,'1812cs.jpg'),(331,'1812ps.jpg'),(327,'1812SMS_series_t.jpg'),(439,'1838839-1.jpg'),(751,'1897ap1l.png'),(801,'1U RACKMOUNT-RAIL MOUNTING BRACKET.png'),(401,'2-520272.jpg'),(168,'2.8.jpg'),(725,'20 W KU RUGGEDIZED SHROUD ASSY.png'),(757,'20 W RUG. SPPA RF CARRIER.png'),(817,'20 W RUG. SSPA BOTTOM COVER.png'),(780,'20 W RUG. SSPA ENCLOSURE.png'),(816,'20 W RUG. SSPA P_S TWO 1_4 BRICKS MOUNTING PLATE.png'),(818,'20 W RUG. SSPA RF COVER.png'),(800,'20 W RUG. SSPA SHROUD FAN BAFFLE BRKT.png'),(799,'20 W RUG. SSPA SHROUD FAN BRKT.png'),(834,'20 W RUG. SSPA. WR-75 TRANSITION WITH SAMPLE PORT.png'),(712,'20 W RUGGEDIZED CARRIER ASSY.png'),(724,'20 W RUGGEDIZED POWER SUPPLY.png'),(769,'20 W RUGGIZED SSSP INPUT MODULE COVER.png'),(533,'20-SOIC 0_295.png'),(553,'20-SSOP_05-08-1641.jpg'),(563,'20-SSOP_SOT266-1.png'),(530,'20-TSSOP.png'),(737,'200 W 1-2 REDUNDANCY WAVEGUIDE ASSY.png'),(806,'200 W C-B 3U POWER SUPPLY FRONT PLATE.png'),(788,'200 W C-B 3U PS TERMINAL STRIP BRACKET.png'),(805,'200 W C-B 3U RACKMOUNT FAN PLATE RF.png'),(807,'200 W C-B PS-HEATSINK MOUNTING PLATE.png'),(756,'200 W C-B RACKMOUNT CARRIER NEW.png'),(743,'200 W C-B RACKMOUNT DUCKING BRACKET.png'),(742,'200 W C-B RACKMOUNT HEAT SINK BRACKET.png'),(820,'200 W C-B RACKMOUNT PS ENCLOSURE-BOTTOM PLATE.png'),(821,'200 W C-B RACKMOUNT PS ENCLOSURE-COVER.png'),(819,'200 W C-B RACKMOUNT PS ENCLOSURE-TOP PLATE.png'),(841,'200 W C-B RF CARRIER WALL NEW.png'),(809,'200 W C-BAND ENCLOSURE COVER-NEW.png'),(808,'200 W C-BAND FAN COVER PLATE.png'),(740,'200 W C-BAND MAGIC TEE ASSEMBLY.png'),(764,'200 W C-BAND MAGIC TEE COVER.png'),(829,'200 W C-BAND MAGIC TEE HALF-1.png'),(830,'200 W C-BAND MAGIC TEE HALF-2.png'),(790,'200 W C-BAND MAGIC-TEE BRACKET.png'),(831,'200 W C-BAND MAGIC-TEE DUAL FLANGE.png'),(723,'200 W C-BAND POWER SUPPLY ASSEMBLY.png'),(789,'200 W C-BAND POWER SUPPLY BRACKET.png'),(763,'200 W C-BAND POWER SUPPLY COVER-NEW.png'),(762,'200 W C-BAND RACKMOUNT DUCTING COVER.png'),(825,'200 W C-BAND RACKMOUNT WAVEGUIDE.png'),(784,'200 W PS HEAT TRANSFER BLOCKS ?2.png'),(783,'200 W PS HEAT TRANSFER BLOCKS.png'),(804,'200 W RACKMOUNT 3-U FRONT PANEL ?2.png'),(721,'200 W RACKMOUNT 3-U FRONT PANEL.png'),(711,'200 W RACKMOUNT C-BAND CARRIER ASSY.png'),(733,'200 W RACKMOUNT CARR-WAVEGUIDE ASSEMBLY.png'),(715,'200 W RACKMOUNT CONVERTER-BIAS ASSY.png'),(734,'200 W RACKMOUNT ENCLOSURE ASSEMBLY.png'),(722,'200 W RACKMOUNT FAN ASSEMBLY.png'),(720,'200 W RACKMOUNT POWER SUPPLY.png'),(826,'200 W RACKMOUNT WAVEGUIDE FLANGE W_GROOVE.png'),(827,'200 W WAVEGUIDE CUSTOMER OUTPUT FLANGE.png'),(779,'200 WATT C-BAND ENCLOSURE NEW.png'),(440,'20020327-D031B01LF.jpg'),(447,'20020327-D041B01LF.png'),(810,'200W C-BAND 1-1 REDUNDANCY MOUNTING PLATE.png'),(792,'200W C-BAND REDUNDANCY MOUNTING BRACKET.png'),(791,'200W C-BAND REDUNDANCY SWITCH BRACKET.png'),(748,'200W-C-BAND RACKMOUNT HEATSINK BAFFLE BRACKET.png'),(17,'202102.jpg'),(34,'2027-47-CLF.jpg'),(52,'20DPCG5C.JPG'),(842,'20W KU RUGGEDIZED SSPA SHROUD WALL.png'),(768,'20W RUGGIZED SSSP POWER SUPPLY COVER.png'),(315,'2100_2200_2300 VERTICAL SERIES.png'),(221,'2220-(5750-Metric)-2_70mm-C-Series.png'),(405,'222146.png'),(653,'24-QFN Exposed Pad.png'),(662,'24-TFQFN EP.png'),(654,'24-TFQFN%20Exposed%20Pad.jpg'),(543,'24-TSSOP_SOT355-1.png'),(652,'24-VFQFN Exposed Pad.png'),(27,'242113.png'),(14,'242125.jpg'),(24,'242163.png'),(195,'2463-001-X5S0-471MLF.JPG'),(710,'25 W KU RF CARR ASSY.png'),(761,'25 WATT POWER SUPPLY COVER.png'),(759,'25 WATT RF CARRIER COVER.png'),(839,'25 WATT RF CARRIER WALL BOTTOM.png'),(838,'25 WATT RF CARRIER WALL TOP.png'),(708,'25001-0302.JPG'),(163,'255-4-SOP.png'),(731,'25W PICOBUC KU-BAND ENCLOSURE ASSEMBLY.png'),(392,'261_MKT-TO263A07.png'),(559,'28L-16L Pkg.jpg'),(79,'2920L185DR.png'),(704,'296-SOT-223-5.png'),(698,'296-TZA07A-NDW-7.png'),(574,'296_4040064-3_PW_14.png'),(597,'296_4093553-3_DCK_5.png'),(170,'2SN-BK-G.bmp'),(747,'3-U ROUND GRIP PULL HANDLE.png'),(21,'31-216.png'),(278,'31-SMA.png'),(175,'3101.0115.png'),(201,'32-TFQFN Exposed Pad.png'),(576,'32-TSSOP_SOT487-1 Pkg.jpg'),(680,'32-VFQFN (5x5).png'),(663,'32-VFQFN Exposed Pad.jpg'),(362,'3224W-1-103E.png'),(149,'38711-2203.jpg'),(145,'38720-0204.jpg'),(434,'38720-3202.jpg'),(144,'38720-3204.png'),(148,'38721-6708.png'),(146,'38760-0102.jpg'),(147,'38770-0108.png'),(732,'40-50W PICOBUC KU-BAND ENCLOSURE ASSEMBLY.png'),(659,'40-QFN.png'),(713,'400 W C-BAND RACKMOUNT CARRIER-MAGIC TEE ASSY.png'),(726,'400 W C-BAND RACKMOUNT POWER SUPPLY ASSEMBLY.png'),(745,'400 W KU RED H-V UNIT MOUNTING BRACKET.png'),(797,'400 W WAVEGUIDE SUPPORT BRACKET BOTTOM.png'),(798,'400 W WAVEGUIDE SUPPORT BRACKET TOP.png'),(803,'400 W-KU PDU PLATE REDUNDANCY.png'),(802,'400 W-KU RED H-V CONTROLLER PLATE.png'),(744,'400 W-KU RED H-V SWITCH BRACKET.png'),(793,'400W C RACKMOUNT CONTROLLER BOARD BRKT.png'),(813,'400W C RACKMOUNT REAR FAN PLATE.png'),(796,'400W C RACKMOUNT REAR PANEL.png'),(823,'400W C-BAND RACKMOUNT ENCLOSURE.png'),(815,'400W RACKMOUNT 4U FRONT PANEL.png'),(750,'400W RACKMOUNT BOTTOM HEATSINK BAFFLE BRKT..png'),(766,'400W RACKMOUNT BOTTOM HEATSINK DUCTING COVER.png'),(814,'400W RACKMOUNT ENCLOSURE TOP COVER.png'),(794,'400W RACKMOUNT POWER SUPPLY REAR PANEL.png'),(822,'400W RACKMOUNT PS ENCLOSURE.png'),(812,'400W RACKMOUNT PS FRONT PANEL.png'),(795,'400W RACKMOUNT PS TOP COVER.png'),(749,'400W RACKMOUNT TOP HEATSINK BAFFLE BRKT.png'),(765,'400W RACKMOUNT TOP HEATSINK DUCTING COVER.png'),(787,'400WATT C-BAND RACKMOUNT HEATSINK.png'),(562,'406-14-TSSOP.png'),(599,'40VQFN.png'),(811,'40W POWER SUPPLY BASE PLATE.png'),(381,'4180G.png'),(677,'4251.JPG'),(539,'428_51-85022_S_16.jpg'),(540,'428_51-85048_A_100.png'),(382,'43-77-20G.png'),(354,'44.jpg'),(194,'4403-033LF.jpg'),(231,'450QXW100MEFC16X35.png'),(472,'46207-0006.JPG'),(499,'46207-0012.JPG'),(551,'48-LQFP.png'),(578,'48-TSSOP.png'),(583,'488_318G-02_SN_DT_6.png'),(580,'488_527AE-01_5.png'),(581,'488_948F-01_DT_DTB_DB_16.png'),(420,'4900_4901.png'),(419,'4902.jpg'),(606,'497-216TFBGA-1_10-13x13-H-216.png'),(605,'497_A0B9_ME_U_48.png'),(415,'5-1814400-1.png'),(397,'50079-8100.png'),(417,'50147-8100.JPG'),(398,'5019301100.png'),(516,'5019312070.JPG'),(521,'5020462070.png'),(450,'5023820470.png'),(466,'5025780600.png'),(396,'5025790000.png'),(449,'5025840470.jpg'),(467,'5025840670.png'),(478,'5025850670.png'),(240,'502R30W681KF3E-^^^^-SC.png'),(515,'5031102000.png'),(529,'505RU-16_RU_16.png'),(660,'505_16QFN-1-3x3_16.png'),(664,'505_32QFN-1-5x5_32.png'),(651,'505_CP-16-10_CP_16.png'),(534,'505_CP-16-13_CP_16.png'),(537,'505_CP-16-22_CP_16.png'),(536,'505_CP-24-5_CP_24.jpg'),(650,'505_CP-24-5_CP_24.png'),(656,'505_HCP-32-1_32.png'),(532,'505_KS-5_KS_5.png'),(457,'51021-0500.png'),(849,'52116-0411.jpg'),(425,'52213-0211.JPG'),(428,'52266-0211.png'),(380,'54F-713-108.jpg'),(587,'559-PTSB0052LB-A-SD-52.jpg'),(573,'56-TSOP.png'),(544,'568-1402-03-HF-16.jpg'),(505,'5745782-4.JPG'),(668,'6 DFN.png'),(676,'6-UFDFN Exposed Pad.jpg'),(661,'6-VDFN Exposed Pad.png'),(393,'6051.2041.jpg'),(444,'6163_0004.png'),(524,'6368011-4.png'),(602,'64-LQFP.png'),(480,'67800-8005.png'),(207,'689-4-MICROX-NBB.jpg'),(456,'690-005-299-043.jpg'),(130,'69915k55p1-d01bl.png'),(184,'6EGS1-1.jpg'),(424,'7-1123722-2.JPG'),(174,'7010_351.jpg'),(547,'706_48TSOP-18_4_48.png'),(132,'7310k15p1-d01bs.png'),(707,'732-16LGA-EP-2.96-9x9-16.png'),(386,'740.jpg'),(49,'741C083472JP.jpg'),(196,'74270011.JPG'),(197,'7427007141.jpg'),(318,'744272221.JPG'),(321,'7448041801.jpg'),(332,'7448052502.jpg'),(56,'745_term.jpg'),(423,'7691_7701_8191.jpg'),(577,'7SZ_SOT?353_419A?02.jpg'),(375,'8-SOIC.png'),(552,'8-SOIC_05-08-1610.png'),(572,'8-SOIC_751.png'),(570,'8-TSSOP_8-MSOP.png'),(541,'8-VDFN.png'),(705,'8-WFDFN.png'),(546,'800_PGG20_PGG_G_EJ_20.jpg'),(323,'8107-RC.jpg'),(316,'8108-RC.JPG'),(131,'8173t11p999-d01bl.png'),(129,'8507k671c1-j06l.png'),(608,'85985k24p1-j06-digitall.png'),(607,'95495k313p2-j06l.png'),(155,'977-009-020R121.png'),(378,'9GA0948P1H03.jpg'),(64,'A-0603.jpg'),(280,'A-LED8-1AAAS-MR7-1-R.png'),(67,'A14416-01.jpg'),(4,'ABM3.jpg'),(1,'ABM3B.jpg'),(2,'ABM3C.png'),(7,'ABM8X-102-32.000MHZ-T.JPG'),(3,'ABS07.jpg'),(538,'ADI_SSOP_20_SPL.jpg'),(287,'AFB-60x60x15mm-Series-3-Wire.jpg'),(291,'AFB-Series-60SQx25-2Leads.jpg'),(286,'AFB0412SHB-T500.JPG'),(289,'AFB0612EH-AB.jpg'),(290,'AFB0624EH-TA50.png'),(296,'AFB0912SH-TZUG.jpg'),(288,'AFC0612DB-F00.png'),(657,'AMMP-6420_wht_175x175.jpg'),(5,'AOCJY-Series.jpg'),(6,'AOCJY221 SERIES.pnG'),(272,'APHB1608.png'),(213,'AQ Series AQ12.jpg'),(236,'AQ_Series_DSL.jpg'),(611,'ARM_SPL.jpg'),(90,'AT224-3.png'),(274,'B0530W.png'),(178,'B40L3905.jpg'),(459,'B5B-PH-K-S.png'),(53,'B88069X9991T203.JPG'),(179,'BALF-NRG-01D3.png'),(479,'BCS-107-F-D-PE.jpg'),(138,'BLM31 SERIES 1206.png'),(137,'BLM41 SERIES 1806.png'),(140,'BNX025H01L.png'),(63,'BRP1408P-12-CS.jpg'),(550,'BTS555 E3146.JPG'),(785,'C- 200 W RACKMOUNT PS HEATSINK LEFT-1.png'),(786,'C- 200 W RACKMOUNT PS HEATSINK RIGHT-2.png'),(782,'C- 200 W RACKMOUNT RF HEATSINK.png'),(443,'C-Grid-III-90120-Tin-3-Position.jpg'),(451,'C-Grid-III-90120-Tin-4-Position.png'),(473,'C-Grid-III-90120-Tin-Contact.png'),(208,'CBR Series.png'),(123,'CC1266.png'),(121,'CC1553.png'),(108,'CC51-1_SMA.png'),(102,'CC51_BNC_SMA.png'),(241,'CC_capacitor_SPL.jpg'),(256,'CD SERIES 15_5D.jpg'),(48,'CER0900A.png'),(843,'CF20120.jpg'),(355,'CFM Series 100k.jpg'),(365,'CFM Series 4-7k.jpg'),(358,'CFMSeries 1-2m.jpg'),(356,'CFMSeries 1m.png'),(360,'CFR-25JR-200R.png'),(685,'CHT4660-QAG.png'),(686,'CHT4690-QAG.png'),(85,'CJ725.png'),(673,'CK605.png'),(222,'CKG SERIES.png'),(69,'CM3440Z171R-10.png'),(714,'CONVERTER-BIAS ASSY-C-BAND-01.png'),(758,'COVER (70 TO L CONVERTOR).png'),(655,'CP-8-2.jpg'),(828,'CPR137 GROOVE FLANGE 198 HOLE 1_8BUT 0_25 THICKNES.png'),(347,'CRCW12101K00FKEAHP.png'),(254,'CS SERIES Short Lead 7_5mm.JPG'),(154,'CU24043-Y1A.jpg'),(189,'CUFS1-SERIES.jpg'),(46,'CVHD-950X-100.000.jpg'),(47,'CVSS-945X-100.000.JPG'),(103,'CY353.png'),(270,'D-61-8-SL.jpg'),(509,'d-sub-for-surface-mounting-3.jpg'),(162,'D10 14 V Series.png'),(104,'DB1627.png'),(87,'DB714.png'),(107,'DD477-1_SMA.png'),(701,'DDA-8-SOIC Pkg.png'),(257,'DE2E3KY472MN2AM01x.png'),(775,'DEVICE BLOCK 4 HOLE.png'),(776,'DEVICE BLOCK MMIC.png'),(774,'DEVICE BLOCK SMALL 2 HOLE.png'),(675,'DFN-6-Photo.jpg'),(190,'DFN51120.jpg'),(670,'dfn6.jpg'),(50,'DFO S1.png'),(595,'DGN-8-MSOP Exp Pad Pkg.png'),(115,'DJ951.png'),(205,'DL1636.png'),(59,'DNGL.png'),(279,'DO-214AA (SMBJ).png'),(341,'DO1608_series_DSL.jpg'),(314,'DO3316T_series_DSL.jpg'),(120,'DQ1225.png'),(111,'DS810.png'),(181,'dual+waveguide-coax.jpg'),(269,'D²PakTO-263_418B−04.png'),(176,'EC11.0001.001.21.png'),(248,'ECO-S2DB561DA.JPG'),(245,'ECQ-E2683JF.JPG'),(224,'ECQ-UAAF105M.jpg'),(223,'ECQ-UL SERIES.png'),(263,'ECW-FD SERIES 16_2mm.JPG'),(262,'ECWF(A) Series Style B_8-W.jpg'),(227,'EEE-FTH101XAP.JPG'),(266,'EEH-ZA1V270P.png'),(250,'EET-UQ2S471KF.JPG'),(284,'EFB0412LD.png'),(520,'EHF-110-01-L-D.jpg'),(526,'EHF-120-01-L-D.jpg'),(648,'EHHD010A0B41-HZ.png'),(503,'EJH-107-01-F-D-SM-LC.jpg'),(379,'EMI FEEDTHROUGH.png'),(136,'EMIFIL-NFL-ST-18-Series.png'),(683,'EMM5075VU.jpg'),(312,'EPL2010_series_DSL.jpg'),(203,'ERA-2SM+.png'),(364,'ERD-S2TJ3R3V.jpg'),(348,'erj.jpg'),(502,'ESW-107-33-G-D.jpg'),(161,'EXBN8V Pkg.png'),(39,'f1199aa20.JPG'),(109,'F14_BNC.png'),(665,'F2932NBGP.jpg'),(156,'FC-SERIES-10.50mm..png'),(30,'FC100V10A-G.jpg'),(31,'FC100V20A-G.jpg'),(127,'FF2586.png'),(110,'FF658.png'),(97,'FF704.png'),(118,'FF747.png'),(99,'FF779.png'),(98,'FF888.png'),(285,'FFB0412HHN-F00.jpg'),(426,'FHP-02-01-T-S.jpg'),(463,'FHP-06-01-T-S.jpg'),(384,'Film Coupler.png'),(228,'FK SERIES.png'),(93,'FK811.png'),(95,'FL905.png'),(37,'FLP25-10.0.JPG'),(101,'FM587.png'),(682,'FMM5056VF.png'),(684,'FMM5057VF.jpg'),(177,'FN9280-10-06.png'),(349,'FPR2-T218 10.000 OHM 1.png'),(57,'FSMLF327.jpg'),(525,'FTS-120-01-F-D.jpg'),(493,'FTSH-105-01-F-DV-K.jpg'),(519,'FTSH-110-01-F-DV-K.jpg'),(88,'FV1206-1.png'),(91,'FV1206-4.png'),(113,'FV1206-7.png'),(86,'FV1206.png'),(431,'FWJ-02-04-T-S-RA.jpg'),(474,'FWJ-06-04-T-S-RA.jpg'),(486,'G17S0910110EU.png'),(158,'G6K-2F-Y.png'),(253,'GA3 Series 1808(4520 Metric) 15.jpg'),(760,'GAN RF CARRIER COVER.png'),(206,'GC957.png'),(116,'GE0805C-3.png'),(114,'GE0805C-9.png'),(157,'GRM60-30.png'),(164,'H1102FNL.png'),(126,'H16_SMA_F.png'),(60,'H2055-01.png'),(61,'H9023-01.png'),(322,'ha4031.jpg'),(42,'HCM49 seris.png'),(329,'hcma0503_SPL.jpg'),(229,'HE10 SERIES.png'),(182,'header_step_tongue_loads.jpg'),(183,'header_taperedload.jpg'),(117,'HL1162.png'),(500,'HTSW-106-07-L-D.jpg'),(504,'HTSW-107-07-L-D.jpg'),(672,'HV1195.png'),(458,'I2SS-05-24-T.jpg'),(436,'IM0005504_large.jpg'),(385,'IMG_0522.JPG'),(62,'INDIUM-SILVER RIBBON.jpg'),(542,'inhouse_xra1405ig240beb_t.jpg'),(514,'IPD1-10-D-K.jpg'),(491,'IPL1-105-02-L-D-K.jpg'),(522,'IPL1-110-02-L-D-K.JPG'),(391,'IPT059N15N3ATMA1.png'),(590,'IS45S32160F-6TLA1.png'),(589,'IS62WV10248DBLL-55TLI.png'),(702,'ITP_TI_MSOP-Power-Pad-8_DGN_1.jpg'),(353,'ITP_Yageo_MFR_1.jpg'),(89,'JM1360.png'),(124,'K18_BNC.png'),(92,'K18_N.png'),(100,'K18_SMA.png'),(835,'KA FEEDING WAVEGUIDE.png'),(836,'KA UPCONVERTER OUTPUT WAVEGUIDE.png'),(746,'KA-FILTER-MECHANIC-CONCEPT-TOP.png'),(771,'Ka-SSPA BASE PLATE.png'),(739,'Ka-SSPA INSIDE WAVEGUIDE ASSEMBLY.png'),(772,'Ka-SSPA MODULE COVER.png'),(773,'Ka-SSPA SPLITTER COMBINER.png'),(770,'Ka-SSPA TOP COVER.png'),(94,'KB1450.png'),(264,'KTD250B106M43A0B00.jpg'),(754,'KU BAND 25W RF CARRIER.png'),(755,'KU BAND 40W_16W GAN_GAAS RF CARRIER.png'),(729,'KU BAND PICOBUC FAN SHROUD ASSEMBLY.png'),(716,'Ku-BAND Input Module-Bias ASSY.png'),(239,'KY SERIES 8x13mm Long Leads.png'),(255,'KY-SERIES-Long-Lead-7mm.png'),(230,'KYB SERIES.JPG'),(251,'LGL2W391MELA50.JPG'),(736,'LINEARIZER CONTROL ASSEMBLY.png'),(106,'LL561.png'),(122,'LL718.png'),(84,'lpf-c011304s.jpg'),(310,'LQG15H-0402(1005Metric).png'),(309,'LQP15M SERIES.png'),(623,'LS100-24.png'),(620,'LS25-12.png'),(618,'LS50-15.png'),(268,'LTST-C190KGKT LTST-C190KRKT LTST-C190KSKT LTST-C190KYKT.png'),(432,'M22-2510246.JPG'),(669,'MACOM_AQFN_32_lead.jpg'),(233,'MAL214699811E3.png'),(545,'MC33984.jpg'),(68,'MCS_SS6M.png'),(409,'MFG_132275.JPG'),(410,'MFG_132318.JPG'),(33,'MFG_2051 Series.png'),(172,'MFG_3413.0512.11.jpg'),(422,'MFG_4915.jpg'),(232,'MFG_AFK series_small.png'),(308,'MFG_AIAC01.png'),(200,'MFG_AMMP-5618.jpg'),(616,'MFG_ASB110PS.png'),(128,'MFG_BU-P72973.png'),(403,'MFG_DNF18-188-M.jpg'),(619,'MFG_EMH250.png'),(624,'MFG_FE175D480C033FP-00.png'),(647,'MFG_HalfVIChipPRM.png'),(634,'MFG_HalfVIChipVTM.png'),(639,'MFG_IQG-Series.jpg'),(645,'MFG_iQL-Series_BasePlate.JPG'),(377,'MFG_LFPAK56_POWER-SO8_SOT669.jpg'),(626,'MFG_LRS-350.png'),(610,'MFG_LRS-50.png'),(632,'MFG_Micro Series.png'),(633,'MFG_Mini Series.png'),(320,'MFG_MLF2012.png'),(637,'MFG_NEB-D.jpg'),(641,'MFG_NQB-468DM.jpg'),(643,'MFG_NQB-D.jpg'),(613,'MFG_PFEx00xx.png'),(359,'MFG_PVG3A.png'),(628,'MFG_RQ-50.jpg'),(612,'MFG_RQ-85.jpg'),(625,'MFG_RSP-500.png'),(627,'MFG_TUNS300.png'),(617,'MFG_TUNS500.jpg'),(621,'MFG_TUNS700F.png'),(846,'MFG_TXH060-xxx.jpg'),(357,'MFR-25FBF52-12K1.jpg'),(369,'MFR-25FBF52-52K3.jpg'),(370,'MFR-25FBF52-59K.jpg'),(361,'MFR-25FRF52-20K.jpg'),(351,'mfr_dsl.jpg'),(198,'MGA-30489-BLKG.png'),(40,'mintrans.jpg'),(259,'MKT SERIES 11.0H 13_0L.png'),(204,'MNA-7+.png'),(512,'ms3111.jpg'),(483,'ms3112.jpg'),(470,'MS3112E-10-6P.jpg'),(487,'MS3112E-12-10S.jpg'),(441,'MS3112E-12-3P.jpg'),(513,'MS3112E-14-19P.jpg'),(490,'MS3116F-12-10P.jpg'),(438,'MS3116F-12-3S.jpg'),(511,'MS3116F-14-19S.jpg'),(22,'MS3181-14CA.JPG'),(568,'MSOP-8.png'),(833,'MTEE STRAIGHT BOTTOM.png'),(832,'MTEE STRAIGHT TOP.png'),(242,'MVE_SPL.jpg'),(243,'MZA SERIES.jpg'),(752,'N-TYPE WR75 MORENO SPACER.png'),(65,'ND3-0505WA50R0G.jpg'),(778,'NEW 100 WATT KU ANTBUC ENCLOSURE.png'),(753,'NEW KA-FILTER COMBINED.png'),(119,'NF1846-1.png'),(142,'NFE61P SERIES.png'),(141,'NFL21S SERIES.png'),(134,'NFM18CC223R1C3D.png'),(404,'NF_female_term_105300_ISO.jpg'),(152,'NJR2936EN.jpg'),(153,'NJS8487SN.jpg'),(151,'norsat-3000x-lnb-c-band_2_2_1.jpg'),(389,'o-ring_sma.jpg'),(283,'OD4010-05MB.jpg'),(51,'OH300-SERIES.png'),(495,'OPP-10-01-T-S-E-AD.jpg'),(273,'ORFLP2-RGB_SPL.jpg'),(244,'OS-CON SEPC 8mm.png'),(238,'OS-CON SVPE 6_30mm.jpg'),(246,'OS-CON-SVQP-SERIES-8mm.jpg'),(461,'OSTTC052162.png'),(609,'p423-breather-drain.jpg'),(366,'PAC100001007FA1000.png'),(635,'PAF500F-Series.jpg'),(674,'PE4283.png'),(292,'PFB0612GHE-T500.jpg'),(294,'PFB0612UHE-T50F.jpg'),(300,'PFB0912DHE.jpg'),(299,'PFB0912UHE.jpg'),(301,'PFB0924DHE-T500.jpg'),(298,'PFB0948UHE-TP16.JPG'),(614,'PFE5000SA.jpg'),(295,'PFR0612XHE-SP00.jpg'),(73,'PGB102ST23WR.png'),(666,'PG_TO_263_7_DSL.jpg'),(374,'Pkg 5880.png'),(35,'PLP1-1xx-FS.jpg'),(36,'PLP1-350-F.jpg'),(38,'PM3-P1522-10.00.jpg'),(324,'PM3700 SERIES.png'),(297,'PMD2409PMB1-A(2)GN.JPG'),(293,'PMD4806PMB1-A.(2)GN.png'),(260,'PME295 SERIES 8_5mm.JPG'),(58,'PMF100586.jpg'),(350,'PML_series_DSL.jpg'),(777,'POWER DEVICE BLOCK 2 HOLE COPPER.png'),(646,'PRM48BF480T500A00.png'),(717,'PS COV ASSY PFE AC-01 GaAs.png'),(718,'PS COV ASSY PFE AC-01 GaN.png'),(719,'PS COV ASSY PFE DC- 02.png'),(586,'PSM712-LF-T7.png'),(462,'PT06_series.jpg'),(55,'PV6 LAMB SERIES.png'),(649,'Q36SR12020NRFH.JPG'),(679,'QFN_16_EP_t.jpg'),(261,'R46KN3220JBM1K.png'),(735,'RACKMOUNT REDUNDANCY REMOTE PANEL ASSEMBLY.png'),(346,'RC_series_SPL.jpg'),(767,'RF COVER DUAL 200 W C-BAND NEW.png'),(363,'RH SERIES 26_97x27.43mm.png'),(535,'RM-8.png'),(681,'s-l1600.jpg'),(388,'S0604_pd.png'),(191,'S1512787-01.jpg'),(271,'S1J.png'),(282,'SC-70 SOT-323.jpg'),(579,'SC-70-6_SC-88_SOT-363_419B?02.png'),(687,'SCT-595.png'),(496,'SEI-110-02-G-S-AB.jpg'),(517,'SFSD-10-28-G-12.00-S.jpg'),(481,'shopping.jpg'),(508,'SIB-116-02-F-S-LC.jpg'),(373,'SIHP7N60E-GE3.png'),(696,'SIMPLE-SWITCHER�-Series-43-QFN.png'),(219,'SKYCAP SR SERIES.png'),(338,'slc7530_dim.jpg'),(497,'SLW-106-01-L-D.jpg'),(29,'SMM_30.pnG'),(169,'SMP-J-B-GF-ST-0645.png'),(249,'SMQ SERIES sml.jpg'),(276,'SMV1247-040LF.png'),(267,'SOD-123.png'),(275,'SOD-523 (1-1G1A SC-79).png'),(569,'SOT-23-3 PKG.png'),(159,'SOT-23-3_527AG.png'),(594,'SOT-23-5 PKG.png'),(560,'SOT-23-6 PKG.png'),(565,'SOT-23-8 PKG.png'),(199,'SOT-363 PKG.png'),(592,'SOT-666.jpg'),(591,'SOT23-5.png'),(575,'SOT753.png'),(372,'SOT_223_3_DSL.jpg'),(564,'SOT_23_8_t.jpg'),(32,'SPK10-0_006-00-90.png'),(335,'sq_spring.jpg'),(336,'SRP1245A-xxxx.png'),(342,'SRP7030_series_SPL.jpg'),(593,'STM 8-SOIC.png'),(455,'T2I-04-VT-T-S-TH.jpg'),(460,'T2I-05-VT-T-S-TH.jpg'),(173,'TA45-ABDBL200C0.png'),(671,'TDFN-6_SPL_t.jpg'),(523,'TFM-115-02-L-D-A.jpg'),(492,'TFML-105-02-L-D.jpg'),(518,'TFML-110-02-L-D-LC.jpg'),(678,'TGA2595-CP_DSL.jpg'),(622,'TH120024.JPG'),(302,'THA0948BE.JPG'),(192,'THR4.jpg'),(193,'THRBP FRONT.JPG'),(600,'ti-iso1541.jpg'),(695,'TI_TO_PMOD_11.jpg'),(706,'TI_VQFN_28_SPL.jpg'),(54,'TL1220.jpg'),(549,'TO-252-5_DPak.jpg'),(697,'TO-252-5_DPak.png'),(376,'TO-263-7 DPak (6 LeadsTab).png'),(700,'TO-263-8.png'),(277,'TO-277A SMPC.png'),(371,'TO-92-3(StandardBody)TO-226_straightlead.png'),(699,'TPS84250RKGT.png'),(160,'TQ-N-SERIES.png'),(368,'TS63Y502KR10.png'),(555,'TSOT-23-5_05-08-1635.png'),(598,'TSOT-23-6.png'),(667,'TSOT_23_6_t.jpg'),(237,'TSUQ SERIES.jpg'),(501,'TSW-106-17-L-D.jpg'),(45,'TSWB-3N-CB222.png'),(585,'TX-GULL.png'),(584,'TX2SA_DSL.jpg'),(234,'UHV_series_DSL.jpg'),(235,'UHW_series_DSL.jpg'),(638,'UIE48T10120-NDABG.JPG'),(644,'UIQ48T20120-NDA0G.JPG'),(642,'UIQ48T_series.jpg'),(636,'UIS48T06120-NDABG.JPG'),(171,'UMT 250 SERIES_sml.jpg'),(781,'UPCONVERTER INSIDE BOTTOM PLATE.png'),(383,'US5_ZUSA-HT-1260.jpg'),(596,'USB-8-VSSOP Pkg.png'),(631,'UWE-12^10-Q12NB-C.jpg'),(629,'UWE-12^10-Q12PB-C.png'),(640,'UWQ_12_20_T48.jpg'),(75,'V120CH8.png'),(74,'V130LA5P.png'),(105,'V1381.png'),(77,'V14MLA0805N.png'),(71,'V275LA20AP.png'),(78,'V275SM20.jpg'),(630,'VHB350.jpg'),(125,'VVV180.png'),(258,'VY1-Series_straight.png'),(247,'VZ SERIES PURPLE HIGHLIGHT 993-995.jpg'),(840,'WALL RF CARRIER GAN BOTTOM.png'),(837,'WALL RF CARRIER GAN TOP.png'),(180,'waveguide.jpg'),(41,'wbc.jpg'),(847,'WE-CLFS.png'),(352,'Wide 1206 (3216 Metric) 0612.png'),(824,'WR75-TRANSITION -THIN.png'),(394,'x-2273008-1.png'),(395,'x-2273082-1.png'),(330,'XAL5020_series_DSL.jpg'),(328,'XAL5030_series_DSL.jpg'),(313,'XAL5050_series_DSL.jpg'),(112,'Z184.png'),(265,'ZLJ SERIES 6_3mm.jpg'),(143,'ZRB-SERIES-0402_sml.png'),(96,'ZZ121.png');
/*!40000 ALTER TABLE `image_link` ENABLE KEYS */;
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