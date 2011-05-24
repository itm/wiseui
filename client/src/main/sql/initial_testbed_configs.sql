-- Load initial testbed configurations.
-- Optimized for HSQL
--
-- Date:   24.05.2011
-- Author: Soenke Nommensen

--
-- Table structure for table binary_image
--

DROP TABLE IF EXISTS binary_image;

CREATE TABLE binary_image 
(
  id int NOT NULL,
  content longvarbinary,
  contentType varchar(255) DEFAULT NULL,
  fileName varchar(255) DEFAULT NULL,
  fileSize bigint NOT NULL,
  PRIMARY KEY (id)
);

--
-- Table structure for table testbed_config
--

DROP TABLE IF EXISTS testbed_config;
CREATE TABLE testbed_config
(
  id int NOT NULL,
  isFederated boolean NOT NULL,
  name varchar(255) DEFAULT NULL,
  rsEndpointUrl varchar(255) DEFAULT NULL,
  sessionmanagementEndpointUrl varchar(255) DEFAULT NULL,
  snaaEndpointUrl varchar(255) DEFAULT NULL,
  testbedId int NOT NULL,
  testbedUrl varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

INSERT INTO testbed_config VALUES (1,1,'WISEBED Federator',NULL,'http://wisebed.itm.uni-luebeck.de:8881/sessions?wsdl','http://wisebed.itm.uni-luebeck.de:8883/snaa?wsdl',0,'http://www.wisebed.eu'),(2,0,'WISEBED UZL Testbed','http://wisebed.itm.uni-luebeck.de:8889/rs','http://wisebed.itm.uni-luebeck.de:8888/sessions?wsdl','http://wisebed.itm.uni-luebeck.de:8890/snaa?wsdl',0,'http://www.wisebed.eu'),(3,0,'WISEBED ULANC Testbed','http://wisebed-portal.lancs.ac.uk:3100','http://wisebed-portal.lancs.ac.uk:3000','http://wisebed-portal.lancs.ac.uk:3200/snaa',0,'http://www.wisebed.eu'),(4,0,'WISEBED CTI Testbed','http://hercules.cti.gr:8889/rs','http://hercules.cti.gr:8888/sessions','http://hercules.cti.gr:8890/snaa',0,'http://www.wisebed.eu'),(5,0,'WISEBED TUBS Testbed','http://wbportal.ibr.cs.tu-bs.de:8082/rs','http://wbportal.ibr.cs.tu-bs.de:8080/sessions','http://wbportal.ibr.cs.tu-bs.de:8083/snaa',0,'http://www.wisebed.eu'),(6,0,'WISEBED UBERN Testbed','http://gridlab23.unibe.ch:3100','http://gridlab23.unibe.ch:3001/','http://gridlab23.unibe.ch:3001/',0,'http://www.wisebed.eu'),(7,0,'WISEBED TUD Testbed','http://dutigw.st.ewi.tudelft.nl:3100','http://dutigw.st.ewi.tudelft.nl:3000','http://dutigw.st.ewi.tudelft.nl:3200/snaa',0,'http://www.wisebed.eu'),(8,0,'WISEBED UPC Testbed','http://wisebed2.lsi.upc.edu:8889/rs','http://wisebed2.lsi.upc.edu:8888/sessions','http://wisebed2.lsi.upc.edu:8890/snaa',0,'http://www.wisebed.eu'),(9,0,'WISEBED FUB Testbed','http://wiseportal.mi.fu-berlin.de:3100','http://wiseportal.mi.fu-berlin.de:3000','http://wiseportal.mi.fu-berlin.de:3000/snaa',0,'http://www.wisebed.eu'),(10,0,'WISEBED UNIGE Testbed','http://testbed.tcs.unige.ch:8889/rs','http://testbed.tcs.unige.ch:8888/sessions','http://testbed.tcs.unige.ch:8890/snaa',0,'http://www.wisebed.eu');

--
-- Table structure for table urn_prefix
--

DROP TABLE IF EXISTS urn_prefix;
CREATE TABLE urn_prefix 
(
  TestbedConfigurationBo_id int NOT NULL,
  urnPrefixList varchar(255) DEFAULT NULL
);

ALTER TABLE urn_prefix
    ADD CONSTRAINT FKB5D01920E050966E FOREIGN KEY (TestbedConfigurationBo_id)
    REFERENCES testbed_config (id);

INSERT INTO urn_prefix VALUES (2,'urn:wisebed:uzl1:'),(3,'urn:wisebed:ulanc:'),(4,'urn:wisebed:ctitestbed:'),(8,'urn:wisebed:upc1:'),(6,'urn:wisebed:ubern:'),(5,'urn:wisebed:tubs:'),(9,'urn:wisebed:fub:'),(10,'urn:wisebed:unigetestbed:'),(7,'urn:wisebed:tud:');

COMMIT;
