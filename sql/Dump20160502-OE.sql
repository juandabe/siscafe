-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 192.168.35.213    Database: schema_siscafe
-- ------------------------------------------------------
-- Server version	5.5.44-0+deb8u1

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
-- Dumping data for table `category_permits`
--

LOCK TABLES `category_permits` WRITE;
/*!40000 ALTER TABLE `category_permits` DISABLE KEYS */;
INSERT INTO `category_permits` VALUES (1,'Operaciones','2016-03-04 11:38:02','2016-03-04 11:38:02'),(2,'Administración','2016-03-04 11:38:02','2016-03-04 11:38:02'),(3,'Parametrización','2016-03-04 11:38:02','2016-03-04 11:38:02');
/*!40000 ALTER TABLE `category_permits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `city_source`
--

LOCK TABLES `city_source` WRITE;
/*!40000 ALTER TABLE `city_source` DISABLE KEYS */;
/*!40000 ALTER TABLE `city_source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
INSERT INTO `clients` VALUES (1,'FEDERACION NACIONAL DE CAFETEROS','ana.lopez@cafedecolombia.com',NULL,1,'34234','VALLE DEL CAUCA','2016-04-12 21:08:19','2016-04-12 21:14:10');
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `customs`
--

LOCK TABLES `customs` WRITE;
/*!40000 ALTER TABLE `customs` DISABLE KEYS */;
INSERT INTO `customs` VALUES (1,'ALMACAFE','Agencia de aduanas de FNC \nAlmacafe.','2016-04-18 19:36:04','2016-04-18 19:36:04'),(2,'ALMAGRARIO','Agencia de aduanas Almagrario','2016-04-18 19:36:37','2016-04-18 19:36:37');
/*!40000 ALTER TABLE `customs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `departaments`
--

LOCK TABLES `departaments` WRITE;
/*!40000 ALTER TABLE `departaments` DISABLE KEYS */;
INSERT INTO `departaments` VALUES (1,'Operaciones BUN','Sede COPCSA de operaciones BUN.','2016-04-09 19:27:46','2016-04-09 19:27:46'),(2,'Operaciones CTG','Sede COPCSA Cartagena Operaciones','2016-04-09 19:24:06','2016-04-09 19:24:06'),(3,'Facturación CTG','Departamento de facturacion COPCSA CTG','2016-04-11 17:17:49','2016-04-11 17:17:49');
/*!40000 ALTER TABLE `departaments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `fumigation_services`
--

LOCK TABLES `fumigation_services` WRITE;
/*!40000 ALTER TABLE `fumigation_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `fumigation_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `items_services`
--

LOCK TABLES `items_services` WRITE;
/*!40000 ALTER TABLE `items_services` DISABLE KEYS */;
INSERT INTO `items_services` VALUES (1,45,'MOVILIZACIÓN PALET CAFE','2016-04-18 21:04:41','2016-04-18 21:04:41');
/*!40000 ALTER TABLE `items_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `lines`
--

LOCK TABLES `lines` WRITE;
/*!40000 ALTER TABLE `lines` DISABLE KEYS */;
/*!40000 ALTER TABLE `lines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `mark_caffee`
--

LOCK TABLES `mark_caffee` WRITE;
/*!40000 ALTER TABLE `mark_caffee` DISABLE KEYS */;
INSERT INTO `mark_caffee` VALUES (1,'EXCELSO','Marca excelso colombiano.','2016-04-15 14:22:33','2016-04-15 14:22:33');
/*!40000 ALTER TABLE `mark_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `motor_ships`
--

LOCK TABLES `motor_ships` WRITE;
/*!40000 ALTER TABLE `motor_ships` DISABLE KEYS */;
/*!40000 ALTER TABLE `motor_ships` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `noveltys_caffee`
--

LOCK TABLES `noveltys_caffee` WRITE;
/*!40000 ALTER TABLE `noveltys_caffee` DISABLE KEYS */;
INSERT INTO `noveltys_caffee` VALUES (1,'RECHAZO POR CALIDAD','Rechazo de calidades de café.','2016-04-19 12:20:35','2016-04-19 12:20:35'),(2,'FUMIGACIÓN','Rechazo de café por fumigación.','2016-04-19 12:21:09','2016-04-19 12:21:09');
/*!40000 ALTER TABLE `noveltys_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `packaging_caffee`
--

LOCK TABLES `packaging_caffee` WRITE;
/*!40000 ALTER TABLE `packaging_caffee` DISABLE KEYS */;
/*!40000 ALTER TABLE `packaging_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `packing_caffee`
--

LOCK TABLES `packing_caffee` WRITE;
/*!40000 ALTER TABLE `packing_caffee` DISABLE KEYS */;
INSERT INTO `packing_caffee` VALUES (1,'Fique 795','Saco de café contruido\ncon fique.',0.795,'2016-04-20 18:38:44','2016-04-20 18:38:44'),(2,'Fique 659','Saco de café contruido\ncon fique.',0.659,'2016-04-20 18:39:00','2016-04-20 18:39:00');
/*!40000 ALTER TABLE `packing_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `permits`
--

LOCK TABLES `permits` WRITE;
/*!40000 ALTER TABLE `permits` DISABLE KEYS */;
INSERT INTO `permits` VALUES (1,'Usuarios','Gestión de usuarios','2016-03-04 11:38:02','2016-04-05 16:28:02',3),(2,'Permisos','Gestión de permisos.','2016-03-04 11:38:02','2016-04-05 16:28:06',3),(3,'Perfiles','Gestión de perfiles de acceso','2016-03-04 11:38:02','2016-04-05 16:28:11',3),(4,'Sedes','Gestión de areas organizativas','2016-03-04 11:38:02','2016-04-09 19:11:16',3),(5,'Sesiones','Gestión de sesiones de usuarios.','2016-03-04 11:38:02','2016-04-05 16:28:19',3),(6,'Lineas navieras','Gestión de Lineas maritimas.','2016-03-04 11:38:02','2016-04-15 21:11:52',3),(7,'Clientes','Gestión de clientes.','2016-03-04 11:38:02','2016-04-05 16:28:30',3),(8,'Items de servicios','Gestión de items de servicios.','2016-03-04 11:38:02','2016-04-07 13:45:59',3),(9,'Motonaves','Gestión de motonaves.','2016-03-04 11:38:02','2016-04-05 16:28:37',3),(10,'Unidades','Gestión de unidades de café','2016-03-04 11:38:02','2016-04-15 12:03:09',3),(11,'Bodegas','Gestión de bodegas de café','2016-03-04 11:38:02','2016-04-14 14:35:33',3),(12,'Operadores Portuarios','Gestión de operadores portuarios.','2016-03-04 11:38:02','2016-04-05 16:28:48',3),(13,'Tipo unidades','Gestion del tipo de unidades de café','2016-03-04 11:38:02','2016-04-15 10:37:30',3),(14,'Tipo Empaques','Gestión de empaque de café.','2016-03-04 11:38:02','2016-04-20 18:34:06',3),(15,'Empresas de transportes','Gestión de transportadores.','2016-03-04 11:38:02','2016-04-18 17:46:45',3),(16,'Ciudades origenes','Gestión de ciudades de origen','2016-03-04 11:38:02','2016-04-05 16:29:38',3),(17,'Marcas','Gestión de marcas de café','2016-03-04 11:38:02','2016-04-15 14:20:52',3),(18,'Novedades','Gestión de novedades de café.','2016-03-04 11:38:02','2016-04-19 12:18:59',3),(19,'Tipo de contenedores','Gestión de tipos de contenedores','2016-03-04 11:38:02','2016-04-05 16:29:52',3),(20,'Slots','Gestión de slot de almacenamiento','2016-03-04 11:38:02','2016-04-15 09:53:29',3),(21,'Servicios en bodega al Café','Gestión de servicios, conexos al Café','2016-03-04 11:38:02','2016-04-05 16:33:44',1),(22,'Fumigación al Café','Gestión de la fumigación del café','2016-03-04 11:38:02','2016-04-05 16:33:39',1),(23,'Ordenes de servicios','Gestión de ordenes de servicio.','2016-03-04 11:38:02','2016-04-04 18:30:39',1),(24,'Pesaje de Café','Gestión de pesaje en descargue y embalaje del café','2016-03-04 11:38:02','2016-03-04 11:38:02',1),(25,'Programación de Café','Gestión de la programación del embalaje de café','2016-03-04 11:38:02','2016-03-04 11:38:02',1),(26,'Devoluciones de Café','Gestión de las devoluciones de café','2016-03-04 11:38:02','2016-03-04 11:38:02',1),(27,'Remesas de Café','Gestión de remesas de café','2016-03-04 11:38:02','2016-04-05 16:20:53',1),(28,'Aduanas','Gestión de agencias de aduanas.','2016-04-18 19:35:05','2016-04-18 19:35:05',3),(29,'Radicar Café','Gestion radicados de café\ndescargado.','2016-04-20 17:45:31','2016-04-21 19:02:56',1);
/*!40000 ALTER TABLE `permits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `port_operators`
--

LOCK TABLES `port_operators` WRITE;
/*!40000 ALTER TABLE `port_operators` DISABLE KEYS */;
INSERT INTO `port_operators` VALUES (1,'COPC','Compañia Operadora Portuaria \nCafetera.','2016-04-15 15:23:26','2016-04-15 15:23:26'),(2,'TECSA','Terminal de contenedores \nSociedad Anonima.','2016-04-15 15:23:39','2016-04-15 15:23:39');
/*!40000 ALTER TABLE `port_operators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (1,'Administrador','Rol de usuario de acceso con privilegios de parametrización en la aplicación.','2016-03-04 11:38:02','2016-04-09 16:55:58'),(2,'Auxiliar Basculero','Rol de usuario de acceso para ejecutar pesos de café','2016-03-04 11:38:02','2016-04-09 16:30:55'),(3,'Administrador FA','Rol de usuario de acceso con permisos completos de uso de la aplicación.','2016-03-04 11:38:02','2016-04-20 18:34:35'),(4,'Auxiliar Operación','Rol de usuario de acceso con privilegios de gestion de servicios y ordenes de servicios, consultas d','2016-03-04 11:38:02','2016-04-09 16:46:37'),(5,'Motorista','Gestiona consulta de remesas\ne inventarios fisico de café.','2016-04-21 18:59:24','2016-04-21 18:59:24'),(6,'Muestreador','Gestiona consulta de remesas\ny papeletas de repesos.','2016-04-21 19:00:06','2016-04-21 19:00:06');
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `profiles_has_permits`
--

LOCK TABLES `profiles_has_permits` WRITE;
/*!40000 ALTER TABLE `profiles_has_permits` DISABLE KEYS */;
INSERT INTO `profiles_has_permits` VALUES (1,1),(3,1),(4,1),(1,2),(3,2),(4,2),(1,3),(3,3),(4,3),(1,4),(3,4),(1,5),(3,5),(1,6),(3,6),(4,6),(3,7),(4,7),(1,8),(3,8),(3,10),(3,11),(3,12),(3,13),(3,14),(3,15),(3,17),(3,18),(3,20),(2,24),(3,24),(3,25),(3,26),(3,27),(3,28),(3,29);
/*!40000 ALTER TABLE `profiles_has_permits` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `related_services`
--

LOCK TABLES `related_services` WRITE;
/*!40000 ALTER TABLE `related_services` DISABLE KEYS */;
/*!40000 ALTER TABLE `related_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `remittances_caffee`
--

LOCK TABLES `remittances_caffee` WRITE;
/*!40000 ALTER TABLE `remittances_caffee` DISABLE KEYS */;
INSERT INTO `remittances_caffee` VALUES (0000001,'3421',275,NULL,NULL,NULL,NULL,NULL,'2451435','CVG342',NULL,NULL,'2016-05-02 11:58:20','2016-05-02 11:58:20',1,'2346732',1,1,1,NULL,NULL,NULL,10,5,NULL,NULL,5,NULL,NULL),(1600000,'2345',235,NULL,NULL,NULL,NULL,NULL,'569033','VGH442',NULL,NULL,'2016-05-02 12:58:05','2016-05-02 12:58:05',1,'143412312',1,1,2,NULL,NULL,NULL,11,5,NULL,NULL,5,NULL,NULL),(1600001,'1123',225,NULL,NULL,NULL,NULL,NULL,'3563452','GTY231',NULL,NULL,'2016-05-02 13:02:13','2016-05-02 13:02:13',1,'234123',1,1,1,NULL,NULL,NULL,7,5,NULL,NULL,5,NULL,NULL),(1600002,'1111',245,NULL,NULL,NULL,NULL,NULL,'34354657','VFR432',NULL,NULL,'2016-05-02 13:32:00','2016-05-02 13:32:00',1,'23234545',1,2,1,NULL,NULL,NULL,9,5,NULL,NULL,5,NULL,NULL),(1600003,'2222',170,NULL,NULL,NULL,NULL,NULL,'8987632','CFD324',NULL,NULL,'2016-05-02 13:34:33','2016-05-02 13:34:33',1,'998877',1,2,1,NULL,NULL,NULL,12,5,NULL,NULL,5,NULL,NULL),(1600004,'2111',195,NULL,NULL,NULL,NULL,NULL,'45654','FFF332',NULL,NULL,'2016-05-02 13:36:05','2016-05-02 13:36:05',1,'77689',1,2,2,NULL,NULL,NULL,8,5,NULL,NULL,5,NULL,NULL),(1600005,'3333',234,NULL,NULL,NULL,NULL,NULL,'112233','VFT323',NULL,NULL,'2016-05-02 13:50:23','2016-05-02 13:50:23',1,'654323211',1,1,1,NULL,NULL,NULL,14,5,NULL,NULL,5,NULL,NULL),(1600006,'7766',178,NULL,NULL,NULL,NULL,NULL,'2211334','FDE321',NULL,NULL,'2016-05-02 14:16:40','2016-05-02 14:16:40',1,'432123',1,2,2,NULL,NULL,NULL,13,5,NULL,NULL,5,NULL,NULL);
/*!40000 ALTER TABLE `remittances_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `remittances_caffee_has_noveltys_caffee`
--

LOCK TABLES `remittances_caffee_has_noveltys_caffee` WRITE;
/*!40000 ALTER TABLE `remittances_caffee_has_noveltys_caffee` DISABLE KEYS */;
/*!40000 ALTER TABLE `remittances_caffee_has_noveltys_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `returns_caffees`
--

LOCK TABLES `returns_caffees` WRITE;
/*!40000 ALTER TABLE `returns_caffees` DISABLE KEYS */;
/*!40000 ALTER TABLE `returns_caffees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `services_orders`
--

LOCK TABLES `services_orders` WRITE;
/*!40000 ALTER TABLE `services_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `services_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `sessions`
--

LOCK TABLES `sessions` WRITE;
/*!40000 ALTER TABLE `sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `shippers`
--

LOCK TABLES `shippers` WRITE;
/*!40000 ALTER TABLE `shippers` DISABLE KEYS */;
INSERT INTO `shippers` VALUES (1,'EMPRESA TRANSPORTE RAPIDITO','2016-04-18 17:49:11','2016-04-18 17:49:11','EMPRESA CALEÑA DE LOGISTICA\nDE MERCANCIA.');
/*!40000 ALTER TABLE `shippers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `shipping_lines`
--

LOCK TABLES `shipping_lines` WRITE;
/*!40000 ALTER TABLE `shipping_lines` DISABLE KEYS */;
INSERT INTO `shipping_lines` VALUES (1,'Linea 1','Descripción Linea 1.','2016-04-15 21:12:42','2016-04-15 21:12:42'),(2,'Linea 2','Descripción Linea 2.','2016-04-18 15:44:59','2016-04-18 15:44:59');
/*!40000 ALTER TABLE `shipping_lines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `slot_store`
--

LOCK TABLES `slot_store` WRITE;
/*!40000 ALTER TABLE `slot_store` DISABLE KEYS */;
INSERT INTO `slot_store` VALUES (7,'A1-3',3),(8,'B1-3',3),(9,'C1-3',3),(10,'D1-2',1),(11,'B1-2',1),(12,'E4-3',3),(13,'J2-2',1),(14,'M2-3',3);
/*!40000 ALTER TABLE `slot_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `stores_caffee`
--

LOCK TABLES `stores_caffee` WRITE;
/*!40000 ALTER TABLE `stores_caffee` DISABLE KEYS */;
INSERT INTO `stores_caffee` VALUES (1,'Bodega 2','BUENAVENTURA','2016-04-14 14:49:57','2016-04-14 14:49:57'),(2,'Contecar','CARTAGENA','2016-04-14 14:51:18','2016-04-14 14:51:18'),(3,'Bodega 3','BUENAVENTURA','2016-04-14 14:52:20','2016-04-14 15:29:34');
/*!40000 ALTER TABLE `stores_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `type_container`
--

LOCK TABLES `type_container` WRITE;
/*!40000 ALTER TABLE `type_container` DISABLE KEYS */;
/*!40000 ALTER TABLE `type_container` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `type_units`
--

LOCK TABLES `type_units` WRITE;
/*!40000 ALTER TABLE `type_units` DISABLE KEYS */;
INSERT INTO `type_units` VALUES (1,'Saco Cafe 70','Cafe','Saco de cafe con un peso\nneto de 70 Kilogramos.','2016-04-21 20:10:21','2016-04-21 20:10:21'),(2,'Saco Cafe 35','Cafe','Saco con un peso neto\nde 35 Kilogramos.','2016-04-21 20:10:31','2016-04-21 20:10:31'),(3,'Saco Cacao 60','Cacao','Saco de cacao con un peso\nneto de 70 Kilogramos.','2016-04-21 20:10:39','2016-04-21 20:10:39');
/*!40000 ALTER TABLE `type_units` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `units_caffee`
--

LOCK TABLES `units_caffee` WRITE;
/*!40000 ALTER TABLE `units_caffee` DISABLE KEYS */;
INSERT INTO `units_caffee` VALUES (1,'Unidad Cafe 35',35,2,'2016-04-15 12:19:11','2016-04-15 12:19:11'),(2,'Unidad Cafe 70',70,1,'2016-04-21 14:22:19','2016-04-28 14:29:27');
/*!40000 ALTER TABLE `units_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Juan David','Echeverri Arbelaez','+0522417428','111','juandabe','202cb962ac59075b964b07152d234b70','2016-03-04 11:38:02','2016-04-12 21:04:29',3,1,1),(2,'Diego','Rios','2417128','117','drios','4d956e23cc710f83107dfa1e55e6cdc9','2016-04-11 12:17:09','2016-04-11 12:17:09',2,0,1),(4,'Danny','Mosquera','2417546','232','dmosquera','202cb962ac59075b964b07152d234b70','2016-04-12 19:08:53','2016-04-12 20:56:07',4,1,3),(5,'Luis','Parra','2456233','232','lparra','0330bd8bc1459518611f0fc317a5f55b','2016-04-21 19:17:10','2016-04-21 19:17:10',5,1,1),(6,'Carlos','Paternini','34567898','342','cpaternini','0330bd8bc1459518611f0fc317a5f55b','2016-04-21 19:17:51','2016-04-21 19:17:51',6,1,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `weighing_download_caffee`
--

LOCK TABLES `weighing_download_caffee` WRITE;
/*!40000 ALTER TABLE `weighing_download_caffee` DISABLE KEYS */;
/*!40000 ALTER TABLE `weighing_download_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `weighing_packaging_caffee`
--

LOCK TABLES `weighing_packaging_caffee` WRITE;
/*!40000 ALTER TABLE `weighing_packaging_caffee` DISABLE KEYS */;
/*!40000 ALTER TABLE `weighing_packaging_caffee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'schema_siscafe'
--

--
-- Dumping routines for database 'schema_siscafe'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-02 15:27:12
