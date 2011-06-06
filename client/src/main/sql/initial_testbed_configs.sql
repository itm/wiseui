--
-- Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
--                             Research Academic Computer Technology Institute (RACTI)
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- MySQL dump 10.13  Distrib 5.5.10, for osx10.6 (i386)
--
-- Host: localhost    Database: wiseuidb
-- ------------------------------------------------------
-- Server version	5.5.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `binary_image`
--

DROP TABLE IF EXISTS `binary_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `binary_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` longblob,
  `contentType` varchar(255) DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `fileSize` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `binary_image`
--

LOCK TABLES `binary_image` WRITE;
/*!40000 ALTER TABLE `binary_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `binary_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `testbed_config`
--

DROP TABLE IF EXISTS `testbed_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `testbed_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `isFederated` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rsEndpointUrl` varchar(255) DEFAULT NULL,
  `sessionmanagementEndpointUrl` varchar(255) DEFAULT NULL,
  `snaaEndpointUrl` varchar(255) DEFAULT NULL,
  `testbedId` int(11) NOT NULL,
  `testbedUrl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `testbed_config`
--

LOCK TABLES `testbed_config` WRITE;
/*!40000 ALTER TABLE `testbed_config` DISABLE KEYS */;
INSERT INTO `testbed_config` VALUES (1,'','WISEBED Federator',NULL,'http://wisebed.itm.uni-luebeck.de:8881/sessions?wsdl','http://wisebed.itm.uni-luebeck.de:8883/snaa?wsdl',0,'http://www.wisebed.eu'),(2,'\0','WISEBED UZL Testbed','http://wisebed.itm.uni-luebeck.de:8889/rs','http://wisebed.itm.uni-luebeck.de:8888/sessions?wsdl','http://wisebed.itm.uni-luebeck.de:8890/snaa?wsdl',0,'http://www.wisebed.eu'),(3,'','WISEBED ULANC Testbed','http://wisebed-portal.lancs.ac.uk:3100','http://wisebed-portal.lancs.ac.uk:3000','http://wisebed-portal.lancs.ac.uk:3200/snaa',0,'http://www.wisebed.eu'),(4,'','WISEBED CTI Testbed','http://hercules.cti.gr:8889/rs','http://hercules.cti.gr:8888/sessions','http://hercules.cti.gr:8890/snaa',0,'http://www.wisebed.eu'),(5,'','WISEBED TUBS Testbed','http://wbportal.ibr.cs.tu-bs.de:8082/rs','http://wbportal.ibr.cs.tu-bs.de:8080/sessions','http://wbportal.ibr.cs.tu-bs.de:8083/snaa',0,'http://www.wisebed.eu'),(6,'','WISEBED UBERN Testbed','http://gridlab23.unibe.ch:3100','http://gridlab23.unibe.ch:3001/','http://gridlab23.unibe.ch:3001/',0,'http://www.wisebed.eu'),(7,'','WISEBED TUD Testbed','http://dutigw.st.ewi.tudelft.nl:3100','http://dutigw.st.ewi.tudelft.nl:3000','http://dutigw.st.ewi.tudelft.nl:3200/snaa',0,'http://www.wisebed.eu'),(8,'','WISEBED UPC Testbed','http://wisebed2.lsi.upc.edu:8889/rs','http://wisebed2.lsi.upc.edu:8888/sessions','http://wisebed2.lsi.upc.edu:8890/snaa',0,'http://www.wisebed.eu'),(9,'','WISEBED FUB Testbed','http://wiseportal.mi.fu-berlin.de:3100','http://wiseportal.mi.fu-berlin.de:3000','http://wiseportal.mi.fu-berlin.de:3000/snaa',0,'http://www.wisebed.eu'),(10,'','WISEBED UNIGE Testbed','http://testbed.tcs.unige.ch:8889/rs','http://testbed.tcs.unige.ch:8888/sessions','http://testbed.tcs.unige.ch:8890/snaa',0,'http://www.wisebed.eu');
/*!40000 ALTER TABLE `testbed_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `urn_prefix`
--

DROP TABLE IF EXISTS `urn_prefix`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `urn_prefix` (
  `TestbedConfigurationBo_id` int(11) NOT NULL,
  `urnPrefixList` varchar(255) DEFAULT NULL,
  KEY `FKB5D01920E050966E` (`TestbedConfigurationBo_id`),
  CONSTRAINT `FKB5D01920E050966E` FOREIGN KEY (`TestbedConfigurationBo_id`) REFERENCES `testbed_config` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `urn_prefix`
--

LOCK TABLES `urn_prefix` WRITE;
/*!40000 ALTER TABLE `urn_prefix` DISABLE KEYS */;
INSERT INTO `urn_prefix` VALUES (2,'urn:wisebed:uzl1:'),(3,'urn:wisebed:ulanc:'),(4,'urn:wisebed:ctitestbed:'),(8,'urn:wisebed:upc1:'),(6,'urn:wisebed:ubern:'),(5,'urn:wisebed:tubs:'),(9,'urn:wisebed:fub:'),(10,'urn:wisebed:unigetestbed:'),(7,'urn:wisebed:tud:');
/*!40000 ALTER TABLE `urn_prefix` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-05-10 11:39:20
