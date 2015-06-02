DROP TABLE IF EXISTS `critic`;
DROP TABLE IF EXISTS `opinion`;
DROP TABLE IF EXISTS `critical_item`;

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
  `critic` varchar(45) DEFAULT NULL,
  `critical_item` varchar(45) DEFAULT NULL,
  `rating` varchar(45) DEFAULT NULL,
  `comments` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

CREATE TABLE `critical_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `expertise` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=latin1;

/*
LOCK TABLES `critic` WRITE;
LOCK TABLES `opinion` WRITE;
*/

INSERT INTO `critic` VALUES (1,'Mark Hotchkiss','mark.hotchkiss@gmail.com','Just a guy','Movies'), (2,'Wolf J. Flywheel','groucho@gmail.com','Attorney at Law','Movies');
INSERT INTO `opinion` VALUES (1,'Mark Hotchkiss','Seven Chances','9','Great stunts'), (2,'Mark Hotchkiss','One Week','8','Great short') ;
INSERT INTO `critical_item` VALUES (1,'Movies','Seven Chances','1925 Buster Keaton comedy'), (2,'Movies','One Week','1920 Buster Keaton comedy') ;
