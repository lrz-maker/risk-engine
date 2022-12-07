SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_engine_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_condition`;
CREATE TABLE `t_engine_condition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `left_type` tinyint(4) NOT NULL COMMENT '0: 常量 1: 上下文属性 2: 函数 3: 指标',
  `left_property` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `left_value` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `right_type` tinyint(4) NOT NULL COMMENT '0: 常量 1: 上下文属性 2: 函数 3: 指标',
  `right_property` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `right_value` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `comparator` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `relation_id` bigint(20) NOT NULL,
  `relation_type` tinyint(4) NOT NULL COMMENT '0: 规则 1: 函数 2: 指标 3: 条件组',
  `version` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='条件';

-- ----------------------------
-- Records of t_engine_condition
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (46, 'getValueByJsonPath-df1c9888-a3d7-407b-b335-94e662092349', '1', 2, 'getValueByJsonPath', '1', 0, NULL, '1', 'GT', 1, 1, 1, '2022-11-09 17:36:02', 'admin', '2022-11-16 16:11:58', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (47, 'getValueByJsonPath-ad56358f-45a9-48bd-9394-c3a39f7a190a', '2', 0, NULL, '2', 0, NULL, '2', 'GT', 1, 1, 0, '2022-11-09 17:41:37', 'admin', '2022-11-16 16:12:01', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (48, 'getValueByJsonPath-a1b392f1-95b1-4009-bbd7-66f1023f4736', '1', 2, 'getValueByJsonPath', '1', 0, NULL, '1', 'GT', 38, 3, 1, '2022-11-09 17:43:34', 'admin', '2022-11-16 16:12:03', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (49, 'getValueByJsonPath-dcf02def-869e-46fb-9d60-79baecdd7409', '2', 0, NULL, '2', 0, NULL, '2', 'GT', 38, 3, 1, '2022-11-09 17:43:34', 'admin', '2022-11-16 16:12:06', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (54, 'test2-1ad41c52-2dfe-44f0-a671-6fcdec1a346f', '111', 1, 'userId', NULL, 0, NULL, '1111', 'EQ', 16, 1, 1, '2022-11-16 18:23:12', 'admin', '2022-11-16 18:23:12', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (55, 'test2-25529154-4eb2-48f6-8625-e4d8a869e901', '1', 0, NULL, '1', 0, NULL, '1', 'EQ', 41, 3, 0, '2022-11-17 10:49:36', 'admin', '2022-11-17 10:49:36', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (60, '22-ffb18b73-7661-4e6b-8e11-f553d8b15ee4', '1', 0, NULL, '1', 0, NULL, '1', 'EQ', 37, 0, 0, '2022-11-25 09:33:01', 'admin', '2022-11-25 09:33:01', 'admin');
INSERT INTO `t_engine_condition` (`id`, `code`, `description`, `left_type`, `left_property`, `left_value`, `right_type`, `right_property`, `right_value`, `comparator`, `relation_id`, `relation_type`, `version`, `create_time`, `update_by`, `update_Time`, `create_by`) VALUES (61, 'NBYGSCDJ-001-67440b78-1a00-45fe-b204-9c11e0754605', '2', 2, '17', '1D', 1, 'userId', '2222', 'CONTAINS', 34, 0, 5, '2022-11-29 14:40:47', 'admin', '2022-11-29 14:40:47', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for t_engine_condition_group
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_condition_group`;
CREATE TABLE `t_engine_condition_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
  `relation_id` bigint(20) NOT NULL,
  `relation_type` tinyint(4) NOT NULL COMMENT '0: 规则 1: 函数 2: 指标 3: 条件组',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='条件组';

-- ----------------------------
-- Records of t_engine_condition_group
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_condition_group` (`id`, `code`, `description`, `logic`, `relation_id`, `relation_type`, `create_time`, `update_by`, `update_Time`, `create_by`, `version`) VALUES (38, 'getValueByJsonPath-9ca60de3-c75c-4cf9-a243-4a9830707c60', '1', 1, 1, 1, '2022-11-09 17:43:33', 'admin', '2022-11-09 17:43:33', 'admin', 1);
INSERT INTO `t_engine_condition_group` (`id`, `code`, `description`, `logic`, `relation_id`, `relation_type`, `create_time`, `update_by`, `update_Time`, `create_by`, `version`) VALUES (41, 'test2-de6f36c4-f011-4067-9e9f-4a36e8f2234d', '1', 1, 16, 1, '2022-11-17 10:49:36', 'admin', '2022-11-17 10:49:36', 'admin', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_event
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_event`;
CREATE TABLE `t_engine_event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `analysis` tinyint(4) NOT NULL COMMENT '0: no 1: yes',
  `accumulate` tinyint(4) NOT NULL COMMENT '0: or 1: and',
  `status` tinyint(4) NOT NULL COMMENT '0: 下线 1: 上线',
  `create_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='事件';

-- ----------------------------
-- Records of t_engine_event
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_event` (`id`, `code`, `description`, `analysis`, `accumulate`, `status`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`) VALUES (9, 'e_nft_purchase_first', '数藏一级交易(购买)', 1, 1, 1, 'admin', '2022-11-01 16:59:25', 'admin', '2022-11-18 11:14:41', 4);
INSERT INTO `t_engine_event` (`id`, `code`, `description`, `analysis`, `accumulate`, `status`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`) VALUES (13, '1', '1', 1, 1, 1, 'admin', '2022-11-18 18:17:41', '', '2022-11-18 18:17:41', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_event_indicator_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_event_indicator_relation`;
CREATE TABLE `t_engine_event_indicator_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `indicator_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='事件与指标关联';

-- ----------------------------
-- Records of t_engine_event_indicator_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_event_indicator_relation` (`id`, `event_id`, `indicator_id`) VALUES (50, 9, 36);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_event_rule_relation
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_event_rule_relation`;
CREATE TABLE `t_engine_event_rule_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_id` bigint(20) NOT NULL,
  `rule_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='事件与规则关联';

-- ----------------------------
-- Records of t_engine_event_rule_relation
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_event_rule_relation` (`id`, `event_id`, `rule_id`) VALUES (52, 9, 34);
INSERT INTO `t_engine_event_rule_relation` (`id`, `event_id`, `rule_id`) VALUES (53, 9, 37);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_function
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_function`;
CREATE TABLE `t_engine_function` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `return_type` tinyint(4) NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=601 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='内置函数';

-- ----------------------------
-- Records of t_engine_function
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_function` (`id`, `code`, `description`, `return_type`, `version`) VALUES (599, 'getValueByJsonPath', '通过JSONPATH获取上下文中的值', 5, 0);
INSERT INTO `t_engine_function` (`id`, `code`, `description`, `return_type`, `version`) VALUES (600, 'roastFunction', '名单函数', 3, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_function_extend
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_function_extend`;
CREATE TABLE `t_engine_function_extend` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
  `version` bigint(20) NOT NULL DEFAULT '0',
  `function_code` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '内置函数code',
  `params` varchar(255) CHARACTER SET utf8mb4 NOT NULL COMMENT '函数自定义入参',
  `create_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='基础函数扩展';

-- ----------------------------
-- Records of t_engine_function_extend
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_function_extend` (`id`, `code`, `description`, `logic`, `version`, `function_code`, `params`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (16, 'test2', 'tt', 1, 3, 'getValueByJsonPath', '$.userId', 'admin', 'admin', '2022-11-15 18:09:12', '2022-11-15 18:09:12');
INSERT INTO `t_engine_function_extend` (`id`, `code`, `description`, `logic`, `version`, `function_code`, `params`, `create_by`, `update_by`, `create_time`, `update_time`) VALUES (17, 'mobileBlackList', 'mobileBlackList', 1, 2, 'roastFunction', '2', 'admin', 'admin', '2022-11-21 19:45:57', '2022-11-21 19:45:57');
COMMIT;

-- ----------------------------
-- Table structure for t_engine_indicator
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_indicator`;
CREATE TABLE `t_engine_indicator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) NOT NULL DEFAULT '0',
  `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
  `period` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `factor` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `type` tinyint(4) NOT NULL COMMENT '0: 指标KEY拼接值\n1: 唯一累积值\n2: 累积时间范围\n3: 单值累积值\n',
  `dimension` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='指标';

-- ----------------------------
-- Records of t_engine_indicator
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_indicator` (`id`, `code`, `description`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`, `logic`, `period`, `factor`, `type`, `dimension`) VALUES (36, '22', '用户的绑定过多少个手机号', 'admin', '2022-11-24 13:47:59', 'admin', '2022-11-24 13:47:59', 15, 1, '3D', '', 1, 'userId,eventCode');
COMMIT;

-- ----------------------------
-- Table structure for t_engine_punish
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_punish`;
CREATE TABLE `t_engine_punish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `punish_code` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `punish_field` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `rule_id` bigint(20) NOT NULL,
  `punish_period` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `roaster_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='惩罚策略';

-- ----------------------------
-- Records of t_engine_punish
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_engine_return_message
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_return_message`;
CREATE TABLE `t_engine_return_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` int(11) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `return_message` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `create_by` varchar(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='拒绝文案';

-- ----------------------------
-- Records of t_engine_return_message
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_return_message` (`id`, `code`, `description`, `return_message`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`) VALUES (34, 100101, '1', '1', 'admin', '2022-11-04 13:37:39', '', '2022-11-04 15:54:16', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_roaster
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_roaster`;
CREATE TABLE `t_engine_roaster` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(32) NOT NULL,
  `name` varchar(64) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_user` varchar(64) DEFAULT NULL,
  `expired_at` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uq_code` (`code`),
  UNIQUE KEY `idx_uq_name` (`name`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_expired_at` (`expired_at`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_engine_roaster
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_roaster` (`id`, `code`, `name`, `create_time`, `update_time`, `update_user`, `expired_at`, `remark`) VALUES (10, '2', '2', '2022-11-24 20:40:58', '2022-11-24 20:40:57', NULL, NULL, '2');
COMMIT;

-- ----------------------------
-- Table structure for t_engine_roaster_list
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_roaster_list`;
CREATE TABLE `t_engine_roaster_list` (
  `id` int(12) unsigned NOT NULL AUTO_INCREMENT,
  `value` varchar(64) NOT NULL,
  `roaster_id` int(12) unsigned NOT NULL,
  `expired_at` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `remark` varchar(255) DEFAULT NULL,
  `update_user` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_uq_value` (`value`,`roaster_id`) USING BTREE,
  KEY `value` (`value`),
  KEY `roaster_id` (`roaster_id`),
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4286 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_engine_roaster_list
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_roaster_list` (`id`, `value`, `roaster_id`, `expired_at`, `create_time`, `update_time`, `remark`, `update_user`) VALUES (4276, '1', 10, '2023-12-02 10:26:47', '2022-11-24 20:43:00', '2022-12-02 10:26:50', '1', 'admin');
INSERT INTO `t_engine_roaster_list` (`id`, `value`, `roaster_id`, `expired_at`, `create_time`, `update_time`, `remark`, `update_user`) VALUES (4284, '2', 10, '2022-11-25 20:43:56', '2022-11-24 20:45:17', '2022-11-24 20:45:17', NULL, 'admin');
INSERT INTO `t_engine_roaster_list` (`id`, `value`, `roaster_id`, `expired_at`, `create_time`, `update_time`, `remark`, `update_user`) VALUES (4285, '3', 10, '2022-11-25 20:43:56', '2022-11-24 20:45:17', '2022-11-24 20:45:17', NULL, 'admin');
COMMIT;

-- ----------------------------
-- Table structure for t_engine_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_rule`;
CREATE TABLE `t_engine_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `logic` tinyint(4) NOT NULL COMMENT '0: or 1: and',
  `status` tinyint(4) NOT NULL COMMENT '0: 下线 1: 上线',
  `create_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) NOT NULL DEFAULT '0',
  `test` tinyint(4) NOT NULL DEFAULT '0',
  `return_message_id` bigint(20) DEFAULT NULL,
  `result` int(11) NOT NULL COMMENT '0: 通过 1001: 拒绝',
  `rule_group_id` bigint(20) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='规则';

-- ----------------------------
-- Records of t_engine_rule
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_rule` (`id`, `code`, `description`, `logic`, `status`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`, `test`, `return_message_id`, `result`, `rule_group_id`, `priority`) VALUES (34, 'NBYGSCDJ-001', '内部员工数藏登记', 0, 0, 'admin', '2022-11-03 15:20:09', 'admin', '2022-12-02 10:22:21', 13, 1, NULL, 1001, 12, 1);
INSERT INTO `t_engine_rule` (`id`, `code`, `description`, `logic`, `status`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`, `test`, `return_message_id`, `result`, `rule_group_id`, `priority`) VALUES (38, '1', '1', 1, 0, 'admin', '2022-12-01 14:25:00', 'admin', '2022-12-01 14:25:10', 0, 0, NULL, 0, NULL, 0);
INSERT INTO `t_engine_rule` (`id`, `code`, `description`, `logic`, `status`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`, `test`, `return_message_id`, `result`, `rule_group_id`, `priority`) VALUES (39, '22', '22', 1, 0, 'admin', '2022-12-01 14:37:54', '', '2022-12-01 14:37:54', 0, 0, NULL, 0, NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_engine_rule_group
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_rule_group`;
CREATE TABLE `t_engine_rule_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
  `status` tinyint(4) NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '',
  `update_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='规则组';

-- ----------------------------
-- Records of t_engine_rule_group
-- ----------------------------
BEGIN;
INSERT INTO `t_engine_rule_group` (`id`, `code`, `description`, `status`, `create_by`, `create_time`, `update_by`, `update_Time`, `version`) VALUES (12, '4', '4', 1, 'admin', '2022-11-01 18:28:41', 'admin', '2022-11-02 09:27:49', 5);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
