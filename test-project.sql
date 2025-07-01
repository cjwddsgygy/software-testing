/*
 Navicat Premium Data Transfer

 Source Server         : www
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : localhost:3306
 Source Schema         : test-project

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 29/06/2025 16:00:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES (1, '王雨生', 'yushengwang', '123456', '2025-06-23 20:58:45', '2025-06-27 02:18:00');
INSERT INTO `admins` VALUES (2, '杨广宇', 'guangyuyang', '123456', '2025-06-29 11:24:42', '2025-06-29 11:24:45');
INSERT INTO `admins` VALUES (3, '朱子聪', 'zicongzhu', '123456', '2025-06-29 11:24:52', '2025-06-29 11:24:50');
INSERT INTO `admins` VALUES (4, '李通', 'tongli', '123456', '2025-06-29 11:25:21', '2025-06-29 11:25:23');
INSERT INTO `admins` VALUES (5, '丁思博', 'siboding', '123456', '2025-06-29 11:25:31', '2025-06-29 11:25:30');

-- ----------------------------
-- Table structure for beds
-- ----------------------------
DROP TABLE IF EXISTS `beds`;
CREATE TABLE `beds`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `elders_id` int NULL DEFAULT NULL COMMENT '老人编号',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '老人姓名',
  `room_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bed_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `elders_id`(`elders_id` ASC) USING BTREE,
  INDEX `name`(`name` ASC) USING BTREE,
  CONSTRAINT `elders_id` FOREIGN KEY (`elders_id`) REFERENCES `elders` (`elders_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `name` FOREIGN KEY (`name`) REFERENCES `elders` (`name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of beds
-- ----------------------------
INSERT INTO `beds` VALUES (1, 1, '王建国', '201', 'A101', '占用', '2025-06-23 20:47:13', '2025-06-28 08:27:14');
INSERT INTO `beds` VALUES (2, NULL, NULL, '101', 'A102', '空闲', '2025-06-23 20:47:13', '2025-06-28 08:37:00');
INSERT INTO `beds` VALUES (3, 3, '刘志强', '101', 'A103', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (4, 4, '陈美玲', '102', 'A104', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (5, 5, '赵文斌', '102', 'A105', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (6, 6, '孙丽娟', '102', 'A106', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (7, 7, '周志刚', '103', 'A107', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (8, 8, '黄秀英', '103', 'A108', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (9, 9, '吴建国', '103', 'A109', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (10, NULL, NULL, '104', 'A110', '空闲', '2025-06-23 20:47:13', '2025-06-28 09:58:28');
INSERT INTO `beds` VALUES (11, 11, '徐志远', '104', 'A111', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (12, 12, '张慧敏', '104', 'A112', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (13, 13, '马建国', '105', 'A113', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (14, 14, '朱小梅', '105', 'A114', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (15, 15, '宋建国', '105', 'A115', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (16, 16, '林秀兰', '106', 'A116', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (17, 17, '杨建国', '106', 'A117', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (18, 18, '何秀英', '106', 'A118', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (19, 19, '许建国', '107', 'A119', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (20, 20, '蒋秀兰', '107', 'A120', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (21, 21, '沈建国', '107', 'A121', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (22, 22, '韩秀兰', '108', 'A122', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (23, 23, '程建国', '108', 'A123', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (24, 24, '董秀英', '108', 'A124', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (25, 25, '梁建国', '109', 'A125', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (26, 26, '蔡秀兰', '109', 'A126', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (27, 27, '潘建国', '109', 'A127', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (28, 28, '秦秀兰', '110', 'A128', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (29, 29, '罗建国', '110', 'A129', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (30, 30, '朱明远', '110', 'A130', '占用', '2025-06-23 20:47:13', '2025-06-28 08:32:08');
INSERT INTO `beds` VALUES (34, NULL, NULL, '1', '1', '空闲', '2025-06-28 10:42:54', '2025-06-28 10:42:54');

-- ----------------------------
-- Table structure for care_workers
-- ----------------------------
DROP TABLE IF EXISTS `care_workers`;
CREATE TABLE `care_workers`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NOT NULL COMMENT '年龄',
  `birth_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ethnicity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态：在职、休假',
  `education` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `experience` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `specialties` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `assigned_elders` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of care_workers
-- ----------------------------
INSERT INTO `care_workers` VALUES (1, '张丽华', 35, '1990-04-16', '汉族', 'zhanglihua', '123456', '在职', '大专', '5年养老院经验', '康复训练,心理疏导', '王建国,李秀兰,刘志强', '2025-06-23 20:46:17', '2025-06-29 15:32:07');
INSERT INTO `care_workers` VALUES (2, '王强', 40, '1985-07-22', '汉族', 'worker_002', '123456', '1', '本科', '8年医院护理经验', '急救处理,用药指导', '陈美玲,赵文斌,孙丽娟', '2025-06-23 20:46:17', '2025-06-28 07:30:26');
INSERT INTO `care_workers` VALUES (3, '李娟', 32, '1993-02-10', '汉族', 'worker_003', '123456', '休假', '中专', '3年社区护理经验', '生活照料,饮食管理', '周志刚,黄秀英,吴建国', '2025-06-23 20:46:17', '2025-06-29 10:08:24');
INSERT INTO `care_workers` VALUES (4, '刘芳', 36, '1989-09-05', '汉族', 'worker_004', '123456', '1', '大专', '6年养老机构经验', '褥疮护理,康复按摩', '郑小红,许志远,张慧敏', '2025-06-23 20:46:17', '2025-06-28 07:30:26');
INSERT INTO `care_workers` VALUES (5, '赵磊', 38, '1987-01-18', '汉族', 'worker_005', '123456', '1', '本科', '7年老年科护理经验', '认知障碍照护,情绪安抚', '马建国,朱小梅,宋建国', '2025-06-23 20:46:17', '2025-06-28 07:30:25');
INSERT INTO `care_workers` VALUES (6, '周敏', 34, '1991-11-30', '汉族', 'worker_006', '123456', '1', '大专', '4年居家护理经验', '助浴,助餐服务', '林秀兰,杨建国,何秀英', '2025-06-23 20:46:17', '2025-06-28 07:30:24');
INSERT INTO `care_workers` VALUES (7, '黄勇', 41, '1984-06-25', '汉族', 'worker_007', '123456', '1', '高中', '10年一线护理经验', '术后康复,压疮处理', '许建国,蒋秀兰,沈建国', '2025-06-23 20:46:17', '2025-06-28 07:30:24');
INSERT INTO `care_workers` VALUES (8, '徐静', 33, '1992-03-12', '汉族', 'worker_008', '123456', '1', '中专', '2年养老院工作经验', '日常起居照顾,协助用药', '韩秀兰,程建国,董秀英', '2025-06-23 20:46:17', '2025-06-28 07:30:23');
INSERT INTO `care_workers` VALUES (9, '孙伟', 37, '1988-08-14', '汉族', 'worker_009', '123456', '1', '大专', '7年综合护理经验', '急救护理,营养配餐', '梁建国,蔡秀兰,潘建国', '2025-06-23 20:46:17', '2025-06-28 07:30:23');
INSERT INTO `care_workers` VALUES (10, '马丽娜', 34, '1991-03-25', '回族', 'worker_010', '123456', '1', '大专', '4年社区服务经验', '心理辅导,日常照料', '秦秀兰,罗建国,朱明远', '2025-06-23 20:46:17', '2025-06-28 07:30:22');
INSERT INTO `care_workers` VALUES (11, '陈晓东', 39, '1986-10-09', '汉族', 'worker_011', '123456', '1', '本科', '9年老年病护理经验', '认知训练,康复治疗', '王建国,李秀兰,刘志强', '2025-06-23 20:46:17', '2025-06-28 07:30:20');
INSERT INTO `care_workers` VALUES (12, '林芳', 35, '1990-07-17', '汉族', 'worker_012', '123456', '1', '大专', '5年临终关怀经验', '心理支持,安宁护理', '陈美玲,赵文斌,孙丽娟', '2025-06-23 20:46:17', '2025-06-28 07:30:19');
INSERT INTO `care_workers` VALUES (13, '吴建国', 42, '1983-12-04', '汉族', 'worker_013', '123456', '1', '高中', '12年护理经验', '慢性病管理,健康监测', '周志刚,黄秀英,吴建国', '2025-06-23 20:46:17', '2025-06-28 07:30:18');
INSERT INTO `care_workers` VALUES (14, '郑小红', 36, '1989-05-22', '汉族', 'worker_014', '123456', '1', '大专', '6年社区护理经验', '饮食指导,运动康复', '郑小红,许志远,张慧敏', '2025-06-23 20:46:17', '2025-06-28 07:30:18');
INSERT INTO `care_workers` VALUES (15, '许强', 38, '1987-02-19', '汉族', 'worker_015', '123456', '1', '本科', '8年老年护理经验', '失能老人护理,心理干预', '马建国,朱小梅,宋建国', '2025-06-23 20:46:17', '2025-06-28 07:30:17');
INSERT INTO `care_workers` VALUES (16, '蒋丽', 33, '1992-09-10', '汉族', 'worker_016', '123456', '1', '中专', '3年居家护理经验', '助行,助穿,助食', '林秀兰,杨建国,何秀英', '2025-06-23 20:46:17', '2025-06-28 07:30:17');
INSERT INTO `care_workers` VALUES (17, '沈刚', 40, '1985-04-08', '汉族', 'worker_017', '123456', '1', '大专', '10年护理经验', '术后恢复,体位管理', '许建国,蒋秀兰,沈建国', '2025-06-23 20:46:17', '2025-06-28 07:30:16');
INSERT INTO `care_workers` VALUES (18, '韩梅', 34, '1991-11-28', '汉族', 'worker_018', '123456', '1', '大专', '4年养老院护理经验', '心理陪伴,行为引导', '韩秀兰,程建国,董秀英', '2025-06-23 20:46:17', '2025-06-28 07:30:16');
INSERT INTO `care_workers` VALUES (19, '程伟', 37, '1988-06-15', '汉族', 'worker_019', '123456', '11', '本科', '7年综合护理经验', '药物管理,血压监测', '梁建国,蔡秀兰,潘建国', '2025-06-23 20:46:17', '2025-06-28 07:30:15');
INSERT INTO `care_workers` VALUES (20, '董芳', 35, '1990-01-20', '汉族', 'worker_020', '123456', '1', '大专', '5年社区护理经验', '心理疏导,认知训练', '秦秀兰,罗建国,朱明远', '2025-06-23 20:46:17', '2025-06-28 07:30:15');
INSERT INTO `care_workers` VALUES (21, '梁强', 39, '1986-03-11', '汉族', 'worker_021', '123456', '1', '本科', '9年老年护理经验', '认知障碍护理,情绪稳定', '王建国,李秀兰,刘志强', '2025-06-23 20:46:17', '2025-06-28 07:30:13');
INSERT INTO `care_workers` VALUES (22, '蔡丽', 34, '1991-08-06', '汉族', 'worker_022', '123456', '1', '大专', '4年养老院护理经验', '心理支持,行为干预', '陈美玲,赵文斌,孙丽娟', '2025-06-23 20:46:17', '2025-06-28 07:30:12');
INSERT INTO `care_workers` VALUES (23, '潘强', 36, '1989-10-12', '汉族', 'worker_023', '123456', '1', '大专', '6年社区护理经验', '日常生活辅助,认知训练', '周志刚,黄秀英,吴建国', '2025-06-23 20:46:17', '2025-06-28 07:30:13');
INSERT INTO `care_workers` VALUES (24, '秦芳', 33, '1992-04-29', '汉族', 'worker_024', '123456', '1', '中专', '3年护理经验', '基础护理,生活协助', '郑小红,许志远,张慧敏', '2025-06-23 20:46:17', '2025-06-28 07:30:11');
INSERT INTO `care_workers` VALUES (25, '罗伟', 38, '1987-07-13', '汉族', 'worker_025', '123456', '1', '本科', '8年老年护理经验', '认知训练,行为干预', '马建国,朱小梅,宋建国', '2025-06-23 20:46:17', '2025-06-28 07:30:12');
INSERT INTO `care_workers` VALUES (26, '朱芳', 35, '1990-02-14', '汉族', 'worker_026', '123456', '1', '大专', '5年养老院经验', '心理陪伴,康复护理', '林秀兰,杨建国,何秀英', '2025-06-23 20:46:17', '2025-06-28 07:30:11');
INSERT INTO `care_workers` VALUES (27, '宋强', 41, '1984-11-20', '汉族', 'worker_027', '123456', '1', '高中', '11年护理经验', '术后康复,慢性病管理', '许建国,蒋秀兰,沈建国', '2025-06-23 20:46:17', '2025-06-28 07:30:10');
INSERT INTO `care_workers` VALUES (28, '韩芳', 34, '1991-06-18', '汉族', 'worker_028', '123456', '1', '大专', '4年护理经验', '心理支持,行为引导', '韩秀兰,程建国,董秀英', '2025-06-23 20:46:17', '2025-06-28 07:30:09');
INSERT INTO `care_workers` VALUES (29, '程强', 37, '1988-09-05', '汉族', 'worker_029', '123456', '1', '本科', '7年老年护理经验', '认知训练,情绪稳定', '梁建国,蔡秀兰,潘建国', '2025-06-23 20:46:17', '2025-06-28 07:30:08');
INSERT INTO `care_workers` VALUES (30, '潘芳', 35, '1990-03-22', '汉族', 'panfang', '123456', '1', '大专', '5年护理经验', '心里陪伴,康复护理', '秦秀兰,罗建国,朱明远', '2025-06-24 10:41:45', '2025-06-28 07:30:08');
INSERT INTO `care_workers` VALUES (45, '1', 1, '2025-06-29', '1', 'guangyuyang', '123456', '在职', NULL, NULL, NULL, NULL, '2025-06-29 15:32:20', '2025-06-29 15:32:20');

-- ----------------------------
-- Table structure for elders
-- ----------------------------
DROP TABLE IF EXISTS `elders`;
CREATE TABLE `elders`  (
  `elders_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `age` int NOT NULL COMMENT '年龄',
  `birth_date` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ethnicity` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `education` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `marital_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hobbies` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `care_level` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `medical_care` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fee_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `expenses` decimal(38, 2) NULL DEFAULT NULL,
  `relative_contact` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `bed_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `check_in_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入院时间',
  `check_out_date` datetime NULL DEFAULT NULL COMMENT '出院时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`elders_id`) USING BTREE,
  UNIQUE INDEX `account`(`account` ASC) USING BTREE,
  INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of elders
-- ----------------------------
INSERT INTO `elders` VALUES (1, '王建国', 781, '1947-03-12', '汉族', 'wangjianguo', '123456', '高中', '已婚', '书法,听戏', '三级护理', '高血压', '全包', 3000.00, '李秀兰 13800138000', 'A101', '2025-02-01 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-29 05:23:45');
INSERT INTO `elders` VALUES (2, '李秀兰', 75, '1950-05-20', '汉族', 'elder_002', '123456', '初中', '丧偶', '广场舞,种花', '二级护理', '糖尿病', '月结', 2500.00, '王建国 13800138001', 'A102', '2025-03-16 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (3, '刘志强', 80, '1945-08-15', '汉族', 'elder_003', '123456', '本科', '已婚', '象棋,钓鱼', '一级护理', '心脏病', '全包', 3200.00, '刘晓芳 13800138002', 'A103', '2025-04-13 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (4, '陈美玲', 72, '1953-01-10', '汉族', 'elder_004', '123456', '大专', '离异', '唱歌,旅游', '三级护理', '关节炎', '月结', 2800.00, '陈大伟 13800138003', 'A104', '2025-04-27 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (5, '赵文斌', 82, '1943-11-05', '汉族', 'elder_005', '123456', '研究生', '丧偶', '读书,下棋', '二级护理', '哮喘', '全包', 3100.00, '赵小燕 13800138004', 'A105', '2025-04-14 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (6, '孙丽娟', 76, '1949-07-18', '汉族', 'elder_006', '123456', '高中', '已婚', '跳舞,插花', '三级护理', '骨质疏松', '月结', 2600.00, '孙建国 13800138005', 'A106', '2024-12-24 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (7, '周志刚', 79, '1946-09-23', '汉族', 'elder_007', '123456', '中专', '丧偶', '打太极,看书', '一级护理', '脑梗后遗症', '全包', 3500.00, '周婷婷 13800138006', 'A107', '2024-07-29 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (8, '黄秀英', 74, '1951-04-14', '汉族', 'elder_008', '123456', '初中', '已婚', '织毛衣,看剧', '二级护理', '白内障', '月结', 2700.00, '黄志强 13800138007', 'A108', '2025-06-15 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (9, '吴建国', 81, '1944-12-01', '汉族', 'elder_009', '123456', '小学', '丧偶', '听广播,晒太阳', '三级护理', '肺气肿', '全包', 2900.00, '吴红梅 13800138008', 'A109', '2025-01-30 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (10, '郑小红', 73, '1952-06-30', '汉族', 'elder_010', '123456', '高中', '离异', '养鸟,听音乐', '二级护理', '冠心病', '月结', 2600.00, '郑建国 13800138009', 'A110', '2024-07-26 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (11, '徐志远', 77, '1948-01-25', '汉族', 'elder_011', '123456', '本科', '已婚', '摄影,写作', '二级护理', '高血脂', '全包', 3100.00, '徐小雨 13800138010', 'A111', '2025-02-09 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (12, '张慧敏', 75, '1950-08-12', '汉族', 'elder_012', '123456', '大专', '丧偶', '跳舞,旅游', '三级护理', '风湿性关节炎', '月结', 2700.00, '张建国 13800138011', 'A112', '2025-05-16 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (13, '马建国', 79, '1946-03-08', '回族', 'elder_013', '123456', '初中', '已婚', '听评书,喝茶', '一级护理', '慢性阻塞性肺病', '全包', 3400.00, '马云霞 13800138012', 'A113', '2025-01-21 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (14, '朱小梅', 76, '1949-11-17', '汉族', 'elder_014', '123456', '高中', '离异', '手工,插画', '二级护理', '轻度认知障碍', '月结', 2800.00, '朱建国 13800138013', 'A114', '2024-09-11 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (15, '宋建国', 80, '1945-09-22', '汉族', 'elder_015', '123456', '大专', '丧偶', '听收音机,散步', '三级护理', '老年痴呆早期', '全包', 3000.00, '宋小芳 13800138014', 'A115', '2024-10-31 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (16, '林秀兰', 74, '1951-02-14', '汉族', 'elder_016', '123456', '初中', '已婚', '种菜,做饭', '二级护理', '轻度贫血', '月结', 2600.00, '林建国 13800138015', 'A116', '2024-08-09 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (17, '杨建国', 81, '1944-07-05', '汉族', 'elder_017', '123456', '高中', '丧偶', '下棋,看电视', '一级护理', '脑出血术后', '全包', 3600.00, '杨小红 13800138016', 'A117', '2025-01-16 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (18, '何秀英', 75, '1950-04-19', '汉族', 'elder_018', '123456', '中专', '离异', '听戏,做家务', '三级护理', '腰椎间盘突出', '月结', 2700.00, '何志强 13800138017', 'A118', '2024-12-07 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (19, '许建国', 77, '1948-10-11', '汉族', 'elder_019', '123456', '大专', '已婚', '书法,收藏邮票', '二级护理', '高尿酸', '全包', 3000.00, '许小红 13800138018', 'A119', '2025-01-24 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (20, '蒋秀兰', 76, '1949-06-23', '汉族', 'elder_020', '123456', '高中', '丧偶', '织毛衣,跳操', '三级护理', '轻度抑郁', '月结', 2800.00, '蒋建国 13800138019', 'A120', '2025-01-18 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (21, '沈建国', 78, '1947-02-14', '汉族', 'elder_021', '123456', '本科', '已婚', '阅读,写日记', '二级护理', '轻度阿尔茨海默症', '全包', 3100.00, '沈小红 13800138020', 'A121', '2024-07-29 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (22, '韩秀兰', 73, '1952-09-05', '汉族', 'elder_022', '123456', '初中', '离异', '养鱼,听音乐', '三级护理', '前列腺肥大', '月结', 2600.00, '韩建国 13800138021', 'A122', '2025-04-02 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (23, '程建国', 82, '1943-11-20', '汉族', 'elder_023', '123456', '高中', '丧偶', '听新闻,打太极', '一级护理', '帕金森综合征', '全包', 3500.00, '程小红 13800138022', 'A123', '2025-01-22 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (24, '董秀英', 75, '1950-01-18', '汉族', 'elder_024', '123456', '大专', '已婚', '绘画,写字', '二级护理', '白内障术后', '月结', 2700.00, '董建国 13800138023', 'A124', '2025-01-24 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (25, '梁建国', 79, '1946-08-12', '汉族', 'elder_025', '123456', '中专', '丧偶', '打牌,看电视', '三级护理', '膝关节退行性病变', '全包', 2900.00, '梁小红 13800138024', 'A125', '2024-09-04 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (26, '蔡秀兰', 74, '1951-05-30', '汉族', 'elder_026', '123456', '高中', '离异', '听歌,做手工', '二级护理', '轻度失智', '月结', 2600.00, '蔡建国 13800138025', 'A126', '2024-09-15 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (27, '潘建国', 77, '1948-03-14', '汉族', 'elder_027', '123456', '本科', '已婚', '摄影,旅行', '三级护理', '高胆固醇血症', '全包', 3100.00, '潘小红 13800138026', 'A127', '2025-01-10 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (28, '秦秀兰', 76, '1949-09-05', '汉族', 'elder_028', '123456', '大专', '丧偶', '跳广场舞,做家务', '二级护理', '轻度焦虑', '月结', 2800.00, '秦建国 13800138027', 'A128', '2024-07-18 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (29, '罗建国', 80, '1945-12-25', '汉族', 'elder_029', '123456', '高中', '已婚', '听广播,看报纸', '一级护理', '脑萎缩', '全包', 3400.00, '罗小红 13800138028', 'A129', '2025-03-04 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (30, '朱明远', 77, '1948-02-11', '汉族', 'elder_030', '123456', '本科', '已婚', '摄影,写作', '二级护理', '高血脂', '全包', 3100.00, '朱小雨 13800138029', 'A130', '2024-09-29 20:37:40', NULL, '2025-06-23 20:37:40', '2025-06-23 20:56:28');
INSERT INTO `elders` VALUES (35, '牢王', 131, '2025-06-27', '汉族', 'laowang', NULL, '', '', '', '1', '', '月结', NULL, '', 'D111', '2025-06-27 22:51:22', NULL, '2025-06-27 22:51:22', '2025-06-27 22:51:22');

-- ----------------------------
-- Table structure for expenses
-- ----------------------------
DROP TABLE IF EXISTS `expenses`;
CREATE TABLE `expenses`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `elder` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `amount` decimal(38, 2) NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付情况',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `expense_date` date NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `elder_id`(`elder` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of expenses
-- ----------------------------
INSERT INTO `expenses` VALUES (1, '王建国', '住宿费', 1131.92, '已支付', '2025-07月', '2025-02-10', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (2, '李秀兰', '住宿费', 1094.66, '未支付', '2025-09月', '2025-06-17', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (3, '刘志强', '住宿费', 1011.19, '未支付', '2025-01月', '2025-01-06', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (4, '陈美玲', '住宿费', 1339.58, '已支付', '2025-08月', '2025-01-13', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (5, '赵文斌', '住宿费', 1363.95, '未支付', '2025-12月', '2025-03-17', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (6, '孙丽娟', '住宿费', 1447.11, '已支付', '2025-10月', '2025-03-30', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (7, '周志刚', '住宿费', 1442.53, '已支付', '2025-12月', '2025-04-25', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (8, '黄秀英', '住宿费', 1330.26, '已支付', '2025-04月', '2025-03-12', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (9, '吴建国', '住宿费', 1472.82, '未支付', '2025-01月', '2025-05-25', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (10, '郑小红', '住宿费', 1408.83, '已支付', '2025-08月', '2025-03-19', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (11, '徐志远', '住宿费', 1440.13, '已支付', '2025-10月', '2025-04-22', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (12, '张慧敏', '住宿费', 1171.86, '未支付', '2025-09月', '2025-04-21', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (13, '马建国', '住宿费', 1365.70, '未支付', '2025-08月', '2025-01-29', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (14, '朱小梅', '住宿费', 1115.34, '已支付', '2025-09月', '2024-12-29', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (15, '宋建国', '住宿费', 1353.14, '未支付', '2025-08月', '2025-01-29', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (16, '林秀兰', '住宿费', 1137.30, '已支付', '2025-12月', '2025-01-03', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (17, '杨建国', '住宿费', 1449.42, '已支付', '2025-08月', '2025-03-29', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (18, '何秀英', '住宿费', 1246.02, '未支付', '2025-01月', '2025-03-02', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (19, '许建国', '住宿费', 1047.44, '已支付', '2025-07月', '2025-03-06', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (20, '蒋秀兰', '住宿费', 1152.74, '未支付', '2025-09月', '2025-03-08', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (21, '沈建国', '住宿费', 1438.06, '已支付', '2025-08月', '2025-04-26', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (22, '韩秀兰', '住宿费', 1420.59, '未支付', '2025-03月', '2025-02-23', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (23, '程建国', '住宿费', 1319.52, '已支付', '2025-03月', '2024-12-27', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (24, '董秀英', '住宿费', 1205.41, '未支付', '2025-01月', '2025-05-24', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (25, '梁建国', '住宿费', 1306.19, '未支付', '2025-07月', '2025-01-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (26, '蔡秀兰', '住宿费', 1448.63, '未支付', '2025-10月', '2025-06-05', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (27, '潘建国', '住宿费', 1123.01, '已支付', '2025-11月', '2025-01-23', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (28, '秦秀兰', '住宿费', 1227.83, '未支付', '2025-10月', '2025-04-10', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (29, '罗建国', '住宿费', 1405.04, '未支付', '2025-10月', '2025-03-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (30, '朱明远', '住宿费', 1251.95, '已支付', '2025-10月', '2025-04-21', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (32, '王建国', '护理费', 719.53, '未支付', '一级护理', '2025-04-15', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (33, '李秀兰', '护理费', 759.97, '未支付', '二级护理', '2025-03-06', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (34, '刘志强', '护理费', 870.02, '未支付', '三级护理', '2025-05-26', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (35, '陈美玲', '护理费', 582.18, '未支付', '二级护理', '2025-05-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (36, '赵文斌', '护理费', 583.95, '未支付', '一级护理', '2025-06-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (37, '孙丽娟', '护理费', 997.01, '已支付', '三级护理', '2025-01-20', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (38, '周志刚', '护理费', 994.35, '已支付', '二级护理', '2025-01-18', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (39, '黄秀英', '护理费', 628.32, '未支付', '三级护理', '2025-03-13', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (40, '吴建国', '护理费', 918.49, '已支付', '二级护理', '2025-01-13', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (41, '郑小红', '护理费', 514.54, '已支付', '二级护理', '2025-05-14', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (42, '徐志远', '护理费', 863.75, '已支付', '三级护理', '2025-02-24', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (43, '张慧敏', '护理费', 707.88, '未支付', '一级护理', '2025-05-23', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (44, '马建国', '护理费', 808.84, '未支付', '二级护理', '2024-12-30', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (45, '朱小梅', '护理费', 594.65, '已支付', '一级护理', '2025-03-25', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (46, '宋建国', '护理费', 733.34, '未支付', '三级护理', '2025-02-10', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (47, '林秀兰', '护理费', 611.92, '已支付', '三级护理', '2025-01-31', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (48, '杨建国', '护理费', 653.19, '未支付', '一级护理', '2025-02-04', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (49, '何秀英', '护理费', 733.06, '未支付', '一级护理', '2025-03-07', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (50, '许建国', '护理费', 508.74, '已支付', '一级护理', '2025-04-25', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (51, '蒋秀兰', '护理费', 906.93, '未支付', '一级护理', '2024-12-30', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (52, '沈建国', '护理费', 818.09, '未支付', '一级护理', '2025-04-25', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (53, '韩秀兰', '护理费', 960.48, '未支付', '二级护理', '2025-05-07', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (54, '程建国', '护理费', 749.96, '未支付', '三级护理', '2025-06-14', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (55, '董秀英', '护理费', 564.01, '未支付', '二级护理', '2025-06-16', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (56, '梁建国', '护理费', 871.89, '已支付', '二级护理', '2025-02-05', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (57, '蔡秀兰', '护理费', 523.25, '已支付', '三级护理', '2025-03-29', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (58, '潘建国', '护理费', 818.46, '已支付', '三级护理', '2025-02-03', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (59, '秦秀兰', '护理费', 849.48, '未支付', '一级护理', '2025-03-02', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (60, '罗建国', '护理费', 860.20, '已支付', '三级护理', '2025-04-17', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (61, '朱明远', '护理费', 879.67, '已支付', '三级护理', '2025-06-13', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (63, '王建国', '伙食费', 389.07, '未支付', '标准餐', '2025-03-02', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (64, '李秀兰', '伙食费', 375.15, '未支付', '标准餐', '2025-06-21', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (65, '刘志强', '伙食费', 306.71, '已支付', '标准餐', '2025-05-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (66, '陈美玲', '伙食费', 308.21, '已支付', '标准餐', '2025-01-01', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (67, '赵文斌', '伙食费', 475.28, '未支付', '标准餐', '2025-04-09', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (68, '孙丽娟', '伙食费', 300.82, '已支付', '定制餐', '2025-02-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (69, '周志刚', '伙食费', 432.81, '已支付', '定制餐', '2025-02-23', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (70, '黄秀英', '伙食费', 403.02, '未支付', '定制餐', '2025-03-20', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (71, '吴建国', '伙食费', 455.56, '已支付', '定制餐', '2025-04-05', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (72, '郑小红', '伙食费', 363.11, '未支付', '定制餐', '2025-06-23', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (73, '徐志远', '伙食费', 548.90, '未支付', '标准餐', '2025-05-14', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (74, '张慧敏', '伙食费', 507.73, '未支付', '定制餐', '2025-01-17', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (75, '马建国', '伙食费', 598.43, '已支付', '标准餐', '2025-02-01', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (76, '朱小梅', '伙食费', 565.31, '未支付', '标准餐', '2025-03-06', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (77, '宋建国', '伙食费', 567.29, '未支付', '定制餐', '2025-04-03', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (78, '林秀兰', '伙食费', 419.46, '已支付', '定制餐', '2025-01-06', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (79, '杨建国', '伙食费', 540.37, '未支付', '标准餐', '2025-03-10', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (80, '何秀英', '伙食费', 404.37, '未支付', '定制餐', '2025-01-26', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (81, '许建国', '伙食费', 359.56, '已支付', '定制餐', '2025-06-22', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (82, '蒋秀兰', '伙食费', 447.17, '已支付', '标准餐', '2025-03-02', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (83, '沈建国', '伙食费', 565.64, '已支付', '定制餐', '2025-06-16', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (84, '韩秀兰', '伙食费', 477.59, '未支付', '定制餐', '2025-04-14', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (85, '程建国', '伙食费', 434.52, '已支付', '标准餐', '2025-06-20', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (86, '董秀英', '伙食费', 567.16, '已支付', '标准餐', '2025-05-08', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (87, '梁建国', '伙食费', 340.33, '已支付', '定制餐', '2025-06-09', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (88, '蔡秀兰', '伙食费', 513.20, '未支付', '标准餐', '2025-04-07', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (89, '潘建国', '伙食费', 366.70, '未支付', '定制餐', '2025-04-11', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (90, '秦秀兰', '伙食费', 479.86, '已支付', '定制餐', '2025-06-14', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (91, '罗建国', '伙食费', 587.52, '已支付', '定制餐', '2025-05-05', '2025-06-23 20:50:20', '2025-06-28 08:44:41');
INSERT INTO `expenses` VALUES (92, '朱明远', '伙食费', 445.73, '已支付', '定制餐', '2025-03-15', '2025-06-23 20:50:20', '2025-06-28 08:44:41');

-- ----------------------------
-- Table structure for health_records
-- ----------------------------
DROP TABLE IF EXISTS `health_records`;
CREATE TABLE `health_records`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `elder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `record_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `record_date` date NOT NULL COMMENT '记录日期',
  `careworker_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '记录护工',
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `elder_id`(`elder_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of health_records
-- ----------------------------
INSERT INTO `health_records` VALUES (1, '王建国', '体温', '36.8 °C', '2024-12-29', '刘芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (2, '李秀兰', '血糖', '5.5 mmol/L', '2025-05-06', '韩芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (3, '刘志强', '心率', '75 bpm', '2025-05-15', '李娟', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (4, '陈美玲', '体重', '65 kg', '2025-02-08', '黄勇', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (5, '赵文斌', '血压', '120/80 mmHg', '2025-05-01', '郑小红', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (6, '孙丽娟', '体温', '36.8 °C', '2025-01-13', '程强', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (7, '周志刚', '血糖', '5.5 mmol/L', '2025-04-26', '程伟', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (8, '黄秀英', '心率', '75 bpm', '2025-05-28', '徐静', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (9, '吴建国', '体重', '65 kg', '2025-01-20', '林芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (10, '郑小红', '血压', '120/80 mmHg', '2024-12-28', '吴建国', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (11, '徐志远', '体温', '36.8 °C', '2025-06-20', '周敏', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (12, '张慧敏', '血糖', '5.5 mmol/L', '2025-03-25', '韩梅', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (13, '马建国', '心率', '75 bpm', '2025-04-14', '蒋丽', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (14, '朱小梅', '体重', '65 kg', '2025-01-19', '黄勇', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (15, '宋建国', '血压', '120/80 mmHg', '2025-01-10', '徐静', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (16, '林秀兰', '体温', '36.8 °C', '2025-03-05', '张丽华', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (17, '杨建国', '血糖', '5.5 mmol/L', '2025-04-11', '韩芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (18, '何秀英', '心率', '75 bpm', '2025-05-03', '郑小红', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (19, '许建国', '体重', '65 kg', '2025-02-17', '梁强', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (20, '蒋秀兰', '血压', '120/80 mmHg', '2025-01-27', '陈晓东', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (21, '沈建国', '体温', '36.8 °C', '2025-05-11', '蔡丽', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (22, '韩秀兰', '血糖', '5.5 mmol/L', '2025-05-08', '秦芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (23, '程建国', '心率', '75 bpm', '2025-02-17', '董芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (24, '董秀英', '体重', '65 kg', '2025-06-18', '周敏', NULL, '2025-06-23 20:50:46', '2025-06-28 22:59:01');
INSERT INTO `health_records` VALUES (25, '梁建国', '血压', '120/80 mmHg', '2025-04-29', '梁强', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (26, '蔡秀兰', '体温', '36.8 °C', '2025-03-21', '潘芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (27, '潘建国', '血糖', '5.5 mmol/L', '2025-05-18', '蔡丽', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (28, '秦秀兰', '心率', '75 bpm', '2025-06-05', '刘芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (29, '罗建国', '体重', '65 kg', '2025-06-01', '韩芳', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (30, '朱明远', '血压', '120/80 mmHg', '2025-04-01', '蒋丽', NULL, '2025-06-23 20:50:46', '2025-06-28 10:29:57');
INSERT INTO `health_records` VALUES (33, '1', '血糖', '1', '2025-06-29', '', '', '2025-06-29 05:23:00', '2025-06-29 05:23:00');

-- ----------------------------
-- Table structure for system_settings
-- ----------------------------
DROP TABLE IF EXISTS `system_settings`;
CREATE TABLE `system_settings`  (
  `setting_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `setting_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '设置值',
  PRIMARY KEY (`setting_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_settings
-- ----------------------------
INSERT INTO `system_settings` VALUES ('bedWarningThreshold', '80');
INSERT INTO `system_settings` VALUES ('copyright', '© 2025 Guangyuyang Tech');
INSERT INTO `system_settings` VALUES ('favicon', 'data:image/x-icon;base64,AAABAAEAEBAAAAEAIABoBAAAFgAAACgAAAAQAAAAIAAAAAEAIAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA4d/9AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA4d/9AOHf/wDh3/8A4d//AOHf/wDh3/8A4d//AOHf/wDh3/8A4d/9AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAOHf/QDh3/8A4d//AOHf/QDh3/0A4d/9AOHf/QDh3/8A4d/9AAAAAOHf/QDh3/0AAAAAAAAAAAAAAAAAAAAAAAAAAOHf/QDh3/8A4d//AOHf/wDh3/8A4d//AOHf/wDh3/0A4d/9AAAAAOHf/QDh3/0A4d/9AAAAAAAAAAAAAAAAAAAAAOHf/QDh3/8A4d//AOHf/wDh3/8A4d/9AOHf/QDh3/8A4d/9AAAAAOHf/QDh3/8A4d//AOHf/QAAAAAAAAAAAAAAAOHf/QDh3/8A4d//AOHf/wDh3/8A4d/9AOHf/QDh3/8A4d/9AAAAAOHf/wDh3/8A4d//AOHf/wDh3/0AAAAAAAAAAOHf/QDh3/8A4d//AOHf/wDh3/8A4d/9AOHf/QDh3/0A4d/9AOHf/QDh3/8A4d//AOHf/wDh3//AOHf/QAAAAAChwP0AocD9AKHA/QChwP0AocD9AKHA/QChwP0AocD9AKHA/QChwP0AocD9AKHA/QChwP0AocD9AKHA/QAAAAA4d/9AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/9AOHf/QDh3/0AAAAAAAAAAOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/0A4d/9AOHf/wDh3//AOHf/wDh3//AOHf/QAAAAAAAAAA4d/9AOHf/QDh3/0AOHf/QDh3/0AOHf/QDh3/9AOHf/wDh3/8A4d//AOHf/wDh3//AOHf/wDh3/0AAAAAAAAAAAAAAADh3/0A4d/9AOHf/QDh3/0A4d/9AOHf/wDh3/8A4d//AOHf/wDh3//AOHf/wDh3//AOHf/QAAAAAAAAAAAAAAAAAAAAA4d/9AOHf/QDh3/8A4d/9AOHf/QDh3/0A4d/9AOHf/wDh3//AOHf/wDh3//AOHf/QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADh3/0A4d/9AOHf/QDh3/0AOHf/QDh3/0AOHf/wDh3//AOHf/QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA4d/9AOHf/QDh3/0A4d/9AOHf/QDh3/0AOHf/QAAAAAAAAAAAAAAAA/D8AAPAHAADgBwAA4AcAAMADAADgAwAA4AEAAPABAAD4AQAA+AEAAOABAADAAQAAwAMAAMADAADgBwAA8B8AAA==');
INSERT INTO `system_settings` VALUES ('isInitialized', 'true');
INSERT INTO `system_settings` VALUES ('logo', '');
INSERT INTO `system_settings` VALUES ('sessionTimeout', '30');
INSERT INTO `system_settings` VALUES ('systemName', '智慧养老院管理系统');
INSERT INTO `system_settings` VALUES ('theme', 'light');

SET FOREIGN_KEY_CHECKS = 1;
