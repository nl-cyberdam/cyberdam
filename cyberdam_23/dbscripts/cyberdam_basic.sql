-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 22, 2011 at 01:44 PM
-- Server version: 5.1.44
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `cyberdam`
--

-- --------------------------------------------------------

--
-- Table structure for table `activity`
--

CREATE TABLE IF NOT EXISTS `activity` (
  `actiontype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `instructions` text,
  `listindex` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `defaultMessageText` text,
  `defaultOption` int(11) DEFAULT NULL,
  `gameModel_id` bigint(20) NOT NULL,
  `script` text,
  `disabledScript` text,
  PRIMARY KEY (`id`),
  KEY `FK9D4BF30F1FE709B7` (`gameModel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `activity`
--


-- --------------------------------------------------------

--
-- Table structure for table `activityvariable`
--

CREATE TABLE IF NOT EXISTS `activityvariable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) DEFAULT NULL,
  `variable_id` bigint(20) DEFAULT NULL,
  `caption` varchar(255) NOT NULL,
  `mandatory` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `activity_id` (`activity_id`),
  KEY `variable_id` (`variable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `activityvariable`
--


-- --------------------------------------------------------

--
-- Table structure for table `activity_resource`
--

CREATE TABLE IF NOT EXISTS `activity_resource` (
  `activity_id` bigint(20) NOT NULL,
  `attachments_id` bigint(20) NOT NULL,
  `listindex` int(11) NOT NULL,
  PRIMARY KEY (`activity_id`,`listindex`),
  KEY `FK1ED4033E9852D03D` (`activity_id`),
  KEY `FK1ED4033E3D24F8FB` (`attachments_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `activity_resource`
--


-- --------------------------------------------------------

--
-- Table structure for table `activity_role`
--

CREATE TABLE IF NOT EXISTS `activity_role` (
  `activity_id` bigint(20) NOT NULL,
  `recipients_id` bigint(20) NOT NULL,
  KEY `FKC23CD7A6F67F4CD9` (`recipients_id`),
  KEY `FKC23CD7A6C0C9AE3E` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `activity_role`
--


-- --------------------------------------------------------

--
-- Table structure for table `cybgroup`
--

CREATE TABLE IF NOT EXISTS `cybgroup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `lastModified` date DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `lastModifiedBy_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDA023F53890DD832` (`lastModifiedBy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `cybgroup`
--


-- --------------------------------------------------------

--
-- Table structure for table `cybgroup_cybuser`
--

CREATE TABLE IF NOT EXISTS `cybgroup_cybuser` (
  `cybgroup_id` bigint(20) NOT NULL,
  `members_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cybgroup_id`,`members_id`),
  KEY `FK77321B8BD6F8FA03` (`cybgroup_id`),
  KEY `FK77321B8B862FA82F` (`members_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cybgroup_cybuser`
--


-- --------------------------------------------------------

--
-- Table structure for table `cybuser`
--

CREATE TABLE IF NOT EXISTS `cybuser` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `accountNonExpired` bit(1) NOT NULL,
  `accountNonLocked` bit(1) NOT NULL,
  `credentialsNonExpired` bit(1) NOT NULL,
  `notifyNewMessages` bit(1) NOT NULL,
  `defaultNotifyNewStepOfPlay` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `ROLE_GAMEAUTHOR` bit(1) DEFAULT NULL,
  `ROLE_GAMEMANIFESTCOMPOSER` bit(1) DEFAULT NULL,
  `ROLE_GAMESESSIONMASTER` bit(1) DEFAULT NULL,
  `ROLE_LCMSADMINISTRATOR` bit(1) DEFAULT NULL,
  `ROLE_LMSADMINISTRATOR` bit(1) DEFAULT NULL,
  `ROLE_PLAYGROUNDAUTHOR` bit(1) DEFAULT NULL,
  `ROLE_SYSTEMADMINISTRATOR` bit(1) DEFAULT NULL,
  `ROLE_USERADMINISTRATOR` bit(1) DEFAULT NULL,
  `ROLE_VLEADMINISTRATOR` bit(1) DEFAULT NULL,
  `lastLogin` datetime DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `locale` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `themeName` varchar(255) DEFAULT NULL,
  `username` varchar(12) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `cybuser`
--

INSERT INTO `cybuser` (`id`, `accountNonExpired`, `accountNonLocked`, `credentialsNonExpired`, `notifyNewMessages`, `defaultNotifyNewStepOfPlay`, `email`, `firstName`, `ROLE_GAMEAUTHOR`, `ROLE_GAMEMANIFESTCOMPOSER`, `ROLE_GAMESESSIONMASTER`, `ROLE_LCMSADMINISTRATOR`, `ROLE_LMSADMINISTRATOR`, `ROLE_PLAYGROUNDAUTHOR`, `ROLE_SYSTEMADMINISTRATOR`, `ROLE_USERADMINISTRATOR`, `ROLE_VLEADMINISTRATOR`, `lastLogin`, `lastName`, `locale`, `password`, `status`, `themeName`, `username`) VALUES
(1, '', '', '', '', '', 'naam@domein.nl', 'Systeem', '\0', '\0', '\0', '\0', '\0', '\0', '', '\0', '\0', '2009-06-17 17:14:44', 'Beheerder', 'nl', '8a90b249da0df7605976b526459f20b4', 1, NULL, 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `cybuser_cybgroup`
--

CREATE TABLE IF NOT EXISTS `cybuser_cybgroup` (
  `cybuser_id` bigint(20) NOT NULL,
  `groups_id` bigint(20) NOT NULL,
  PRIMARY KEY (`cybuser_id`,`groups_id`),
  KEY `FK80ACE1B9319C3E2` (`groups_id`),
  KEY `FK80ACE1B1E3F9411` (`cybuser_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cybuser_cybgroup`
--


-- --------------------------------------------------------

--
-- Table structure for table `gamemanifest`
--

CREATE TABLE IF NOT EXISTS `gamemanifest` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `lastModified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `gameModel_id` bigint(20) DEFAULT NULL,
  `lastModifier_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD2F68213933E055` (`owner_id`),
  KEY `FKD2F68212849343C` (`creator_id`),
  KEY `FKD2F68219903415B` (`lastModifier_id`),
  KEY `FKD2F68211FE709B7` (`gameModel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `gamemanifest`
--


-- --------------------------------------------------------

--
-- Table structure for table `gamemodel`
--

CREATE TABLE IF NOT EXISTS `gamemodel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `description` text,
  `lastModified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `lastModifier_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `initialStepOfPlay_id` bigint(20) DEFAULT NULL,
  `statusTemplate` text,
  `headerTemplate` text,
  `script` text,
  `sessionClassicLayout` bit(1) NOT NULL DEFAULT b'1',
  PRIMARY KEY (`id`),
  KEY `FK3C6F56773933E055` (`owner_id`),
  KEY `FK3C6F56772849343C` (`creator_id`),
  KEY `FK3C6F5677634BD5F9` (`initialStepOfPlay_id`),
  KEY `FK3C6F56779903415B` (`lastModifier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `gamemodel`
--


-- --------------------------------------------------------

--
-- Table structure for table `gamemodel_resource`
--

CREATE TABLE IF NOT EXISTS `gamemodel_resource` (
  `gamemodel_id` bigint(20) NOT NULL,
  `resources_id` bigint(20) NOT NULL,
  `listindex` int(11) NOT NULL,
  PRIMARY KEY (`gamemodel_id`,`listindex`),
  UNIQUE KEY `resources_id` (`resources_id`),
  KEY `FK7BA5F4D61FE709B7` (`gamemodel_id`),
  KEY `FK7BA5F4D6683F666` (`resources_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `gamemodel_resource`
--


-- --------------------------------------------------------

--
-- Table structure for table `gamesession`
--

CREATE TABLE IF NOT EXISTS `gamesession` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `currentStatusStarted` datetime DEFAULT NULL,
  `lastModified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `runningStarted` datetime DEFAULT NULL,
  `sessionStopped` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `lastModifier_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `currentStepOfPlay_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `manifest_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB2242643933E055` (`owner_id`),
  KEY `FKB224264F5C1EDE4` (`currentStepOfPlay_id`),
  KEY `FKB2242642849343C` (`creator_id`),
  KEY `FKB2242643C3CF0CF` (`manifest_id`),
  KEY `FKB2242649903415B` (`lastModifier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `gamesession`
--


-- --------------------------------------------------------

--
-- Table structure for table `gamesession_participant`
--

CREATE TABLE IF NOT EXISTS `gamesession_participant` (
  `gamesession_id` bigint(20) NOT NULL,
  `participants_id` bigint(20) NOT NULL,
  UNIQUE KEY `participants_id` (`participants_id`),
  KEY `FK63A819D8D5148357` (`gamesession_id`),
  KEY `FK63A819D81EDD098A` (`participants_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `gamesession_participant`
--


-- --------------------------------------------------------

--
-- Table structure for table `languagepack`
--

CREATE TABLE IF NOT EXISTS `languagepack` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `locale` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `locale` (`locale`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `languagepack`
--

INSERT INTO `languagepack` (`id`, `locale`, `name`) VALUES
(1, '', 'english'),
(2, 'nl', 'nederlands');

-- --------------------------------------------------------

--
-- Table structure for table `logentry`
--

CREATE TABLE IF NOT EXISTS `logentry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `action` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `module` varchar(255) DEFAULT NULL,
  `parameter` varchar(255) DEFAULT NULL,
  `userString` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `logentry`
--


-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `body` text NOT NULL,
  `sentDate` datetime DEFAULT NULL,
  `subject` varchar(255) NOT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  `stepOfPlay_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK38EB0007FB5E3EBD` (`stepOfPlay_id`),
  KEY `FK38EB0007EE9562D5` (`sender_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `message`
--


-- --------------------------------------------------------

--
-- Table structure for table `messagebox`
--

CREATE TABLE IF NOT EXISTS `messagebox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `messagebox`
--


-- --------------------------------------------------------

--
-- Table structure for table `messagebox_message`
--

CREATE TABLE IF NOT EXISTS `messagebox_message` (
  `messagebox_id` bigint(20) NOT NULL,
  `messages_id` bigint(20) NOT NULL,
  KEY `FKFD03AF2CC47847B2` (`messages_id`),
  KEY `FKFD03AF2CEA4AAF3D` (`messagebox_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `messagebox_message`
--


-- --------------------------------------------------------

--
-- Table structure for table `message_participant`
--

CREATE TABLE IF NOT EXISTS `message_participant` (
  `message_id` bigint(20) NOT NULL,
  `recipients_id` bigint(20) NOT NULL,
  KEY `FKBEAA7AFB93AE8B17` (`message_id`),
  KEY `FKBEAA7AFBD18A970` (`recipients_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `message_participant`
--


-- --------------------------------------------------------

--
-- Table structure for table `message_sessionresource`
--

CREATE TABLE IF NOT EXISTS `message_sessionresource` (
  `message_id` bigint(20) NOT NULL,
  `attachments_id` bigint(20) NOT NULL,
  PRIMARY KEY (`message_id`,`attachments_id`),
  KEY `FK9F24B86C93AE8B17` (`message_id`),
  KEY `FK9F24B86C995A8B` (`attachments_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `message_sessionresource`
--


-- --------------------------------------------------------

--
-- Table structure for table `multilanguagemessage`
--

CREATE TABLE IF NOT EXISTS `multilanguagemessage` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `basename` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `message` text,
  `languagePack` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`,`languagePack`),
  KEY `FK29236B5611C19E6` (`languagePack`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `multilanguagemessage`
--


-- --------------------------------------------------------

--
-- Table structure for table `nextstepofplay`
--

CREATE TABLE IF NOT EXISTS `nextstepofplay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `step_id` bigint(20) DEFAULT NULL,
  `PROGRESSACTIVITY_ID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9B14A8AA5E1F63D` (`PROGRESSACTIVITY_ID`),
  KEY `FK9B14A8ABAD11B28` (`step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `nextstepofplay`
--


-- --------------------------------------------------------

--
-- Table structure for table `participant`
--

CREATE TABLE IF NOT EXISTS `participant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notifyNewStepOfPlay` bit(1) NOT NULL,
  `gameSession_id` bigint(20) DEFAULT NULL,
  `trash_id` bigint(20) DEFAULT NULL,
  `roleAndPlayground_id` bigint(20) DEFAULT NULL,
  `outtrash_id` bigint(20) DEFAULT NULL,
  `sessionResourceBox_id` bigint(20) DEFAULT NULL,
  `outbox_id` bigint(20) DEFAULT NULL,
  `inbox_id` bigint(20) DEFAULT NULL,
  `sessionResourceTrash_id` bigint(20) DEFAULT NULL,
  `value1` text,
  `value2` text,
  `value3` text,
  `value4` text,
  `value5` text,
  PRIMARY KEY (`id`),
  KEY `FK2DBDEF33319CEDDB` (`inbox_id`),
  KEY `FK2DBDEF337A5F07E9` (`trash_id`),
  KEY `FK2DBDEF33D5148357` (`gameSession_id`),
  KEY `FK2DBDEF33877B57B0` (`sessionResourceTrash_id`),
  KEY `FK2DBDEF334FFB9FDD` (`roleAndPlayground_id`),
  KEY `FK2DBDEF33973C8D44` (`outbox_id`),
  KEY `FK2DBDEF33A0AB393D` (`sessionResourceBox_id`),
  KEY `FK2DBDEF33DE1B8937` (`outtrash_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `participant`
--


-- --------------------------------------------------------

--
-- Table structure for table `participant_activity`
--

CREATE TABLE IF NOT EXISTS `participant_activity` (
  `participant_id` bigint(20) NOT NULL,
  `completedActivities_id` bigint(20) NOT NULL,
  PRIMARY KEY (`participant_id`,`completedActivities_id`),
  KEY `FKD4D19E9B319C4E34` (`completedActivities_id`),
  KEY `FKD4D19E9BF68A8517` (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `participant_activity`
--


-- --------------------------------------------------------

--
-- Table structure for table `participant_activityavailable`
--

CREATE TABLE IF NOT EXISTS `participant_activityavailable` (
  `participant_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  PRIMARY KEY (`participant_id`,`activity_id`),
  KEY `activity_id` (`activity_id`),
  KEY `participant_id` (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `participant_activityavailable`
--


-- --------------------------------------------------------

--
-- Table structure for table `participant_cybuser`
--

CREATE TABLE IF NOT EXISTS `participant_cybuser` (
  `participant_id` bigint(20) NOT NULL,
  `users_id` bigint(20) NOT NULL,
  PRIMARY KEY (`participant_id`,`users_id`),
  KEY `FK63AFEB6BC63E08E0` (`users_id`),
  KEY `FK63AFEB6BF68A8517` (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `participant_cybuser`
--


-- --------------------------------------------------------

--
-- Table structure for table `playground`
--

CREATE TABLE IF NOT EXISTS `playground` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caption` varchar(255) DEFAULT NULL,
  `description` text,
  `lastModified` datetime DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `uriId` varchar(20) NOT NULL,
  `version` varchar(255) DEFAULT NULL,
  `lastModifier_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uriId` (`uriId`),
  KEY `FK80622DBB9903415B` (`lastModifier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `playground`
--


-- --------------------------------------------------------

--
-- Table structure for table `playgroundobject`
--

CREATE TABLE IF NOT EXISTS `playgroundobject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `description` text,
  `inDirectory` bit(1) NOT NULL,
  `lastModified` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `onMap` bit(1) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `x` int(11) NOT NULL,
  `y` int(11) NOT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `thumbnail_id` bigint(20) DEFAULT NULL,
  `picture_id` bigint(20) DEFAULT NULL,
  `lastModifier_id` bigint(20) DEFAULT NULL,
  `playground_fk` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE7A58BAC0163CED` (`picture_id`),
  KEY `FKE7A58BA3933E055` (`owner_id`),
  KEY `FKE7A58BA2849343C` (`creator_id`),
  KEY `FKE7A58BA9903415B` (`lastModifier_id`),
  KEY `FKE7A58BA81B9A3F` (`thumbnail_id`),
  KEY `FKE7A58BA341713A7` (`playground_fk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `playgroundobject`
--


-- --------------------------------------------------------

--
-- Table structure for table `resource`
--

CREATE TABLE IF NOT EXISTS `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` varchar(255) DEFAULT NULL,
  `content` mediumblob,
  `contentType` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `fileSize` int(11) NOT NULL,
  `lastModified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `lastModifier_id` bigint(20) DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEBABC40E2849343C` (`creator_id`),
  KEY `FKEBABC40E9903415B` (`lastModifier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resource`
--


-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `listindex` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `gameModel_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3580761FE709B7` (`gameModel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `role`
--


-- --------------------------------------------------------

--
-- Table structure for table `roleactivity`
--

CREATE TABLE IF NOT EXISTS `roleactivity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `activity_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK278C9D8528226C5D` (`role_id`),
  KEY `FK278C9D859852D03D` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `roleactivity`
--


-- --------------------------------------------------------

--
-- Table structure for table `roletoplaygroundmapping`
--

CREATE TABLE IF NOT EXISTS `roletoplaygroundmapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gameManifest_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `playgroundObject_id` bigint(20) DEFAULT NULL,
  `playground_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD391B96228226C5D` (`role_id`),
  KEY `FKD391B962CDB1C57D` (`playgroundObject_id`),
  KEY `FKD391B9628F6E199D` (`gameManifest_id`),
  KEY `FK_roletoplaygroundmapping_playground` (`playground_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `roletoplaygroundmapping`
--


-- --------------------------------------------------------

--
-- Table structure for table `sessionreportlogitem`
--

CREATE TABLE IF NOT EXISTS `sessionreportlogitem` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sessionId` bigint(20) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `stepOfPlay` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `actualName` varchar(255) DEFAULT NULL,
  `receiver` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `messageId` bigint(20) DEFAULT NULL,
  `resourceId` bigint(20) DEFAULT NULL,
  `activity_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `activity_id` (`activity_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `sessionreportlogitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `sessionresource`
--

CREATE TABLE IF NOT EXISTS `sessionresource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` mediumblob,
  `contentType` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `fileSize` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `stepOfPlay_id` bigint(20) DEFAULT NULL,
  `participant_id` bigint(20) DEFAULT NULL,
  `published` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FKB27350A4FB5E3EBD` (`stepOfPlay_id`),
  KEY `participant_id` (`participant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `sessionresource`
--


-- --------------------------------------------------------

--
-- Table structure for table `sessionresourcebox`
--

CREATE TABLE IF NOT EXISTS `sessionresourcebox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `sessionresourcebox`
--


-- --------------------------------------------------------

--
-- Table structure for table `sessionresourcebox_sessionresource`
--

CREATE TABLE IF NOT EXISTS `sessionresourcebox_sessionresource` (
  `sessionresourcebox_id` bigint(20) NOT NULL,
  `resources_id` bigint(20) NOT NULL,
  PRIMARY KEY (`sessionresourcebox_id`,`resources_id`),
  KEY `FK7F07330CA0AB393D` (`sessionresourcebox_id`),
  KEY `FK7F07330CC9F857F6` (`resources_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sessionresourcebox_sessionresource`
--


-- --------------------------------------------------------

--
-- Table structure for table `session_playgroundobject`
--

CREATE TABLE IF NOT EXISTS `session_playgroundobject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gamesession_id` bigint(20) NOT NULL,
  `playgroundobject_id` bigint(20) NOT NULL,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `gamesession_id_fkey` (`gamesession_id`),
  KEY `playgroundobject_id_fkey` (`playgroundobject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `session_playgroundobject`
--


-- --------------------------------------------------------

--
-- Table structure for table `stepofplay`
--

CREATE TABLE IF NOT EXISTS `stepofplay` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `listindex` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `gameModel_id` bigint(20) NOT NULL,
  `script` text,
  PRIMARY KEY (`id`),
  KEY `FKCFCC93571FE709B7` (`gameModel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `stepofplay`
--


-- --------------------------------------------------------

--
-- Table structure for table `stepofplay_roleactivity`
--

CREATE TABLE IF NOT EXISTS `stepofplay_roleactivity` (
  `stepofplay_id` bigint(20) NOT NULL,
  `roleActivities_id` bigint(20) NOT NULL,
  `listindex` int(11) NOT NULL,
  PRIMARY KEY (`stepofplay_id`,`listindex`),
  UNIQUE KEY `roleActivities_id` (`roleActivities_id`,`listindex`) USING BTREE,
  KEY `FK305D96DB03CC6FF` (`roleActivities_id`),
  KEY `FK305D96DFB5E3EBD` (`stepofplay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `stepofplay_roleactivity`
--


-- --------------------------------------------------------

--
-- Table structure for table `systemparameters`
--

CREATE TABLE IF NOT EXISTS `systemparameters` (
  `id` bigint(20) NOT NULL,
  `croninterval` int(11) NOT NULL,
  `defaultLanguageCode` varchar(255) DEFAULT NULL,
  `defaultRows` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `uploadSizeMaxBytes` bigint(20) NOT NULL,
  `vleActivities` int(11) NOT NULL,
  `vleFiles` int(11) NOT NULL,
  `vleMail` int(11) NOT NULL,
  `vleMessage` bit(1) NOT NULL,
  `vleStep` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `systemparameters`
--


-- --------------------------------------------------------

--
-- Table structure for table `variable`
--

CREATE TABLE IF NOT EXISTS `variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `initialValue` text,
  `gameModel_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `variable`
--


-- --------------------------------------------------------

--
-- Table structure for table `variable_session_value`
--

CREATE TABLE IF NOT EXISTS `variable_session_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gamesession_id` bigint(20) NOT NULL,
  `value` varchar(1024) DEFAULT NULL,
  `variable_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `gamesession_id` (`gamesession_id`),
  KEY `variable_id` (`variable_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `variable_session_value`
--


--
-- Constraints for dumped tables
--

--
-- Constraints for table `activity`
--
ALTER TABLE `activity`
  ADD CONSTRAINT `FK9D4BF30F1FE709B7` FOREIGN KEY (`gameModel_id`) REFERENCES `gamemodel` (`id`);

--
-- Constraints for table `activityvariable`
--
ALTER TABLE `activityvariable`
  ADD CONSTRAINT `activityvariable_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  ADD CONSTRAINT `activityvariable_ibfk_2` FOREIGN KEY (`variable_id`) REFERENCES `variable` (`id`);

--
-- Constraints for table `activity_resource`
--
ALTER TABLE `activity_resource`
  ADD CONSTRAINT `FK1ED4033E3D24F8FB` FOREIGN KEY (`attachments_id`) REFERENCES `resource` (`id`),
  ADD CONSTRAINT `FK1ED4033E9852D03D` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`);

--
-- Constraints for table `activity_role`
--
ALTER TABLE `activity_role`
  ADD CONSTRAINT `FKC23CD7A6C0C9AE3E` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  ADD CONSTRAINT `FKC23CD7A6F67F4CD9` FOREIGN KEY (`recipients_id`) REFERENCES `role` (`id`);

--
-- Constraints for table `cybgroup`
--
ALTER TABLE `cybgroup`
  ADD CONSTRAINT `FKDA023F53890DD832` FOREIGN KEY (`lastModifiedBy_id`) REFERENCES `cybuser` (`id`);

--
-- Constraints for table `cybgroup_cybuser`
--
ALTER TABLE `cybgroup_cybuser`
  ADD CONSTRAINT `FK77321B8B862FA82F` FOREIGN KEY (`members_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FK77321B8BD6F8FA03` FOREIGN KEY (`cybgroup_id`) REFERENCES `cybgroup` (`id`);

--
-- Constraints for table `cybuser_cybgroup`
--
ALTER TABLE `cybuser_cybgroup`
  ADD CONSTRAINT `FK80ACE1B1E3F9411` FOREIGN KEY (`cybuser_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FK80ACE1B9319C3E2` FOREIGN KEY (`groups_id`) REFERENCES `cybgroup` (`id`);

--
-- Constraints for table `gamemanifest`
--
ALTER TABLE `gamemanifest`
  ADD CONSTRAINT `FKD2F68211FE709B7` FOREIGN KEY (`gameModel_id`) REFERENCES `gamemodel` (`id`),
  ADD CONSTRAINT `FKD2F68212849343C` FOREIGN KEY (`creator_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKD2F68213933E055` FOREIGN KEY (`owner_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKD2F68219903415B` FOREIGN KEY (`lastModifier_id`) REFERENCES `cybuser` (`id`);

--
-- Constraints for table `gamemodel`
--
ALTER TABLE `gamemodel`
  ADD CONSTRAINT `FK3C6F56772849343C` FOREIGN KEY (`creator_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FK3C6F56773933E055` FOREIGN KEY (`owner_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FK3C6F5677634BD5F9` FOREIGN KEY (`initialStepOfPlay_id`) REFERENCES `stepofplay` (`id`),
  ADD CONSTRAINT `FK3C6F56779903415B` FOREIGN KEY (`lastModifier_id`) REFERENCES `cybuser` (`id`);

--
-- Constraints for table `gamemodel_resource`
--
ALTER TABLE `gamemodel_resource`
  ADD CONSTRAINT `FK7BA5F4D61FE709B7` FOREIGN KEY (`gamemodel_id`) REFERENCES `gamemodel` (`id`),
  ADD CONSTRAINT `FK7BA5F4D6683F666` FOREIGN KEY (`resources_id`) REFERENCES `resource` (`id`);

--
-- Constraints for table `gamesession`
--
ALTER TABLE `gamesession`
  ADD CONSTRAINT `FKB2242642849343C` FOREIGN KEY (`creator_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKB2242643933E055` FOREIGN KEY (`owner_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKB2242643C3CF0CF` FOREIGN KEY (`manifest_id`) REFERENCES `gamemanifest` (`id`),
  ADD CONSTRAINT `FKB2242649903415B` FOREIGN KEY (`lastModifier_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKB224264F5C1EDE4` FOREIGN KEY (`currentStepOfPlay_id`) REFERENCES `stepofplay` (`id`);

--
-- Constraints for table `gamesession_participant`
--
ALTER TABLE `gamesession_participant`
  ADD CONSTRAINT `FK63A819D81EDD098A` FOREIGN KEY (`participants_id`) REFERENCES `participant` (`id`),
  ADD CONSTRAINT `FK63A819D8D5148357` FOREIGN KEY (`gamesession_id`) REFERENCES `gamesession` (`id`);

--
-- Constraints for table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `FK38EB0007EE9562D5` FOREIGN KEY (`sender_id`) REFERENCES `participant` (`id`),
  ADD CONSTRAINT `FK38EB0007FB5E3EBD` FOREIGN KEY (`stepOfPlay_id`) REFERENCES `stepofplay` (`id`);

--
-- Constraints for table `messagebox_message`
--
ALTER TABLE `messagebox_message`
  ADD CONSTRAINT `FKFD03AF2CC47847B2` FOREIGN KEY (`messages_id`) REFERENCES `message` (`id`),
  ADD CONSTRAINT `FKFD03AF2CEA4AAF3D` FOREIGN KEY (`messagebox_id`) REFERENCES `messagebox` (`id`);

--
-- Constraints for table `message_participant`
--
ALTER TABLE `message_participant`
  ADD CONSTRAINT `FKBEAA7AFB93AE8B17` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`),
  ADD CONSTRAINT `FKBEAA7AFBD18A970` FOREIGN KEY (`recipients_id`) REFERENCES `participant` (`id`);

--
-- Constraints for table `message_sessionresource`
--
ALTER TABLE `message_sessionresource`
  ADD CONSTRAINT `FK9F24B86C93AE8B17` FOREIGN KEY (`message_id`) REFERENCES `message` (`id`),
  ADD CONSTRAINT `FK9F24B86C995A8B` FOREIGN KEY (`attachments_id`) REFERENCES `sessionresource` (`id`);

--
-- Constraints for table `multilanguagemessage`
--
ALTER TABLE `multilanguagemessage`
  ADD CONSTRAINT `FK29236B5611C19E6` FOREIGN KEY (`languagePack`) REFERENCES `languagepack` (`id`);

--
-- Constraints for table `nextstepofplay`
--
ALTER TABLE `nextstepofplay`
  ADD CONSTRAINT `FK9B14A8AA5E1F63D` FOREIGN KEY (`PROGRESSACTIVITY_ID`) REFERENCES `activity` (`id`),
  ADD CONSTRAINT `FK9B14A8ABAD11B28` FOREIGN KEY (`step_id`) REFERENCES `stepofplay` (`id`);

--
-- Constraints for table `participant`
--
ALTER TABLE `participant`
  ADD CONSTRAINT `FK2DBDEF33319CEDDB` FOREIGN KEY (`inbox_id`) REFERENCES `messagebox` (`id`),
  ADD CONSTRAINT `FK2DBDEF334FFB9FDD` FOREIGN KEY (`roleAndPlayground_id`) REFERENCES `roletoplaygroundmapping` (`id`),
  ADD CONSTRAINT `FK2DBDEF337A5F07E9` FOREIGN KEY (`trash_id`) REFERENCES `messagebox` (`id`),
  ADD CONSTRAINT `FK2DBDEF33877B57B0` FOREIGN KEY (`sessionResourceTrash_id`) REFERENCES `sessionresourcebox` (`id`),
  ADD CONSTRAINT `FK2DBDEF33973C8D44` FOREIGN KEY (`outbox_id`) REFERENCES `messagebox` (`id`),
  ADD CONSTRAINT `FK2DBDEF33A0AB393D` FOREIGN KEY (`sessionResourceBox_id`) REFERENCES `sessionresourcebox` (`id`),
  ADD CONSTRAINT `FK2DBDEF33D5148357` FOREIGN KEY (`gameSession_id`) REFERENCES `gamesession` (`id`),
  ADD CONSTRAINT `FK2DBDEF33DE1B8937` FOREIGN KEY (`outtrash_id`) REFERENCES `messagebox` (`id`);

--
-- Constraints for table `participant_activity`
--
ALTER TABLE `participant_activity`
  ADD CONSTRAINT `FKD4D19E9B319C4E34` FOREIGN KEY (`completedActivities_id`) REFERENCES `activity` (`id`),
  ADD CONSTRAINT `FKD4D19E9BF68A8517` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`);

--
-- Constraints for table `participant_activityavailable`
--
ALTER TABLE `participant_activityavailable`
  ADD CONSTRAINT `participant_activityavailable_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  ADD CONSTRAINT `participant_activityavailable_ibfk_2` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`);

--
-- Constraints for table `participant_cybuser`
--
ALTER TABLE `participant_cybuser`
  ADD CONSTRAINT `FK63AFEB6BC63E08E0` FOREIGN KEY (`users_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FK63AFEB6BF68A8517` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`);

--
-- Constraints for table `playground`
--
ALTER TABLE `playground`
  ADD CONSTRAINT `FK80622DBB9903415B` FOREIGN KEY (`lastModifier_id`) REFERENCES `cybuser` (`id`);

--
-- Constraints for table `playgroundobject`
--
ALTER TABLE `playgroundobject`
  ADD CONSTRAINT `FKE7A58BA2849343C` FOREIGN KEY (`creator_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKE7A58BA341713A7` FOREIGN KEY (`playground_fk`) REFERENCES `playground` (`id`),
  ADD CONSTRAINT `FKE7A58BA3933E055` FOREIGN KEY (`owner_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKE7A58BA81B9A3F` FOREIGN KEY (`thumbnail_id`) REFERENCES `resource` (`id`),
  ADD CONSTRAINT `FKE7A58BA9903415B` FOREIGN KEY (`lastModifier_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKE7A58BAC0163CED` FOREIGN KEY (`picture_id`) REFERENCES `resource` (`id`);

--
-- Constraints for table `resource`
--
ALTER TABLE `resource`
  ADD CONSTRAINT `FKEBABC40E2849343C` FOREIGN KEY (`creator_id`) REFERENCES `cybuser` (`id`),
  ADD CONSTRAINT `FKEBABC40E9903415B` FOREIGN KEY (`lastModifier_id`) REFERENCES `cybuser` (`id`);

--
-- Constraints for table `role`
--
ALTER TABLE `role`
  ADD CONSTRAINT `FK3580761FE709B7` FOREIGN KEY (`gameModel_id`) REFERENCES `gamemodel` (`id`);

--
-- Constraints for table `roleactivity`
--
ALTER TABLE `roleactivity`
  ADD CONSTRAINT `FK278C9D8528226C5D` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `FK278C9D859852D03D` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`);

--
-- Constraints for table `roletoplaygroundmapping`
--
ALTER TABLE `roletoplaygroundmapping`
  ADD CONSTRAINT `FKD391B96228226C5D` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `FKD391B9628F6E199D` FOREIGN KEY (`gameManifest_id`) REFERENCES `gamemanifest` (`id`),
  ADD CONSTRAINT `FKD391B962CDB1C57D` FOREIGN KEY (`playgroundObject_id`) REFERENCES `playgroundobject` (`id`),
  ADD CONSTRAINT `FK_roletoplaygroundmapping_playground` FOREIGN KEY (`playground_id`) REFERENCES `playground` (`id`);

--
-- Constraints for table `sessionreportlogitem`
--
ALTER TABLE `sessionreportlogitem`
  ADD CONSTRAINT `sessionreportlogitem_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  ADD CONSTRAINT `sessionreportlogitem_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`);

--
-- Constraints for table `sessionresource`
--
ALTER TABLE `sessionresource`
  ADD CONSTRAINT `FKB27350A4FB5E3EBD` FOREIGN KEY (`stepOfPlay_id`) REFERENCES `stepofplay` (`id`),
  ADD CONSTRAINT `sessionresource_ibfk_1` FOREIGN KEY (`participant_id`) REFERENCES `participant` (`id`);

--
-- Constraints for table `sessionresourcebox_sessionresource`
--
ALTER TABLE `sessionresourcebox_sessionresource`
  ADD CONSTRAINT `FK7F07330CA0AB393D` FOREIGN KEY (`sessionresourcebox_id`) REFERENCES `sessionresourcebox` (`id`),
  ADD CONSTRAINT `FK7F07330CC9F857F6` FOREIGN KEY (`resources_id`) REFERENCES `sessionresource` (`id`);

--
-- Constraints for table `session_playgroundobject`
--
ALTER TABLE `session_playgroundobject`
  ADD CONSTRAINT `gamesession_id_fkey` FOREIGN KEY (`gamesession_id`) REFERENCES `gamesession` (`id`),
  ADD CONSTRAINT `playgroundobject_id_fkey` FOREIGN KEY (`playgroundobject_id`) REFERENCES `playgroundobject` (`id`);

--
-- Constraints for table `stepofplay`
--
ALTER TABLE `stepofplay`
  ADD CONSTRAINT `FKCFCC93571FE709B7` FOREIGN KEY (`gameModel_id`) REFERENCES `gamemodel` (`id`);

--
-- Constraints for table `stepofplay_roleactivity`
--
ALTER TABLE `stepofplay_roleactivity`
  ADD CONSTRAINT `FK305D96DB03CC6FF` FOREIGN KEY (`roleActivities_id`) REFERENCES `roleactivity` (`id`),
  ADD CONSTRAINT `FK305D96DFB5E3EBD` FOREIGN KEY (`stepofplay_id`) REFERENCES `stepofplay` (`id`);

--
-- Constraints for table `variable_session_value`
--
ALTER TABLE `variable_session_value`
  ADD CONSTRAINT `variable_session_value_ibfk_3` FOREIGN KEY (`variable_id`) REFERENCES `variable` (`id`),
  ADD CONSTRAINT `variable_session_value_ibfk_1` FOREIGN KEY (`gamesession_id`) REFERENCES `gamesession` (`id`),
  ADD CONSTRAINT `variable_session_value_ibfk_2` FOREIGN KEY (`variable_id`) REFERENCES `variable` (`id`);
