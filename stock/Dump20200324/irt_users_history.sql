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
-- Table structure for table `users_history`
--

DROP TABLE IF EXISTS `users_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users_history` (
  `date` datetime NOT NULL,
  `by` int(10) unsigned NOT NULL,
  `id_users` int(10) unsigned NOT NULL,
  `detil` varchar(245) NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users_history`
--

LOCK TABLES `users_history` WRITE;
/*!40000 ALTER TABLE `users_history` DISABLE KEYS */;
INSERT INTO `users_history` VALUES ('2013-07-31 11:01:02',1,1,'`username`=\'Alex\',`password`=\'ZHJhZ29u\''),('2013-07-31 11:01:18',1,2,'`permission`=2621440'),('2013-07-31 11:01:25',1,3,'`permission`=2686976'),('2013-07-31 11:01:30',1,4,'`permission`=4849664'),('2013-07-31 11:01:39',1,5,'`permission`=4718592'),('2013-07-31 11:01:46',1,6,'`permission`=2686976'),('2013-07-31 11:01:50',1,7,'`permission`=2621440'),('2013-07-31 11:01:58',1,13,'`permission`=7233536'),('2013-07-31 11:02:04',1,8,'`permission`=4718592'),('2013-07-31 11:02:08',1,9,'`permission`=2621440'),('2013-07-31 11:02:14',1,10,'`permission`=6815744'),('2013-07-31 11:02:22',1,11,'`permission`=524288'),('2013-07-31 11:02:27',1,12,'`permission`=7233536'),('2013-07-31 11:04:31',1,2,'`extension`=\'409\',`e_mail`=\'alex@irttechnologies.com\''),('2013-07-31 11:04:59',1,7,'`e_mail`=\'darcy@irttechnologies.com\''),('2013-07-31 11:09:41',13,13,'`password`=\'YWxsZW4=\''),('2013-07-31 13:07:49',12,12,'`username`=\'semion\',`password`=\'aXJ0MjAxMg==\''),('2013-08-02 08:21:23',1,4,'`permission`=4851712'),('2013-08-08 09:16:31',6,6,'`username`=\'dani\',`password`=\'ZGhraXJ0\''),('2013-08-08 13:28:34',7,7,'`username`=\'darcy\',`password`=\'ZGFyY3k=\''),('2013-08-18 21:21:26',1,8,'`permission`=524288'),('2013-08-18 21:21:56',1,9,'`permission`=6815744'),('2013-08-28 09:31:09',11,11,'`username`=\'roldan\',`password`=\'dGVzdDEyMw==\''),('2013-09-09 08:53:59',1,3,'`permission`=2818048'),('2013-09-09 11:49:33',3,3,'`username`=\'Alexioffe\',`password`=\'MTIzNDU2\''),('2013-09-11 10:44:46',1,13,''),('2013-09-11 10:45:02',1,13,'`password`=\'?\''),('2013-09-11 10:45:35',13,13,'`password`=\'YWxsZW4=\''),('2013-10-30 10:35:05',1,6,'`permission`=2691072'),('2014-03-18 13:10:10',1,1,'`permission`=15726592'),('2014-04-17 12:54:17',1,1,''),('2014-10-01 09:06:23',14,14,'`password`=\'MTgwNjc1\''),('2015-05-25 11:51:35',15,15,'`password`=\'bm9raWFuOTAw\''),('2015-09-02 10:35:06',15,15,'`password`=\'bm9raWFuNzI=\''),('2016-04-04 12:29:34',13,13,'`password`=\'YWxsZW4=\''),('2016-04-04 12:53:51',16,16,'`username`=\'yelena\',`password`=\'MjAxNg==\''),('2016-08-02 12:52:38',17,17,'`password`=\'eUE3MDkxMTM=\''),('2016-08-02 12:54:09',17,17,'`password`=\'eUE3MDkxMTM=\''),('2018-05-17 14:25:55',18,18,'`password`=\'ZG1saTExMTg=\''),('2018-06-01 11:14:42',10,10,'`username`=\'marina\',`password`=\'bWFyaW5h\'');
/*!40000 ALTER TABLE `users_history` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-03-24 10:16:03
