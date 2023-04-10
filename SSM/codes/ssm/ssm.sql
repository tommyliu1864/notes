/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : ssm

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 10/04/2023 11:15:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept` (
  `dept_id` int NOT NULL AUTO_INCREMENT,
  `dept_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_dept
-- ----------------------------
BEGIN;
INSERT INTO `t_dept` (`dept_id`, `dept_name`) VALUES (1, '研发部');
INSERT INTO `t_dept` (`dept_id`, `dept_name`) VALUES (2, '财务部');
INSERT INTO `t_dept` (`dept_id`, `dept_name`) VALUES (3, '行政部');
COMMIT;

-- ----------------------------
-- Table structure for t_emp
-- ----------------------------
DROP TABLE IF EXISTS `t_emp`;
CREATE TABLE `t_emp` (
  `emp_id` int NOT NULL AUTO_INCREMENT,
  `emp_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  PRIMARY KEY (`emp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_emp
-- ----------------------------
BEGIN;
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (4, '赵四', 18, '男', NULL);
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (5, '尼古拉斯', 18, '男', NULL);
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (6, '赵四', 18, '男', NULL);
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (7, '尼古拉斯', 18, '男', NULL);
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (8, 'Jack', 19, '男', NULL);
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (9, 'Jack', 19, '男', NULL);
INSERT INTO `t_emp` (`emp_id`, `emp_name`, `age`, `gender`, `dept_id`) VALUES (10, '张三', 23, '男', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `age` int DEFAULT NULL,
  `gender` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` (`id`, `username`, `password`, `age`, `gender`, `email`) VALUES (1, '张三', '123456', 18, '男', '123@qq.com');
INSERT INTO `t_user` (`id`, `username`, `password`, `age`, `gender`, `email`) VALUES (2, '李四', '456789', 28, '女', '123@gmail.com');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
