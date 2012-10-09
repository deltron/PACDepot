/* 
SQLyog v3.52
Host - www.pacdepot.com : Database - pacpublications
**************************************************************
Server version 4.0.12
*/

create database if not exists `pacpublications`;

/*
Table struture for Ads
*/

drop table if exists `Ads`;
CREATE TABLE `Ads` (
  `height` int(5) NOT NULL default '0',
  `width` int(5) NOT NULL default '0',
  `object` blob NOT NULL,
  `id` int(20) NOT NULL default '0',
  `contentType` varchar(64) NOT NULL default '',
  `hits` int(10) unsigned default '0',
  `href` varchar(128) NOT NULL default '',
  `sponsorid` int(11) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*
Table struture for Answers
*/

drop table if exists `Answers`;
CREATE TABLE `Answers` (
  `id` bigint(20) unsigned NOT NULL default '0',
  `questionid` bigint(20) unsigned default NULL,
  `itemid` bigint(20) unsigned default NULL,
  `userid` bigint(20) unsigned default NULL,
  `openanswer` text,
  `scalevalue` int(10) unsigned default NULL,
  `multichoice` varchar(72) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;

/*
Table struture for Banners
*/

drop table if exists `Banners`;
CREATE TABLE `Banners` (
  `id` int(20) NOT NULL default '0',
  `contentType` varchar(64) NOT NULL default '',
  `object` longblob NOT NULL,
  `width` int(5) NOT NULL default '0',
  `height` int(5) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*
Table struture for Brands
*/

drop table if exists `Brands`;
CREATE TABLE `Brands` (
  `id` int(20) NOT NULL default '0',
  `contentType` varchar(64) NOT NULL default '',
  `image` blob,
  `name` varchar(64) NOT NULL default '',
  `width` int(5) NOT NULL default '0',
  `height` int(5) NOT NULL default '0',
  `href` varchar(128) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*
Table data for pacpublications.Brands
*/

INSERT INTO `Brands` VALUES (1,'image/jpg','����','(non applicable)',2,2,'#') ;

/*
Table struture for Categories
*/

drop table if exists `Categories`;
CREATE TABLE `Categories` (
  `id` int(20) unsigned NOT NULL default '0',
  `name` char(64) NOT NULL default '0',
  `parentid` int(20) unsigned default '0',
  `template` char(10) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`,`parentid`)
) TYPE=MyISAM;

/*
Table data for pacpublications.Categories
*/

INSERT INTO `Categories` VALUES (1,'Services',0,'chronique') , (2,'Photos',0,'photo') , (3,'Emplois',0,'chronique') , (4,'Chroniques',0,'chronique') , (5,'Produits',0,'chronique') , (6,'Répertoire',0,'chronique') , (7,'Forfaits',0,NULL) , (8,'Divers',0,NULL) , (9,'Récits',0,'recits') , (10,'Foire aux questions',0,NULL) , (11,'Recettes',0,'recette') ;

/*
Table struture for Cities
*/

drop table if exists `Cities`;
CREATE TABLE `Cities` (
  `id` tinyint(20) unsigned NOT NULL default '0',
  `name` varchar(64) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*
Table data for pacpublications.Cities
*/

INSERT INTO `Cities` VALUES (1,'(aucun)') , (2,'Montréal') , (3,'Québec') , (4,'Longueuil') , (5,'Laval') , (6,'Gatineau') , (7,'Sherbrooke') , (8,'Trois-Rivières') , (9,'Saguenay') , (10,'Lévis') , (11,'Terrebonne') , (12,'Saint-Jean sur Richelieu') , (13,'Repentigny') , (14,'Saint-Jérôme') , (15,'Drummondville') , (16,'Granby') , (17,'Shawinigan') , (18,'Saint-Hyacinthe') , (19,'Rimouski') , (20,'Châteauguay') , (21,'Saint-Eustache') , (22,'Victoriaville') , (23,'Sorel-Tracy') , (24,'Rouyn Noranda') , (25,'Salaberry de Valleyfield') , (26,'Blainville') , (27,'Joliette') , (28,'Val d\'Or') , (29,'Alma') , (30,'Mirabel') , (31,'Mascouche') , (32,'Baie Comeau') , (33,'Saint-Georges') , (34,'Sainte-Julie') , (35,'Boisbriand') , (36,'Sept-Iles') , (37,'Thetford-Mines') , (38,'Sainte-Thérèse') , (39,'Saint-Constant') , (40,'Rivière du Loup') , (41,'Amos') , (42,'Maniwaki') ;

/*
Table struture for Icons
*/

drop table if exists `Icons`;
CREATE TABLE `Icons` (
  `id` int(20) NOT NULL default '0',
  `contentType` varchar(64) NOT NULL default '',
  `object` blob NOT NULL,
  `width` int(5) NOT NULL default '0',
  `height` int(5) NOT NULL default '0',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

/*
Table struture for Items
*/

drop table if exists `Items`;
CREATE TABLE `Items` (
  `id` int(60) NOT NULL default '0',
  `type` varchar(64) NOT NULL default '',
  `nature` varchar(64) default NULL,
  `title` varchar(72) NOT NULL default '',
  `description` text,
  `name` varchar(64) default NULL,
  `telephone` varchar(32) default NULL,
  `email` varchar(128) default NULL,
  `href` varchar(255) default NULL,
  `price` float default '0',
  `negociable` tinyint(1) unsigned default NULL,
  `quantity` int(10) default NULL,
  `ageInMonths` int(10) default NULL,
  `lifespan` int(5) default '0',
  `brandid` int(5) default NULL,
  `userbrand` varchar(64) default NULL,
  `hasImage` tinyint(1) unsigned NOT NULL default '0',
  `image` blob,
  `contentType` varchar(64) default NULL,
  `width` int(5) default NULL,
  `height` int(5) default NULL,
  `cityid` int(20) NOT NULL default '0',
  `categoryid` int(20) NOT NULL default '0',
  `sponsorid` int(20) NOT NULL default '0',
  `confirmed` tinyint(1) unsigned NOT NULL default '0',
  `date` date NOT NULL default '0000-00-00',
  `userid` bigint(20) unsigned default NULL,
  PRIMARY KEY  (`id`),
  KEY `SponsorId` (`sponsorid`),
  KEY `CategoryId` (`categoryid`),
  KEY `Confirmed` (`confirmed`),
  KEY `CityId` (`cityid`),
  KEY `userid` (`userid`)
) TYPE=MyISAM;

/*
Table struture for Questions
*/

drop table if exists `Questions`;
CREATE TABLE `Questions` (
  `id` bigint(20) unsigned NOT NULL default '0',
  `itemid` bigint(20) unsigned NOT NULL default '0',
  `sequence` tinyint(3) unsigned NOT NULL default '0',
  `title` varchar(72) default NULL,
  `description` text,
  `href` varchar(128) default NULL,
  `type` varchar(72) default NULL,
  `minLabel` varchar(16) default NULL,
  `maxLabel` varchar(16) default NULL,
  `choice1` varchar(132) default NULL,
  `choice2` varchar(132) default NULL,
  `choice3` varchar(132) default NULL,
  `choice4` varchar(132) default NULL,
  `choice5` varchar(132) default NULL,
  `choice6` varchar(132) default NULL,
  `choice7` varchar(132) default NULL,
  `choice8` varchar(132) default NULL,
  `contentType` varchar(64) default NULL,
  `image` longblob,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;

/*
Table struture for Registrationbitmaps
*/

drop table if exists `Registrationbitmaps`;
CREATE TABLE `Registrationbitmaps` (
  `id` int(10) unsigned NOT NULL default '0',
  `firstname` tinyint(1) unsigned default NULL,
  `lastname` tinyint(1) unsigned default NULL,
  `telephone` tinyint(1) unsigned default NULL,
  `address` tinyint(1) unsigned default NULL,
  `city` tinyint(1) unsigned default NULL,
  `postalcode` tinyint(1) unsigned default NULL,
  `country` tinyint(1) unsigned default NULL,
  `province` tinyint(1) unsigned default NULL,
  `email` tinyint(1) unsigned default NULL,
  `sex` tinyint(1) unsigned default NULL,
  `image` longblob,
  `contentType` varchar(72) default NULL,
  `description` text,
  `title` varchar(80) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `id_2` (`id`)
) TYPE=MyISAM;

/*
Table struture for Sponsors
*/

drop table if exists `Sponsors`;
CREATE TABLE `Sponsors` (
  `id` int(20) NOT NULL default '0',
  `name` varchar(80) NOT NULL default '',
  `username` varchar(16) NOT NULL default '',
  `href` varchar(128) NOT NULL default '',
  `homepage` text,
  `public` tinyint(1) default '0',
  `customTemplates` tinyint(1) unsigned default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`)
) TYPE=MyISAM;

/*
Table struture for Users
*/

drop table if exists `Users`;
CREATE TABLE `Users` (
  `id` bigint(20) unsigned NOT NULL default '0',
  `itemid` bigint(20) unsigned default NULL,
  `username` varchar(32) default NULL,
  `name` varchar(72) default NULL,
  `password` varchar(72) default NULL,
  `groupname` varchar(32) default NULL,
  `email` varchar(72) default NULL,
  `telephone` varchar(72) default NULL,
  `cityid` int(10) unsigned default NULL,
  `sponsorid` bigint(20) unsigned default NULL,
  `address` varchar(72) default NULL,
  `city` varchar(80) default NULL,
  `postalcode` varchar(8) default NULL,
  `country` varchar(80) default NULL,
  `province` varchar(80) default NULL,
  `sex` varchar(8) default NULL,
  `firstname` varchar(80) default NULL,
  `lastname` varchar(80) default NULL,
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

