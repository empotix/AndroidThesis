/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50161
Source Host           : localhost:3306
Source Database       : fulda_thesis_db

Target Server Type    : MYSQL
Target Server Version : 50161
File Encoding         : 65001

Date: 2012-05-17 15:05:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `branchs`
-- ----------------------------
DROP TABLE IF EXISTS `branchs`;
CREATE TABLE `branchs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `brand_id` int(11) NOT NULL,
  `is_parkplace` tinyint(4) NOT NULL,
  `is_childcare` tinyint(4) NOT NULL,
  `is_disabled` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `to_brand` (`brand_id`),
  CONSTRAINT `to_brand` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of branchs
-- ----------------------------
INSERT INTO `branchs` VALUES ('1', 'McDonalds Taksim', 'adresss', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('2', 'Koton Kadikoy', 'address', '2', '1', '1', '1');
INSERT INTO `branchs` VALUES ('3', 'H&M Atasehir', 'address', '3', '1', '1', '1');
INSERT INTO `branchs` VALUES ('4', 'Media Markt Meydan', 'address', '4', '1', '1', '1');
INSERT INTO `branchs` VALUES ('5', 'McDonalds Sisli', 'address', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('6', 'McDonalds Besiktas', 'address', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('7', 'McDonalds Levent', 'address', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('8', 'McDonalds Maslak', 'addresa', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('9', 'McDonalds Kadikoy', 'address', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('10', 'McDonalds Suadiye', 'address', '1', '1', '1', '1');
INSERT INTO `branchs` VALUES ('11', 'Koton Taksim', 'address', '2', '1', '1', '1');
INSERT INTO `branchs` VALUES ('12', 'Koton Maslak', 'address', '2', '1', '1', '1');
INSERT INTO `branchs` VALUES ('13', 'Koton Besiktas', 'address', '2', '1', '1', '1');
INSERT INTO `branchs` VALUES ('14', 'H&M Besiktas', 'address', '3', '1', '1', '1');
INSERT INTO `branchs` VALUES ('15', 'H&M Levent', 'address', '3', '1', '1', '1');
INSERT INTO `branchs` VALUES ('16', 'MediaMarkt Levent', 'address', '4', '1', '1', '1');
INSERT INTO `branchs` VALUES ('17', 'MediaMarkt Maslak', 'address', '4', '1', '1', '1');

-- ----------------------------
-- Table structure for `brands`
-- ----------------------------
DROP TABLE IF EXISTS `brands`;
CREATE TABLE `brands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET latin1 NOT NULL,
  `phone` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of brands
-- ----------------------------
INSERT INTO `brands` VALUES ('1', 'McDonalds', '3432423', 'http://31.186.5.71:8080/StoredImages/mcdonalds.png');
INSERT INTO `brands` VALUES ('2', 'Koton', '42342', 'http://31.186.5.71:8080/StoredImages/koton.png');
INSERT INTO `brands` VALUES ('3', 'H&M', '423422', 'http://31.186.5.71:8080/StoredImages/hm.png');
INSERT INTO `brands` VALUES ('4', 'MediaMarkt', '4234242', 'http://31.186.5.71:8080/StoredImages/mediamarkt.png');

