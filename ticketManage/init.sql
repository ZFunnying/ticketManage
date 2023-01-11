/*
Navicat MySQL Data Transfer

Source Server         : demo1
Source Server Version : 50738
Source Host           : localhost:3306
Source Database       : init

Target Server Type    : MYSQL
Target Server Version : 50738
File Encoding         : 65001

Date: 2023-01-11 23:35:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `group_info`
-- ----------------------------
DROP TABLE IF EXISTS `group_info`;
CREATE TABLE `group_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `sort` int(3) DEFAULT NULL,
  `group_code` varchar(100) DEFAULT NULL,
  `group_name` varchar(100) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `state` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分组信息表';

-- ----------------------------
-- Records of group_info
-- ----------------------------

-- ----------------------------
-- Table structure for `group_item`
-- ----------------------------
DROP TABLE IF EXISTS `group_item`;
CREATE TABLE `group_item` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `group_code` varchar(50) DEFAULT NULL COMMENT '组别',
  `item_name` varchar(255) DEFAULT NULL COMMENT '组件',
  `item_code` varchar(255) DEFAULT NULL,
  `sort` int(3) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `item_code` (`item_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分组子项表';

-- ----------------------------
-- Records of group_item
-- ----------------------------

-- ----------------------------
-- Table structure for `h5_menu`
-- ----------------------------
DROP TABLE IF EXISTS `h5_menu`;
CREATE TABLE `h5_menu` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `node` int(2) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `has_children` bit(1) DEFAULT NULL,
  `roles` varchar(255) DEFAULT NULL,
  `sort` int(5) DEFAULT NULL,
  `is_show` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of h5_menu
-- ----------------------------

-- ----------------------------
-- Table structure for `h5_menu_parent_children_rel`
-- ----------------------------
DROP TABLE IF EXISTS `h5_menu_parent_children_rel`;
CREATE TABLE `h5_menu_parent_children_rel` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(5) NOT NULL,
  `children_id` int(5) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of h5_menu_parent_children_rel
-- ----------------------------

-- ----------------------------
-- Table structure for `logging_event`
-- ----------------------------
DROP TABLE IF EXISTS `logging_event`;
CREATE TABLE `logging_event` (
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` text NOT NULL,
  `logger_name` varchar(254) NOT NULL,
  `level_string` varchar(254) NOT NULL,
  `thread_name` varchar(254) DEFAULT NULL,
  `reference_flag` smallint(6) DEFAULT NULL,
  `arg0` varchar(254) DEFAULT NULL,
  `arg1` varchar(254) DEFAULT NULL,
  `arg2` varchar(254) DEFAULT NULL,
  `arg3` varchar(254) DEFAULT NULL,
  `caller_filename` varchar(254) NOT NULL,
  `caller_class` varchar(254) NOT NULL,
  `caller_method` varchar(254) NOT NULL,
  `caller_line` char(4) NOT NULL,
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`event_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of logging_event
-- ----------------------------

-- ----------------------------
-- Table structure for `logging_event_exception`
-- ----------------------------
DROP TABLE IF EXISTS `logging_event_exception`;
CREATE TABLE `logging_event_exception` (
  `event_id` bigint(20) NOT NULL,
  `i` smallint(6) NOT NULL,
  `trace_line` varchar(254) NOT NULL,
  PRIMARY KEY (`event_id`,`i`) USING BTREE,
  CONSTRAINT `logging_event_exception_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `logging_event` (`event_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of logging_event_exception
-- ----------------------------

-- ----------------------------
-- Table structure for `logging_event_property`
-- ----------------------------
DROP TABLE IF EXISTS `logging_event_property`;
CREATE TABLE `logging_event_property` (
  `event_id` bigint(20) NOT NULL,
  `mapped_key` varchar(254) NOT NULL,
  `mapped_value` text,
  PRIMARY KEY (`event_id`,`mapped_key`) USING BTREE,
  CONSTRAINT `logging_event_property_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `logging_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of logging_event_property
-- ----------------------------

-- ----------------------------
-- Table structure for `menu_parent_children_rel`
-- ----------------------------
DROP TABLE IF EXISTS `menu_parent_children_rel`;
CREATE TABLE `menu_parent_children_rel` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `parent_id` int(5) NOT NULL,
  `children_id` int(5) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of menu_parent_children_rel
-- ----------------------------
INSERT INTO `menu_parent_children_rel` VALUES ('17', '44', '61');
INSERT INTO `menu_parent_children_rel` VALUES ('18', '44', '60');
INSERT INTO `menu_parent_children_rel` VALUES ('19', '44', '62');
INSERT INTO `menu_parent_children_rel` VALUES ('20', '65', '60');
INSERT INTO `menu_parent_children_rel` VALUES ('21', '65', '61');
INSERT INTO `menu_parent_children_rel` VALUES ('22', '65', '62');

-- ----------------------------
-- Table structure for `permission`
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `url` varchar(255) DEFAULT NULL COMMENT '接口',
  `roles` varchar(255) DEFAULT NULL COMMENT '权限',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------

-- ----------------------------
-- Table structure for `qiniu`
-- ----------------------------
DROP TABLE IF EXISTS `qiniu`;
CREATE TABLE `qiniu` (
  `id` int(1) NOT NULL AUTO_INCREMENT,
  `access_key` varchar(255) DEFAULT NULL,
  `secret_key` varchar(255) DEFAULT NULL,
  `temp_url` varchar(255) DEFAULT NULL,
  `bucket_name` varchar(255) DEFAULT NULL,
  `upload_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of qiniu
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `node` int(2) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `redirect` varchar(255) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `has_children` bit(1) DEFAULT NULL,
  `sort` int(5) DEFAULT NULL,
  `is_show` bit(1) DEFAULT b'1',
  `title` varchar(255) DEFAULT NULL,
  `icon` varchar(100) DEFAULT NULL,
  `roles` varchar(255) DEFAULT NULL,
  `no_cache` bit(1) DEFAULT b'0',
  `affix` bit(1) DEFAULT b'0',
  `breadcrumb` bit(1) DEFAULT b'1',
  `always_show` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('44', '0', '/system', '/system/user', 'Layout', 'system', '', '1', '', '系统设置', 'system', 'super_admin', null, null, null, '');
INSERT INTO `sys_menu` VALUES ('45', '1', '/system/user', null, 'modules/user/views/sysUserList', 'sysUserList', null, '1', '', '用户设置', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('46', '1', '/system/role', null, 'modules/sys_role/views/sysRoleList', 'sysRoleList', null, '2', '', '组别设置', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('47', '1', '/system/menu', null, 'modules/menu/views/sysMenuList', 'sysMenuList', null, '3', '', '菜单设置', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('48', '1', '/system/h5menu', null, 'modules/menu/views/h5MenuList', 'h5MenuList', null, '4', '', '移动端菜单设置', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('49', '1', '/system/permission', null, 'modules/permission/views/permissionList', 'permissionList', null, '5', '', '权限设置', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('50', '1', '/system/itemGroup', null, 'modules/item_group/views/groupItemList', 'groupItemList', null, '6', '', '数据字典', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('51', '1', '/system/logs', null, 'modules/system_logger/views/loggingEventList', 'loggingEventList', null, '7', '', '系统日志', null, 'super_admin', '', '', '', '');
INSERT INTO `sys_menu` VALUES ('60', '1', '/system/searchTicket', '', 'modules/user/views/searchTicket', 'searchTicket', null, '8', '', '车票查询', null, null, '', '', '', '');
INSERT INTO `sys_menu` VALUES ('61', '1', '/system/ticketBuy', null, 'modules/user/views/buyTicket', 'buyTicket', null, '9', '', '车票购买', null, null, '', '', '', '');
INSERT INTO `sys_menu` VALUES ('62', '1', '/system/stops', null, 'modules/user/views/stops', 'stops', null, '10', '', '站点查询', null, null, '', '', '', '');
INSERT INTO `sys_menu` VALUES ('65', '0', '/system', '/system/user', 'Layout', 'orders', '', '2', '', '用户订单', null, null, '', '', '', '');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role` varchar(50) DEFAULT NULL COMMENT '角色',
  `description` varchar(255) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `role` (`role`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('2', 'super_admin', '超级管理员');
INSERT INTO `sys_role` VALUES ('3', 'test', '测试');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `real_name` varchar(255) NOT NULL DEFAULT '' COMMENT '真实名字',
  `email` varchar(255) NOT NULL COMMENT '电子邮件',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `salt` varchar(255) NOT NULL COMMENT '盐',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `introduction` varchar(255) DEFAULT NULL COMMENT '介绍',
  `state` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态',
  `add_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('69', 'admin', '管理员', 'admin@admin.com', '9defd1d9cba7095422e7d76103632b74da02eefaa7ec0089eadb212100459ea8', 'Bck3xXws', null, '管理员账号', '', '2020-12-26 01:03:02');

-- ----------------------------
-- Table structure for `user_role_rel`
-- ----------------------------
DROP TABLE IF EXISTS `user_role_rel`;
CREATE TABLE `user_role_rel` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `role_id` int(11) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user_role_rel
-- ----------------------------
INSERT INTO `user_role_rel` VALUES ('3', '69', '2');
