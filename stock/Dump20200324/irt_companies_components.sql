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
-- Table structure for table `companies_components`
--

DROP TABLE IF EXISTS `companies_components`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `companies_components` (
  `id_companies` int(10) unsigned NOT NULL,
  `id_components` int(10) unsigned NOT NULL,
  `qty` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_companies`,`id_components`),
  CONSTRAINT `fk_companies_components_companies1` FOREIGN KEY (`id_companies`) REFERENCES `companies` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `companies_components`
--

LOCK TABLES `companies_components` WRITE;
/*!40000 ALTER TABLE `companies_components` DISABLE KEYS */;
INSERT INTO `companies_components` VALUES (1,1,0),(1,3,0),(1,4,0),(1,5,0),(1,7,0),(1,8,0),(1,9,0),(1,10,0),(1,11,0),(1,12,0),(1,13,0),(1,14,0),(1,16,0),(1,19,0),(1,20,0),(1,21,0),(1,22,0),(1,23,0),(1,25,0),(1,26,0),(1,27,0),(1,29,0),(1,30,0),(1,31,0),(1,32,0),(1,36,0),(1,37,0),(1,38,0),(1,39,0),(1,40,0),(1,41,0),(1,42,0),(1,43,0),(1,44,0),(1,47,0),(1,48,0),(1,50,0),(1,51,0),(1,53,0),(1,54,0),(1,56,0),(1,58,0),(1,60,0),(1,61,0),(1,63,0),(1,64,0),(1,65,0),(1,67,0),(1,70,0),(1,73,0),(1,75,0),(1,76,0),(1,78,0),(1,79,0),(1,80,0),(1,81,0),(1,85,0),(1,87,0),(1,88,0),(1,89,0),(1,90,0),(1,91,0),(1,94,0),(1,99,0),(1,100,0),(1,102,0),(1,105,0),(1,106,0),(1,107,0),(1,108,0),(1,109,0),(1,110,0),(1,111,0),(1,112,0),(1,114,0),(1,115,0),(1,118,0),(1,119,0),(1,120,0),(1,123,0),(1,125,0),(1,126,0),(1,127,0),(1,128,0),(1,129,0),(1,130,0),(1,133,0),(1,134,0),(1,136,0),(1,137,0),(1,139,0),(1,140,0),(1,141,0),(1,142,0),(1,143,0),(1,145,0),(1,146,0),(1,149,0),(1,150,0),(1,151,0),(1,154,0),(1,156,0),(1,157,0),(1,158,0),(1,162,0),(1,163,0),(1,169,0),(1,170,0),(1,172,0),(1,175,0),(1,178,0),(1,179,0),(1,180,0),(1,182,0),(1,184,0),(1,185,0),(1,186,0),(1,187,0),(1,188,0),(1,189,0),(1,191,0),(1,192,0),(1,193,0),(1,195,0),(1,196,0),(1,197,0),(1,198,0),(1,199,0),(1,200,0),(1,201,0),(1,202,0),(1,205,0),(1,206,0),(1,207,0),(1,208,0),(1,210,0),(1,211,0),(1,212,0),(1,213,0),(1,215,0),(1,216,0),(1,217,0),(1,218,0),(1,219,0),(1,221,0),(1,222,0),(1,234,0),(1,236,0),(1,237,0),(1,268,0),(1,274,0),(1,275,0),(1,276,0),(1,277,0),(1,278,0),(1,279,0),(1,280,0),(1,282,0),(1,290,0),(1,291,0),(1,292,0),(1,293,0),(1,302,0),(1,304,0),(1,305,0),(1,306,0),(1,307,0),(1,310,0),(1,315,0),(1,318,0),(1,319,0),(1,320,0),(1,324,0),(1,336,0),(1,341,0),(1,342,0),(1,343,0),(1,350,0),(1,353,0),(1,355,0),(1,356,0),(1,358,0),(1,360,0),(1,361,0),(1,365,0),(1,368,0),(1,369,0),(1,370,0),(1,372,0),(1,382,0),(1,385,0),(1,386,0),(1,389,0),(1,391,0),(1,393,0),(1,395,0),(1,404,0),(1,406,0),(1,408,0),(1,413,0),(1,414,0),(1,417,0),(1,426,9750),(1,429,0),(1,430,0),(1,431,0),(1,480,0),(1,499,0),(1,508,0),(1,509,0),(1,511,0),(1,519,0),(1,525,0),(1,526,0),(1,528,0),(1,529,0),(1,534,0),(1,536,0),(1,537,0),(1,547,0),(1,568,0),(1,571,0),(1,572,0),(1,573,0),(1,574,0),(1,579,0),(1,580,0),(1,581,0),(1,583,0),(1,585,0),(1,586,0),(1,590,0),(1,591,0),(1,596,0),(1,598,0),(1,602,0),(1,603,0),(1,604,0),(1,611,0),(1,612,0),(1,613,0),(1,614,0),(1,617,0),(1,618,0),(1,619,0),(1,620,0),(1,622,0),(1,624,0),(1,626,0),(1,627,0),(1,633,0),(1,652,0),(1,656,0),(1,659,0),(1,661,0),(1,663,0),(1,664,0),(1,665,0),(1,691,0),(1,692,0),(1,693,0),(1,694,0),(1,695,0),(1,696,0),(1,700,0),(1,703,0),(1,706,0),(1,707,0),(1,708,0),(1,710,0),(1,712,0),(1,713,0),(1,715,0),(1,719,0),(1,720,0),(1,721,0),(1,722,0),(1,723,0),(1,724,0),(1,725,0),(1,727,0),(1,728,0),(1,729,0),(1,730,0),(1,732,0),(1,733,0),(1,735,0),(1,736,0),(1,737,0),(1,738,0),(1,739,0),(1,740,0),(1,741,0),(1,742,0),(1,743,0),(1,745,0),(1,746,0),(1,747,0),(1,749,0),(1,760,0),(1,761,0),(1,763,0),(1,764,0),(1,765,0),(1,766,0),(1,769,0),(1,770,0),(1,771,0),(1,778,0),(1,783,0),(1,796,0),(1,801,0),(1,804,0),(1,812,0),(1,813,0),(1,816,0),(1,817,0),(1,818,0),(1,819,0),(1,820,0),(1,821,0),(1,822,0),(1,823,0),(1,824,0),(1,825,0),(1,826,0),(1,856,0),(1,877,0),(1,878,0),(1,886,0),(1,906,0),(1,909,0),(1,910,0),(1,916,0),(1,923,0),(1,942,0),(1,943,0),(1,960,0),(1,961,0),(1,984,0),(1,1005,0),(1,1007,0),(1,1008,0),(1,1009,0),(1,1011,0),(1,1044,0),(1,1046,0),(1,1060,0),(1,1064,0),(1,1066,0),(1,1067,0),(1,1073,0),(1,1074,0),(1,1078,0),(1,1133,0),(1,1194,0),(1,1197,0),(1,1199,0),(1,1200,0),(1,1214,0),(1,1226,0),(1,1247,0),(1,1252,0),(1,1258,0),(1,1260,0),(1,1280,0),(1,1295,0),(1,3095,0),(20,285,0),(25,1130,0),(40,1,0),(40,3,0),(40,4,0),(40,5,0),(40,6,0),(40,7,3200),(40,8,0),(40,9,0),(40,10,48),(40,11,48),(40,13,268),(40,14,0),(40,16,48),(40,19,48),(40,20,8),(40,21,0),(40,23,0),(40,24,0),(40,25,283),(40,26,3138),(40,27,2200),(40,28,210),(40,29,153),(40,30,0),(40,31,398),(40,32,0),(40,34,496),(40,36,0),(40,37,0),(40,38,6847),(40,39,13812),(40,40,3678),(40,41,6731),(40,42,16224),(40,43,5220),(40,44,10132),(40,47,11377),(40,48,3453),(40,49,0),(40,50,2538),(40,51,436),(40,53,9182),(40,54,2396),(40,56,2492),(40,58,3650),(40,60,2000),(40,61,14434),(40,62,976),(40,63,5368),(40,64,5256),(40,65,13924),(40,66,462),(40,67,11006),(40,70,2700),(40,71,0),(40,73,9120),(40,75,7322),(40,76,4688),(40,78,460),(40,79,994),(40,80,1217),(40,81,0),(40,83,516),(40,85,0),(40,87,0),(40,88,0),(40,89,1430),(40,90,0),(40,91,600),(40,92,0),(40,94,500),(40,99,0),(40,100,10998),(40,102,70),(40,105,5172),(40,106,5612),(40,107,15185),(40,108,2730),(40,109,15752),(40,110,4767),(40,111,336),(40,112,8200),(40,114,2892),(40,115,8095),(40,118,3084),(40,119,0),(40,120,8820),(40,121,8174),(40,123,2996),(40,124,9078),(40,125,5288),(40,126,10748),(40,127,8050),(40,128,8225),(40,129,0),(40,130,6308),(40,131,7896),(40,133,348),(40,134,580),(40,136,7142),(40,137,0),(40,139,7880),(40,140,10176),(40,141,11588),(40,142,7392),(40,143,7312),(40,144,0),(40,145,8640),(40,146,5176),(40,149,8180),(40,150,5092),(40,151,11980),(40,154,0),(40,156,3620),(40,157,667),(40,158,0),(40,162,528),(40,163,136),(40,166,0),(40,169,467),(40,170,352),(40,172,670),(40,174,0),(40,175,452),(40,178,2610),(40,179,336),(40,180,440),(40,182,2682),(40,183,110),(40,184,1856),(40,185,206),(40,186,3290),(40,187,3007),(40,188,109),(40,189,190),(40,191,136),(40,192,0),(40,193,0),(40,195,0),(40,196,297),(40,197,0),(40,199,0),(40,200,0),(40,201,0),(40,202,0),(40,205,105),(40,206,101),(40,207,91),(40,208,100),(40,210,0),(40,211,129),(40,212,106),(40,213,0),(40,215,0),(40,216,203),(40,217,355),(40,218,0),(40,219,149),(40,220,197),(40,221,0),(40,222,348),(40,235,0),(40,237,1000),(40,239,0),(40,242,8),(40,268,105),(40,271,1496),(40,272,0),(40,274,575),(40,275,240),(40,276,296),(40,277,105),(40,278,6488),(40,279,0),(40,280,16128),(40,282,1696),(40,290,0),(40,291,0),(40,292,2000),(40,293,137),(40,294,0),(40,297,19),(40,299,81),(40,300,9),(40,301,18),(40,304,3045),(40,305,219),(40,306,185),(40,307,313),(40,310,0),(40,316,916),(40,318,1536),(40,319,3406),(40,320,2004),(40,321,0),(40,324,4854),(40,325,9596),(40,326,848),(40,327,596),(40,328,496),(40,329,696),(40,335,0),(40,336,956),(40,337,5574),(40,342,20),(40,343,447),(40,349,0),(40,350,5822),(40,353,0),(40,355,410),(40,356,2624),(40,358,499),(40,360,158),(40,361,76),(40,365,156),(40,368,158),(40,369,2),(40,370,2596),(40,372,649),(40,379,1880),(40,381,348),(40,382,4600),(40,385,440),(40,386,100),(40,389,205),(40,391,1),(40,392,450),(40,393,416),(40,395,0),(40,404,10),(40,406,834),(40,408,7068),(40,409,0),(40,410,952),(40,411,0),(40,413,9300),(40,414,2248),(40,423,0),(40,429,0),(40,431,738),(40,432,0),(40,445,0),(40,451,700),(40,467,0),(40,469,676),(40,473,0),(40,480,2948),(40,490,6),(40,499,363),(40,506,8794),(40,508,3900),(40,509,906),(40,511,2498),(40,515,4900),(40,518,0),(40,519,10378),(40,521,334),(40,525,4400),(40,526,210),(40,528,4928),(40,529,520),(40,531,292),(40,536,8916),(40,537,17518),(40,545,3744),(40,571,2898),(40,572,44),(40,574,68),(40,579,176),(40,580,8540),(40,581,3067),(40,583,6600),(40,584,3314),(40,585,13684),(40,586,5282),(40,587,0),(40,588,922),(40,589,3560),(40,590,5176),(40,591,0),(40,596,209),(40,602,7510),(40,603,2236),(40,604,8058),(40,605,70),(40,606,7022),(40,607,1974),(40,609,0),(40,611,2820),(40,612,1),(40,613,66),(40,614,0),(40,617,0),(40,618,3),(40,619,0),(40,620,5),(40,621,0),(40,622,6),(40,624,226),(40,626,210),(40,627,0),(40,633,0),(40,652,103),(40,656,7636),(40,661,0),(40,663,434),(40,664,348),(40,665,0),(40,691,30),(40,692,1753),(40,693,0),(40,694,30),(40,695,0),(40,696,8570),(40,699,8),(40,700,1400),(40,703,815),(40,706,920),(40,707,60),(40,710,0),(40,712,6222),(40,719,104),(40,720,1500),(40,721,9750),(40,722,6354),(40,723,0),(40,724,0),(40,727,91),(40,728,1),(40,729,190),(40,730,130),(40,731,54),(40,732,0),(40,733,341),(40,736,0),(40,737,50),(40,738,1),(40,739,7),(40,740,43),(40,741,200),(40,742,300),(40,743,2466),(40,744,70),(40,747,3000),(40,749,86),(40,760,2350),(40,761,0),(40,763,8092),(40,764,4600),(40,766,7858),(40,767,664),(40,769,3710),(40,770,2576),(40,771,3052),(40,783,650),(40,787,0),(40,788,129),(40,801,2454),(40,804,402),(40,805,1438),(40,806,0),(40,816,7454),(40,817,0),(40,818,8),(40,819,6),(40,821,8),(40,822,0),(40,823,0),(40,824,9),(40,825,0),(40,826,3552),(40,828,298),(40,831,11),(40,833,4),(40,841,8650),(40,842,0),(40,856,0),(40,877,298),(40,878,988),(40,886,7746),(40,890,9483),(40,898,0),(40,901,1992),(40,902,0),(40,906,7900),(40,908,870),(40,910,2100),(40,916,0),(40,923,348),(40,942,102),(40,961,956),(40,975,4844),(40,977,0),(40,996,2764),(40,1000,0),(40,1002,0),(40,1005,278),(40,1007,3441),(40,1008,600),(40,1009,103),(40,1011,2353),(40,1028,0),(40,1044,2264),(40,1046,800),(40,1060,1945),(40,1064,0),(40,1066,102),(40,1067,465),(40,1072,4342),(40,1074,104),(40,1078,106),(40,1088,0),(40,1103,752),(40,1133,0),(40,1144,0),(40,1154,0),(40,1191,31),(40,1199,0),(40,1200,0),(40,1214,948),(40,1226,386),(40,1230,9924),(40,1231,916),(40,1234,0),(40,1252,0),(40,1258,0),(40,1260,0),(40,1276,4),(40,1280,0),(40,1292,0),(40,1295,0),(40,1305,0),(40,1307,0),(40,1311,106),(40,1318,0),(40,1321,2),(40,1324,750),(40,1331,164),(40,1360,0),(40,1367,100),(40,1395,934),(40,1402,838),(40,1407,0),(40,1443,0),(40,1450,0),(40,1474,6536),(40,1488,220),(40,1489,85),(40,1490,0),(40,1496,0),(40,1535,1828),(40,1547,23),(40,1549,169),(40,1590,0),(40,1598,0),(40,1599,9858),(40,1600,8750),(40,1601,0),(40,1602,0),(40,1603,0),(40,1621,0),(40,1644,652),(40,1655,280),(40,1656,779),(40,1665,5),(40,1685,52),(40,1706,198),(40,1708,54),(40,1722,698),(40,1730,324),(40,1784,214),(40,1785,2),(40,1788,0),(40,1790,0),(40,1831,0),(40,1832,0),(40,1833,30),(40,1834,0),(40,1838,181),(40,1842,224),(40,1843,757),(40,1845,0),(40,1858,0),(40,1869,116),(40,1890,908),(40,1918,80),(40,1933,612),(40,1942,66),(40,1943,0),(40,2013,0),(40,2044,0),(40,2045,0),(40,2047,0),(40,2113,0),(40,2115,0),(40,2121,0),(40,2124,0),(40,2125,0),(40,2133,0),(40,2156,470),(40,2157,0),(40,2191,194),(40,2192,0),(40,2246,0),(40,2251,460),(40,2253,0),(40,2297,7512),(40,2314,0),(40,2316,0),(40,2317,0),(40,2321,0),(40,2334,0),(40,2350,0),(40,2418,0),(40,2459,0),(40,2465,0),(40,2471,0),(40,2501,0),(40,2538,0),(40,2552,16),(40,2556,54),(40,2558,197),(40,2562,0),(40,2594,0),(40,2595,2448),(40,2597,30),(40,2598,55),(40,2599,0),(40,2607,0),(40,2608,0),(40,2651,14),(40,2652,2),(40,2653,25),(40,2655,7),(40,2657,25),(40,2660,700),(40,2662,0),(40,2664,0),(40,2674,266),(40,2683,0),(40,2689,0),(40,2691,23),(40,2693,0),(40,2694,4),(40,2696,0),(40,2702,0),(40,2717,0),(40,2734,9660),(40,2735,28),(40,2757,0),(40,2758,852),(40,2788,0),(40,2790,0),(40,2791,0),(40,2792,0),(40,2793,0),(40,2797,970),(40,2800,7),(40,2807,0),(40,2808,0),(40,2829,504),(40,2837,34),(40,2854,0),(40,2887,672),(40,2900,64),(40,2907,1552),(40,2917,2376),(40,2951,2332),(40,2953,0),(40,2998,2288),(40,3001,0),(40,3024,0),(40,3029,0),(40,3030,940),(40,3059,4),(40,3093,0),(40,3094,0),(40,3095,0),(40,3096,0),(40,3097,260),(40,3110,0),(40,3111,1),(40,3114,0),(40,3115,1740),(40,3116,0),(40,3119,0),(40,3124,0),(40,3125,440),(40,3158,240),(40,3159,640),(40,3160,480),(40,3165,0),(40,3187,0),(40,3252,0),(40,3386,111),(40,3703,290),(40,3709,0),(40,3715,0),(40,3717,4800),(40,3729,800),(40,3732,0),(40,3741,0),(40,3742,0),(40,3743,0),(40,3746,0),(40,3751,4),(40,3756,0),(40,3773,0),(40,3785,0),(40,3795,0),(40,3797,34),(40,3882,0),(40,3906,14),(40,3912,0),(40,4009,14),(40,4016,870),(40,4036,5),(40,4061,0),(40,4064,0),(40,4073,910),(40,4074,940),(40,4075,760),(40,4080,0),(40,4111,970),(40,4112,970),(40,4204,0),(48,566,0),(59,3,0),(59,4,0),(59,6,0),(59,7,0),(59,8,0),(59,9,0),(59,10,0),(59,11,0),(59,13,0),(59,14,0),(59,16,0),(59,19,0),(59,20,0),(59,21,0),(59,23,0),(59,25,0),(59,26,0),(59,27,0),(59,28,0),(59,30,0),(59,31,0),(59,32,0),(59,34,0),(59,36,0),(59,37,0),(59,38,0),(59,39,0),(59,40,0),(59,41,0),(59,42,0),(59,43,0),(59,44,0),(59,47,0),(59,48,0),(59,49,416),(59,50,0),(59,51,0),(59,53,0),(59,54,0),(59,55,0),(59,56,0),(59,58,0),(59,60,0),(59,61,0),(59,62,0),(59,63,0),(59,64,0),(59,65,0),(59,66,0),(59,67,0),(59,70,0),(59,71,132),(59,73,0),(59,75,0),(59,76,0),(59,78,0),(59,79,0),(59,80,0),(59,81,0),(59,83,0),(59,85,0),(59,87,0),(59,89,0),(59,91,0),(59,92,0),(59,94,0),(59,99,0),(59,100,0),(59,102,0),(59,105,0),(59,106,0),(59,107,0),(59,108,0),(59,109,0),(59,110,0),(59,111,0),(59,112,0),(59,114,0),(59,115,0),(59,118,0),(59,119,0),(59,120,0),(59,121,0),(59,123,0),(59,124,0),(59,125,0),(59,126,0),(59,127,0),(59,128,0),(59,130,0),(59,131,0),(59,133,0),(59,134,0),(59,136,0),(59,137,0),(59,139,0),(59,140,0),(59,141,0),(59,142,0),(59,143,0),(59,144,0),(59,145,0),(59,146,0),(59,149,0),(59,150,0),(59,151,0),(59,154,0),(59,156,0),(59,157,0),(59,158,0),(59,162,0),(59,163,0),(59,166,0),(59,169,0),(59,170,0),(59,172,0),(59,174,0),(59,175,0),(59,178,0),(59,179,0),(59,180,0),(59,182,0),(59,183,0),(59,184,0),(59,185,0),(59,186,0),(59,187,0),(59,188,0),(59,189,0),(59,191,0),(59,192,0),(59,193,0),(59,196,0),(59,197,0),(59,199,0),(59,200,0),(59,201,0),(59,202,0),(59,205,0),(59,206,0),(59,208,0),(59,210,0),(59,211,0),(59,212,0),(59,213,0),(59,215,0),(59,216,0),(59,217,0),(59,218,0),(59,219,0),(59,220,0),(59,221,0),(59,222,0),(59,237,0),(59,268,0),(59,271,0),(59,272,0),(59,274,0),(59,275,0),(59,276,0),(59,277,0),(59,278,0),(59,280,0),(59,282,0),(59,290,0),(59,291,0),(59,292,0),(59,293,0),(59,294,0),(59,299,0),(59,301,0),(59,304,0),(59,305,0),(59,306,0),(59,307,0),(59,310,0),(59,316,0),(59,318,0),(59,319,0),(59,320,0),(59,324,0),(59,325,0),(59,326,0),(59,327,0),(59,328,0),(59,329,0),(59,336,0),(59,337,0),(59,342,0),(59,343,0),(59,347,0),(59,350,0),(59,353,0),(59,355,0),(59,356,0),(59,358,0),(59,360,0),(59,361,0),(59,364,0),(59,365,0),(59,368,0),(59,369,0),(59,370,0),(59,372,0),(59,379,0),(59,381,0),(59,382,0),(59,384,976),(59,385,0),(59,386,0),(59,387,0),(59,391,0),(59,392,0),(59,393,0),(59,395,0),(59,404,0),(59,406,0),(59,408,0),(59,410,0),(59,413,0),(59,414,0),(59,426,0),(59,429,0),(59,430,0),(59,431,0),(59,432,0),(59,445,0),(59,451,0),(59,467,0),(59,469,0),(59,473,0),(59,480,0),(59,490,0),(59,499,0),(59,506,0),(59,508,0),(59,509,0),(59,511,0),(59,515,0),(59,518,0),(59,519,0),(59,521,0),(59,525,0),(59,528,0),(59,529,0),(59,531,0),(59,536,0),(59,537,0),(59,540,0),(59,545,0),(59,547,9972),(59,571,0),(59,572,0),(59,574,0),(59,579,0),(59,580,0),(59,581,0),(59,583,0),(59,584,0),(59,585,0),(59,586,0),(59,587,0),(59,588,0),(59,589,0),(59,596,0),(59,602,0),(59,603,0),(59,604,0),(59,605,0),(59,606,0),(59,607,0),(59,611,0),(59,612,0),(59,613,0),(59,614,0),(59,617,0),(59,618,0),(59,619,0),(59,620,0),(59,622,0),(59,624,0),(59,626,0),(59,627,100),(59,633,0),(59,652,0),(59,656,0),(59,663,0),(59,664,0),(59,665,0),(59,691,0),(59,692,0),(59,693,0),(59,694,0),(59,695,0),(59,696,0),(59,699,0),(59,700,0),(59,703,0),(59,706,0),(59,707,0),(59,708,0),(59,710,0),(59,712,0),(59,713,0),(59,719,0),(59,720,0),(59,721,0),(59,722,0),(59,723,0),(59,724,0),(59,725,50),(59,727,0),(59,728,0),(59,729,0),(59,730,0),(59,731,0),(59,732,0),(59,733,0),(59,735,50),(59,736,0),(59,737,0),(59,738,0),(59,739,0),(59,740,0),(59,741,0),(59,742,0),(59,743,0),(59,744,0),(59,745,0),(59,746,0),(59,747,0),(59,749,0),(59,760,0),(59,761,0),(59,763,0),(59,764,0),(59,765,3192),(59,766,0),(59,767,0),(59,769,0),(59,770,0),(59,771,0),(59,778,50),(59,783,0),(59,787,0),(59,788,0),(59,796,0),(59,801,0),(59,804,0),(59,805,0),(59,806,0),(59,816,0),(59,817,0),(59,818,0),(59,819,0),(59,821,0),(59,822,0),(59,823,0),(59,824,0),(59,825,0),(59,826,0),(59,828,0),(59,831,0),(59,833,0),(59,841,0),(59,842,0),(59,843,708),(59,854,978),(59,856,0),(59,860,931),(59,874,18),(59,877,0),(59,878,0),(59,886,0),(59,890,0),(59,898,0),(59,901,0),(59,902,0),(59,906,0),(59,908,0),(59,909,0),(59,910,0),(59,916,0),(59,923,0),(59,942,0),(59,961,0),(59,970,0),(59,977,0),(59,996,0),(59,1000,0),(59,1002,0),(59,1005,0),(59,1007,0),(59,1009,0),(59,1011,0),(59,1028,0),(59,1044,0),(59,1046,0),(59,1060,0),(59,1072,0),(59,1074,0),(59,1088,0),(59,1103,0),(59,1191,0),(59,1194,0),(59,1197,0),(59,1199,0),(59,1200,0),(59,1214,0),(59,1226,0),(59,1229,9980),(59,1230,0),(59,1231,0),(59,1234,0),(59,1247,0),(59,1252,0),(59,1258,0),(59,1260,0),(59,1280,0),(59,1295,0),(59,1303,0),(59,1305,0),(59,1307,0),(59,1310,0),(59,1311,0),(59,1321,0),(59,1324,0),(59,1331,0),(59,1377,0),(59,1402,0),(59,1443,0),(59,1457,9968),(59,1473,4976),(59,1474,0),(59,1488,0),(59,1489,0),(59,1513,0),(59,1535,0),(59,1547,0),(59,1549,0),(59,1600,700),(59,1603,0),(59,1607,0),(59,1620,0),(59,1621,0),(59,1644,0),(59,1656,0),(59,1665,0),(59,1666,0),(59,1667,0),(59,1668,0),(59,1670,0),(59,1671,0),(59,1678,0),(59,1685,0),(59,1706,0),(59,1708,0),(59,1726,1),(59,1727,0),(59,1730,0),(59,1784,0),(59,1785,0),(59,1788,0),(59,1790,0),(59,1831,0),(59,1832,0),(59,1833,0),(59,1838,0),(59,1842,0),(59,1843,0),(59,1845,0),(59,1864,0),(59,1869,0),(59,1911,0),(59,1918,0),(59,1928,0),(59,1933,0),(59,1942,0),(59,1943,0),(59,2007,0),(59,2013,0),(59,2015,0),(59,2019,0),(59,2025,0),(59,2026,0),(59,2043,0),(59,2044,0),(59,2045,0),(59,2047,0),(59,2048,0),(59,2049,0),(59,2051,0),(59,2113,0),(59,2115,0),(59,2121,0),(59,2124,0),(59,2125,0),(59,2129,0),(59,2133,0),(59,2142,80),(59,2151,0),(59,2153,0),(59,2156,0),(59,2157,0),(59,2191,0),(59,2192,0),(59,2246,0),(59,2251,0),(59,2252,101),(59,2253,0),(59,2254,0),(59,2297,0),(59,2314,0),(59,2316,0),(59,2317,0),(59,2318,0),(59,2319,0),(59,2321,0),(59,2334,0),(59,2350,0),(59,2419,6),(59,2420,0),(59,2421,6),(59,2465,0),(59,2471,0),(59,2497,0),(59,2501,0),(59,2538,0),(59,2541,0),(59,2542,0),(59,2552,0),(59,2553,0),(59,2554,0),(59,2557,0),(59,2562,0),(59,2581,0),(59,2594,0),(59,2597,0),(59,2598,0),(59,2599,0),(59,2607,0),(59,2608,0),(59,2634,398),(59,2651,0),(59,2653,0),(59,2655,0),(59,2657,0),(59,2659,8),(59,2660,0),(59,2662,0),(59,2664,0),(59,2674,0),(59,2679,0),(59,2692,0),(59,2696,0),(59,2701,0),(59,2702,0),(59,2734,0),(59,2745,0),(59,2757,0),(59,2758,0),(59,2789,0),(59,2791,0),(59,2792,0),(59,2793,0),(59,2807,0),(59,2808,0),(59,2829,0),(59,2837,0),(59,2854,0),(59,2868,0),(59,2887,0),(59,2907,0),(59,2909,0),(59,2917,0),(59,2949,0),(59,2951,0),(59,2973,4),(59,3015,0),(59,3093,0),(59,3094,0),(59,3095,0),(59,3096,0),(59,3110,0),(59,3111,0),(59,3112,0),(59,3113,0),(59,3114,0),(59,3115,0),(59,3116,0),(59,3119,0),(59,3125,0),(59,3158,0),(59,3159,0),(59,3160,0),(59,3189,478),(59,3314,100);
/*!40000 ALTER TABLE `companies_components` ENABLE KEYS */;
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