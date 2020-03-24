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
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `permission` int(11) DEFAULT '0',
  `extension` varchar(3) DEFAULT NULL,
  `e_mail` varchar(45) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Alex','ZHJhZ29u','Oleksandr','Potomkin',67075584,'406','oleksandr@irttechnologies.com',1),(2,'Alexander','?','Alexander','Vasiliev',2621440,'409','alex@irttechnologies.com',1),(3,'Alexioffe','MTIzNDU2','Alexander','Ioffe',2818048,'408','alexioffe@irttechnologies.com',1),(4,'Amir','?','Amir','Motiuk',4851712,'401','amir@irttechnologies.com',1),(5,'Arkadi','?','Arkadi','Potapov',4718592,'412','arkadi@irttechnologies.com',1),(6,'dani','ZGhraXJ0','Dani','Kingsbury',0,'407','dani@irttechnologies.com',0),(7,'darcy','ZGFyY3k=','Darcy','Crawley',2752512,NULL,'darcy@irttechnologies.com',1),(8,'Faik','?','Faik','Ibragimov',524288,'409','faik@irttechnologies.com',1),(9,'Igor','?','Igor','Perlitch',6815744,'402','igor@irttechnologies.com',1),(10,'marina','bWFyaW5h','Marina','Lissianskaia',40370176,'403','marina@irttechnologies.com',1),(11,'roldan','dGVzdDEyMw==','Roldan','Perez Morales',524288,'404','roldan@irttechnologies.com',0),(12,'semion','aXJ0MjAxMg==','Semion','Katz',24010752,'405','semion@irttechnologies.com',0),(13,'lena','YWxsZW4=','Elena','Perlitch',40790528,NULL,'elena@irttechnologies.com',1),(14,'Sagy','MTgwNjc1','Sagy','Keren',2691072,'407','sagy@irttechnologies.com',0),(15,'alexe','bm9raWFuNzI=','Alex','Eisenhut',7172608,NULL,'alexe@irttechnologies.com',1),(16,'yelena','MjAxNg==','Yelena',NULL,7233536,NULL,'yelena@irttecnologies.com',1),(17,'ritesh','eUE3MDkxMTM=','Ritesh','Patel',2621440,NULL,'ritesh@irttechnologies.com',0),(18,'Masha','ZG1saTExMTg=','Masha','Perlitch',7233536,NULL,NULL,1),(19,'alvin','aXJ0MTIz','alvin','de lara',524288,NULL,'delaraalvin_512@yahoo.com',1),(20,'arayda','cnR0eXJ0dHk=','Anatoly','Rayda',23408640,NULL,NULL,1),(21,'Arline','QWwhakBoYXVkcmljMjI0','Arline','De Lara',524288,NULL,NULL,1),(22,'xucao','MTIzNA==','Xu','Cao',2752512,NULL,'xucao@irttechnologies.com',1),(23,'ravikumar','Qm9kZHU=','Ravi Kumar','Boddu',11411456,NULL,'ravi@irttechnologies.com',1),(24,'Roman','?','Roman','Chopik',17301504,NULL,NULL,1),(25,'vav','NDMyMQ==','vlad','mik',655360,NULL,NULL,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:16:02
