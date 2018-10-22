/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50723
Source Host           : localhost:3306
Source Database       : remarksystem

Target Server Type    : MYSQL
Target Server Version : 50723
File Encoding         : 65001

Date: 2018-10-22 17:10:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_id` bigint(20) NOT NULL,
  `row_num` int(10) NOT NULL,
  `choice_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '答案状态:0:未审核,1正确,2错误',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for answer_data
-- ----------------------------
DROP TABLE IF EXISTS `answer_data`;
CREATE TABLE `answer_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `row_num` int(10) NOT NULL,
  `content` varchar(900) NOT NULL,
  `package_id` bigint(20) NOT NULL,
  `answer_header_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=749 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for answer_header
-- ----------------------------
DROP TABLE IF EXISTS `answer_header`;
CREATE TABLE `answer_header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `column_num` int(10) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for choice
-- ----------------------------
DROP TABLE IF EXISTS `choice`;
CREATE TABLE `choice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data
-- ----------------------------
DROP TABLE IF EXISTS `data`;
CREATE TABLE `data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `row_num` int(10) NOT NULL,
  `content` varchar(5000) NOT NULL,
  `answer_id` bigint(20) DEFAULT NULL,
  `package_id` bigint(20) DEFAULT NULL,
  `header_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id_index` (`project_id`) USING BTREE,
  KEY `package_id_index` (`package_id`),
  KEY `header_id_index` (`header_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13784 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for header
-- ----------------------------
DROP TABLE IF EXISTS `header`;
CREATE TABLE `header` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `column_num` int(10) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=259 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for package
-- ----------------------------
DROP TABLE IF EXISTS `package`;
CREATE TABLE `package` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `project_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '从抓取完成就进入开始标注状态.0:未完成,1:审核状态,2:验收状态,3:审核打回,4:验收打回,5:通过状态',
  `start_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `has_text` int(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否带有文本标注区,默认无文本标注区,0:没有,1:有',
  `words_num` int(10) NOT NULL DEFAULT '-1' COMMENT '字数限制,-1:没有字数限制',
  `data_num` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '数据总条数',
  `package_num` int(10) NOT NULL DEFAULT '200' COMMENT '指定一个数据包的数据条数',
  `check_num` int(10) NOT NULL DEFAULT '30' COMMENT '审核的条数',
  `status` int(4) unsigned NOT NULL DEFAULT '0' COMMENT '项目当前的状态,0:未启动,1:进行中,2:全部完成',
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `qq_num` varchar(255) NOT NULL,
  `phone_num` varchar(11) NOT NULL,
  `emp_num` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` int(10) NOT NULL DEFAULT '0' COMMENT '0:待审核,1:remark,2:inspector,3:examiner,4:admin',
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `empnum_index` (`emp_num`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_project
-- ----------------------------
DROP TABLE IF EXISTS `user_project`;
CREATE TABLE `user_project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8;
