/*
Navicat MySQL Data Transfer

Source Server         : mysql_local
Source Server Version : 50721
Source Host           : 127.0.0.1:3306
Source Database       : tmall

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-10-05 11:38:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_item
-- ----------------------------
DROP TABLE IF EXISTS `t_item`;
CREATE TABLE `t_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(50) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `t_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`id`),
  CONSTRAINT `t_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `t_product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_item
-- ----------------------------
INSERT INTO `t_item` VALUES ('33', '191005797305XWH0', '8001', '1');
INSERT INTO `t_item` VALUES ('34', '191005797305XWH0', '8005', '1');

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` varchar(50) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `total_amount` float(10,2) DEFAULT NULL,
  `actual_total_amount` float(10,2) DEFAULT NULL,
  `discount_by_credit` float(10,2) DEFAULT NULL,
  `payment_gateway` int(11) DEFAULT NULL,
  `payment_status` int(11) DEFAULT NULL,
  `order_status` int(11) DEFAULT NULL,
  `use_credit` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `t_order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES ('191005797305XWH0', '1002', '194.00', '194.00', null, null, null, null, null, '2019-10-05 10:16:16');

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `price` float(10,2) DEFAULT NULL,
  `exchange_credit` int(11) DEFAULT NULL,
  `exchange_count_limit` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `exchange_flag` tinyint(1) DEFAULT NULL,
  `img_url` varchar(200) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES ('8001', '天猫魔盒', '189.00', '99', '1', '499981', '1', 'images/tmmh.png', null);
INSERT INTO `t_product` VALUES ('8002', '明星同款太阳镜', '300.00', null, null, '43', '0', 'images/mxtktyj.png', null);
INSERT INTO `t_product` VALUES ('8003', 'tp-link路由器', '100.00', null, null, '60', '0', 'images/tplink.png', null);
INSERT INTO `t_product` VALUES ('8004', '云南白药牙膏', '10.00', null, null, '66', '0', 'images/ynbyyg.png', null);
INSERT INTO `t_product` VALUES ('8005', '康师傅红烧牛肉面', '5.00', null, null, '68', '0', 'images/ksfhsnrm.png', null);
INSERT INTO `t_product` VALUES ('8006', '小米音箱', '200.00', null, null, '90', '0', 'images/xiaomiaiyx.png', null);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `credit` int(11) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1001', 'test', 'e10adc3949ba59abbe56e057f20f883e', '60', '13312345678', '昆山市花桥镇创业路566号');
INSERT INTO `t_user` VALUES ('1002', 'scott', 'e10adc3949ba59abbe56e057f20f883e', '200', '17712345678', '上海市徐汇区虹桥路432号');
