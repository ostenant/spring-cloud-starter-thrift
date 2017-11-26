/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50623
Source Host           : localhost:3306
Source Database       : deposit

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2017-11-22 18:18:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bank
-- ----------------------------
DROP TABLE IF EXISTS `bank`;
CREATE TABLE `bank` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(32) DEFAULT NULL COMMENT '银行编码',
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL COMMENT '相关描述信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of bank
-- ----------------------------

-- ----------------------------
-- Table structure for branch
-- ----------------------------
DROP TABLE IF EXISTS `branch`;
CREATE TABLE `branch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(32) DEFAULT NULL COMMENT '银行分支编码',
  `name` varchar(255) DEFAULT NULL COMMENT '银行分支名称',
  `address` varchar(255) DEFAULT NULL COMMENT '分支银行地址',
  `staffs` int(255) DEFAULT NULL COMMENT '员工数量',
  `bank_id` bigint(20) DEFAULT NULL COMMENT '所属银行ID',
  `region_id` int(11) DEFAULT NULL COMMENT '分支银行所属区域(NORTH：1，CENTRAL：2，SOUTH：3，EAST：4，SOUTHWEST：5，NORTHWEST：6，NORTHEAST：7)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of branch
-- ----------------------------

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `birthday` varchar(255) DEFAULT NULL COMMENT '生日日期',
  `sex` int(11) DEFAULT NULL COMMENT '(0: 男，1：女)',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of customer
-- ----------------------------

-- ----------------------------
-- Table structure for deposit_card
-- ----------------------------
DROP TABLE IF EXISTS `deposit_card`;
CREATE TABLE `deposit_card` (
  `id` varchar(32) NOT NULL DEFAULT '' COMMENT '储蓄卡编号',
  `is_vip` bit(1) DEFAULT NULL COMMENT '是否是vip用户',
  `opening_time` varchar(255) DEFAULT NULL COMMENT '开户时间',
  `account_balance` double(64,0) DEFAULT NULL COMMENT '账户余额',
  `account_flow` double(64,0) DEFAULT NULL COMMENT '账户总流水(入账金额+出账金额)',
  `branch_id` bigint(20) DEFAULT NULL COMMENT '开户银行分支ID',
  `customer_id` varchar(32) DEFAULT NULL COMMENT '客户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of deposit_card
-- ----------------------------

-- ----------------------------
-- Table structure for deposit_history
-- ----------------------------
DROP TABLE IF EXISTS `deposit_history`;
CREATE TABLE `deposit_history` (
  `serial_number` varchar(32) NOT NULL COMMENT '存款流水号',
  `transaction_amount` double(255,0) DEFAULT NULL COMMENT '存款金额',
  `submitted_time` varchar(255) DEFAULT NULL COMMENT '存款提交时间',
  `finished_time` varchar(255) DEFAULT NULL COMMENT '存款完成时间',
  `status` int(11) DEFAULT NULL COMMENT '存款状态(1：已完成，2：进行中，3：失败)',
  `deposit_card_id` varchar(32) DEFAULT NULL COMMENT '储蓄卡编号',
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of deposit_history
-- ----------------------------

-- ----------------------------
-- Table structure for withdraw_history
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_history`;
CREATE TABLE `withdraw_history` (
  `serial_number` varchar(32) NOT NULL COMMENT '取款流水号',
  `transaction_amount` double(255,0) DEFAULT NULL COMMENT '取款金额',
  `submitted_time` varchar(255) DEFAULT NULL COMMENT '取款提交时间',
  `finished_time` varchar(255) DEFAULT NULL COMMENT '取款完成时间',
  `status` int(11) DEFAULT NULL COMMENT '取款状态(1：已完成，2：进行中，3：失败)',
  `deposit_card_id` varchar(32) DEFAULT NULL COMMENT '储蓄卡编号',
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of withdraw_history
-- ----------------------------
