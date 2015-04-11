
DROP TABLE IF EXISTS `critic`;

CREATE TABLE `critic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `description` blob,
  `expertise` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

LOCK TABLES `critic` WRITE;
INSERT INTO `critic` VALUES (1,'Mark Hotchkiss','mark.hotchkiss@gmail.com','Just a guy','Movies'), (2,'Wolf J. Flywheel','groucho@gmail.com','Attorney at Law','Movies');