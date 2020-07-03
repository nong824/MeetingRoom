/*
SQLyog Ultimate v11.24 (64 bit)
MySQL - 5.7.16 : Database - meeting
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`meeting` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `meeting`;

/*Table structure for table `application_record` */

DROP TABLE IF EXISTS `application_record`;

CREATE TABLE `application_record` (
  `application_id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `apply_time` datetime NOT NULL,
  `audit_time` datetime DEFAULT NULL,
  `audit_status` int(1) NOT NULL DEFAULT '0',
  `reject_reason` varchar(200) DEFAULT NULL,
  `apply_date` date NOT NULL,
  `apply_slot` int(11) NOT NULL,
  `meeting_theme` varchar(200) NOT NULL,
  `meeting_size` int(11) DEFAULT NULL,
  PRIMARY KEY (`application_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Data for the table `application_record` */

insert  into `application_record`(`application_id`,`dept_id`,`room_id`,`apply_time`,`audit_time`,`audit_status`,`reject_reason`,`apply_date`,`apply_slot`,`meeting_theme`,`meeting_size`) values (8,1,4,'2020-06-28 19:39:37','2020-06-28 19:42:54',1,'','2020-06-28',3,'开展开学工作',30),(9,1,6,'2020-06-28 19:40:56','2020-06-28 19:42:58',1,'','2020-07-02',3,'关于原创项目启动的动员大会',60),(10,2,4,'2020-06-28 19:42:03','2020-06-28 19:43:14',2,'会议室已经被开发部预约了','2020-06-28',3,'关于此次记者会的场地布置策划',30),(11,4,1,'2020-06-28 19:42:41','2020-06-28 19:43:20',1,'','2020-06-30',2,'手游人物模型的设计会议',60);

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `dept_id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(200) NOT NULL,
  `dept_phone` varchar(11) NOT NULL,
  `dept_no` varchar(4) NOT NULL,
  `dept_password` varchar(11) NOT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `department` */

insert  into `department`(`dept_id`,`dept_name`,`dept_phone`,`dept_no`,`dept_password`) values (1,'开发部','13451678901','1001','232323'),(2,'策划部','13467299832','1002','454545'),(3,'美术部','15643529438','1003','787878'),(4,'宣传部','15634238374','1004','121212');

/*Table structure for table `manager` */

DROP TABLE IF EXISTS `manager`;

CREATE TABLE `manager` (
  `username` varchar(5) NOT NULL,
  `password` varchar(11) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `manager` */

insert  into `manager`(`username`,`password`) values ('admin','admin');

/*Table structure for table `meeting_record` */

DROP TABLE IF EXISTS `meeting_record`;

CREATE TABLE `meeting_record` (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `meeting_theme` varchar(200) NOT NULL,
  `meeting_size` int(11) NOT NULL,
  `dept_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `meeting_date` date NOT NULL,
  `meeting_slot` int(11) NOT NULL,
  `apply_id` int(11) NOT NULL,
  PRIMARY KEY (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `meeting_record` */

insert  into `meeting_record`(`record_id`,`meeting_theme`,`meeting_size`,`dept_id`,`room_id`,`meeting_date`,`meeting_slot`,`apply_id`) values (12,'开展开学工作',30,1,4,'2020-06-28',3,8),(13,'关于原创项目启动的动员大会',60,1,6,'2020-07-02',3,9),(14,'手游人物模型的设计会议',60,4,1,'2020-06-30',2,11);

/*Table structure for table `meeting_room` */

DROP TABLE IF EXISTS `meeting_room`;

CREATE TABLE `meeting_room` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_no` varchar(3) NOT NULL,
  `room_size` int(11) NOT NULL,
  `room_status` tinyint(1) NOT NULL DEFAULT '1',
  `air` tinyint(1) NOT NULL DEFAULT '1',
  `projector` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `meeting_room` */

insert  into `meeting_room`(`room_id`,`room_no`,`room_size`,`room_status`,`air`,`projector`) values (1,'301',60,1,1,1),(2,'302',30,0,1,1),(3,'303',30,1,0,1),(4,'304',30,1,1,1),(5,'305',60,1,1,0),(6,'306',60,1,1,1),(7,'401',30,1,1,1),(8,'402',30,1,1,1),(10,'307',60,1,1,1);

/*Table structure for table `status` */

DROP TABLE IF EXISTS `status`;

CREATE TABLE `status` (
  `room_id` int(11) NOT NULL AUTO_INCREMENT,
  `room_no` varchar(4) DEFAULT NULL,
  `one` tinyint(1) DEFAULT '1',
  `two` tinyint(1) DEFAULT '1',
  `three` tinyint(1) DEFAULT '1',
  `four` tinyint(1) DEFAULT '1',
  `five` tinyint(1) DEFAULT '1',
  `room_size` int(11) DEFAULT NULL,
  `air` tinyint(1) DEFAULT '1',
  `projector` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `status` */

insert  into `status`(`room_id`,`room_no`,`one`,`two`,`three`,`four`,`five`,`room_size`,`air`,`projector`) values (1,'301',1,1,1,1,1,60,1,1),(2,'302',0,0,0,0,0,30,1,1),(3,'303',1,1,1,1,1,30,0,1),(4,'304',1,1,1,1,1,30,1,1),(5,'305',1,1,1,1,1,60,1,0),(6,'306',1,1,1,1,1,60,1,1),(7,'401',1,1,1,1,1,30,1,1),(8,'402',1,1,1,1,1,30,1,1),(10,'307',1,1,1,1,1,60,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
