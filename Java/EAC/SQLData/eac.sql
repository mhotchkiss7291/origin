
DROP TABLE IF EXISTS `critic`;
DROP TABLE IF EXISTS `opinion`;

CREATE TABLE `critic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `description` blob,
  `expertise` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

CREATE TABLE `opinion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `description` blob,
  `expertise` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

/*
LOCK TABLES `critic` WRITE;
LOCK TABLES `opinion` WRITE;
*/

INSERT INTO `critic` VALUES (1,'Mark Hotchkiss','mark.hotchkiss@gmail.com','Just a guy','Movies'), (2,'Wolf J. Flywheel','groucho@gmail.com','Attorney at Law','Movies');
INSERT INTO `opinion` VALUES (1,'Seven Chances','mark.hotchkiss@gmail.com','My favorite','Movies'), (2,'One Week','groucho@gmail.com','Another favorite','Movies');