-- ----------------------------
-- Table structure for `coupons`
-- ----------------------------
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branch_id` int(11) NOT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `type` tinyint(4) NOT NULL,
  `range` int(11) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET latin1 NOT NULL,
  `description` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `start_datetime` datetime NOT NULL,
  `end_datetime` datetime NOT NULL,
  `status` tinyint(4) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `to_branch` (`branch_id`),
  CONSTRAINT `to_branch` FOREIGN KEY (`branch_id`) REFERENCES `branchs` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of coupons
-- ----------------------------
INSERT INTO `coupons` VALUES ('1', '1', '41.051784', '28.985109', '1', '10', 'CheesBurger+Cola 20%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-24 16:08:54', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('4', '15', '41.080775', '29.010086', '1', '10', 'Discount 50%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-23 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/dress1.jpg');
INSERT INTO `coupons` VALUES ('5', '13', '41.078288', '29.004849', '1', '10', 'Summer Season 20%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-23 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/dress2.jpg');
INSERT INTO `coupons` VALUES ('6', '4', '41.03042', '29.150848', '1', '10', 'Laptops + free printer', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-23 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/sony.jpg');
INSERT INTO `coupons` VALUES ('7', '7', '41.090785', '29.040086', '1', '10', 'Hamburger + Free coffee', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-23 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('8', '2', '41.003739', '29.040985', '1', '10', 'Summer Season discount 20%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-23 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/dress1.jpg');
INSERT INTO `coupons` VALUES ('9', '12', '41.119623', '29.002574', '1', '10', 'Discount 30%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-04-23 16:08:51', '2012-04-23 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/dress2.jpg');
INSERT INTO `coupons` VALUES ('10', '4', '41.024722', '29.126816', '1', '10', 'PS3 games 50%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-01 16:08:51', '2012-05-02 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/sony.jpg');
INSERT INTO `coupons` VALUES ('11', '11', '41.061764', '28.965119', '1', '10', 'Old Season 50%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-01 16:08:51', '2012-05-01 16:08:51', '1', 'http://31.186.5.71:8080/StoredImages/dress1.jpg');
INSERT INTO `coupons` VALUES ('15', '8', '41.109623', '29.032584', '1', '10', 'Free Ice Cream', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum ', '2012-05-17 13:38:59', '2012-05-29 13:39:03', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('16', '6', '41.058288', '29.004849', '1', '10', 'Free Ice Cream With Menu', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-15 16:58:00', '2012-05-15 16:58:00', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('17', '5', '41.043174', '28.990602', '1', '10', 'Free Ice Cream', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-15 16:58:10', '2012-05-15 16:58:10', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('18', '14', '41.048288', '29.024839', '1', '10', 'Discount for Mens', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-17 13:12:26', '2012-05-29 13:12:31', '1', 'http://31.186.5.71:8080/StoredImages/dress1.jpg');
INSERT INTO `coupons` VALUES ('19', '10', '40.974844', '29.055405', '1', '10', 'free ice cream', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-23 13:52:25', '2012-05-31 13:52:31', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('20', '9', '40.971949', '29.024677', '1', '10', 'free ice cream', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-28 13:53:53', '2012-05-31 13:53:59', '1', 'http://31.186.5.71:8080/StoredImages/hamburger.png');
INSERT INTO `coupons` VALUES ('21', '3', '40.998039', '29.128876', '1', '10', 'summer season 50%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-17 13:55:28', '2012-05-23 13:55:35', '1', 'http://31.186.5.71:8080/StoredImages/dress2.jpg');
INSERT INTO `coupons` VALUES ('22', '17', '41.109613', '29.032574', '1', '10', 'PS§ Games 50%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-18 13:57:11', '2012-05-21 13:57:16', '1', 'http://31.186.5.71:8080/StoredImages/sony.jpg');
INSERT INTO `coupons` VALUES ('23', '16', '41.080795', '29.010066', '1', '10', 'PS3 games 50%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-23 13:58:22', '2012-05-25 13:58:27', '1', 'http://31.186.5.71:8080/StoredImages/sony.jpg');
INSERT INTO `coupons` VALUES ('24', '14', '41.048258', '29.024879', '1', '10', 'Summer Season 20%', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi porta adipiscing mauris ac semper. Donec accumsan risus in velit tempor eget auctor ipsum luctus.', '2012-05-20 13:59:35', '2012-05-22 13:59:40', '1', 'http://31.186.5.71:8080/StoredImages/dress1.jpg');

-- ----------------------------
-- Table structure for `user_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `user_id` int(11) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `datetime` datetime NOT NULL,
  `code` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`coupon_id`),
  KEY `to_coupon` (`coupon_id`),
  CONSTRAINT `to_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),
  CONSTRAINT `to_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------
INSERT INTO `user_coupon` VALUES ('1', '5', '0', '2012-05-15 19:45:56', '0534bedc-ccb1-4715-9380-4127cee39092');
INSERT INTO `user_coupon` VALUES ('2', '1', '0', '2012-05-05 16:22:25', '1f44cb01-e3d3-47b3-80ec-2de16f823ad2');
INSERT INTO `user_coupon` VALUES ('2', '16', '0', '2012-05-15 20:33:13', '84ac72c3-b55a-4a7e-9f9e-7e3a70d4792b');

-- ----------------------------
-- Table structure for `user_favcoupon`
-- ----------------------------
DROP TABLE IF EXISTS `user_favcoupon`;
CREATE TABLE `user_favcoupon` (
  `user_id` int(11) NOT NULL,
  `coupon_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`coupon_id`),
  KEY `to_coupons` (`coupon_id`),
  CONSTRAINT `to_coupons` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`),
  CONSTRAINT `to_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_favcoupon
-- ----------------------------
INSERT INTO `user_favcoupon` VALUES ('1', '1');
INSERT INTO `user_favcoupon` VALUES ('2', '1');
INSERT INTO `user_favcoupon` VALUES ('2', '5');
INSERT INTO `user_favcoupon` VALUES ('2', '10');

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET latin1 NOT NULL,
  `surname` varchar(255) CHARACTER SET latin1 NOT NULL,
  `email` varchar(255) CHARACTER SET latin1 NOT NULL,
  `type` tinyint(4) NOT NULL,
  `lat` double DEFAULT NULL,
  `lng` double DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `token_id` varchar(255) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', 'emrah', 'dayioglu', 'test@test', '2', null, null, '1', 'XYZ123', '2012-04-25 11:37:08');
INSERT INTO `users` VALUES ('2', 'test', 'stest', 'test@test', '2', null, null, 'xxx', null, '2012-04-30 14:51:06');
INSERT INTO `users` VALUES ('3', 'dsda', 'dsda', 'dsda', '2', null, null, 'dsda', null, '2012-04-30 14:55:51');
