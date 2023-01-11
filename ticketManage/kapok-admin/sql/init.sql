
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for group_info
-- ----------------------------
DROP TABLE IF EXISTS `group_info`;
CREATE TABLE `group_info`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `sort` int(3) NULL DEFAULT NULL,
  `group_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `state` tinyint(2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分组信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for group_item
-- ----------------------------
DROP TABLE IF EXISTS `group_item`;
CREATE TABLE `group_item`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `group_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组别',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
  `item_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(3) NULL DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint(2) NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `item_code`(`item_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分组子项表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for h5_menu
-- ----------------------------
DROP TABLE IF EXISTS `h5_menu`;
CREATE TABLE `h5_menu`  (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `node` int(2) NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `has_children` bit(1) NULL DEFAULT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(5) NULL DEFAULT NULL,
  `is_show` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for h5_menu_parent_children_rel
-- ----------------------------
DROP TABLE IF EXISTS `h5_menu_parent_children_rel`;
CREATE TABLE `h5_menu_parent_children_rel`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` int(5) NOT NULL,
  `children_id` int(5) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for logging_event
-- ----------------------------
DROP TABLE IF EXISTS `logging_event`;
CREATE TABLE `logging_event`  (
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `logger_name` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `level_string` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `thread_name` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `reference_flag` smallint(6) NULL DEFAULT NULL,
  `arg0` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `arg1` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `arg2` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `arg3` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `caller_filename` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `caller_class` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `caller_method` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `caller_line` char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`event_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logging_event_exception
-- ----------------------------
DROP TABLE IF EXISTS `logging_event_exception`;
CREATE TABLE `logging_event_exception`  (
  `event_id` bigint(20) NOT NULL,
  `i` smallint(6) NOT NULL,
  `trace_line` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`event_id`, `i`) USING BTREE,
  CONSTRAINT `logging_event_exception_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `logging_event` (`event_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for logging_event_property
-- ----------------------------
DROP TABLE IF EXISTS `logging_event_property`;
CREATE TABLE `logging_event_property`  (
  `event_id` bigint(20) NOT NULL,
  `mapped_key` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `mapped_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`event_id`, `mapped_key`) USING BTREE,
  CONSTRAINT `logging_event_property_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `logging_event` (`event_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for menu_parent_children_rel
-- ----------------------------
DROP TABLE IF EXISTS `menu_parent_children_rel`;
CREATE TABLE `menu_parent_children_rel`  (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `parent_id` int(5) NOT NULL,
  `children_id` int(5) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of menu_parent_children_rel
-- ----------------------------
INSERT INTO `menu_parent_children_rel` VALUES (11, 44, 45);
INSERT INTO `menu_parent_children_rel` VALUES (12, 44, 46);
INSERT INTO `menu_parent_children_rel` VALUES (13, 44, 47);
INSERT INTO `menu_parent_children_rel` VALUES (14, 44, 48);
INSERT INTO `menu_parent_children_rel` VALUES (15, 44, 49);
INSERT INTO `menu_parent_children_rel` VALUES (16, 44, 50);
INSERT INTO `menu_parent_children_rel` VALUES (17, 44, 51);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口',
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qiniu
-- ----------------------------
DROP TABLE IF EXISTS `qiniu`;
CREATE TABLE `qiniu`  (
  `id` int(1) NOT NULL AUTO_INCREMENT,
  `access_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `secret_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `temp_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bucket_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `upload_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `node` int(2) NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `has_children` bit(1) NULL DEFAULT NULL,
  `sort` int(5) NULL DEFAULT NULL,
  `is_show` bit(1) NULL DEFAULT b'1',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `no_cache` bit(1) NULL DEFAULT b'0',
  `affix` bit(1) NULL DEFAULT b'0',
  `breadcrumb` bit(1) NULL DEFAULT b'1',
  `always_show` bit(1) NULL DEFAULT b'0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (44, 0, '/system', '/system/user', 'Layout', 'system', b'1', 1, b'1', '系统设置', 'system', 'super_admin', NULL, NULL, NULL, b'1');
INSERT INTO `sys_menu` VALUES (45, 1, '/system/user', NULL, 'modules/user/views/sysUserList', 'sysUserList', NULL, 1, b'1', '用户设置', NULL, 'super_admin', b'0', b'0', b'1', b'0');
INSERT INTO `sys_menu` VALUES (46, 1, '/system/role', NULL, 'modules/sys_role/views/sysRoleList', 'sysRoleList', NULL, 2, b'1', '组别设置', NULL, 'super_admin', b'0', b'0', b'1', b'0');
INSERT INTO `sys_menu` VALUES (47, 1, '/system/menu', NULL, 'modules/menu/views/sysMenuList', 'sysMenuList', NULL, 3, b'1', '菜单设置', NULL, 'super_admin', b'0', b'0', b'1', b'0');
INSERT INTO `sys_menu` VALUES (48, 1, '/system/h5menu', NULL, 'modules/menu/views/h5MenuList', 'h5MenuList', NULL, 4, b'1', '移动端菜单设置', NULL, 'super_admin', b'0', b'0', b'1', b'0');
INSERT INTO `sys_menu` VALUES (49, 1, '/system/permission', NULL, 'modules/permission/views/permissionList', 'permissionList', NULL, 5, b'1', '权限设置', NULL, 'super_admin', b'0', b'0', b'1', b'0');
INSERT INTO `sys_menu` VALUES (50, 1, '/system/itemGroup', NULL, 'modules/item_group/views/groupItemList', 'groupItemList', NULL, 6, b'1', '数据字典', NULL, 'super_admin', b'0', b'0', b'1', b'0');
INSERT INTO `sys_menu` VALUES (51, 1, '/system/logs', NULL, 'modules/system_logger/views/loggingEventList', 'loggingEventList', NULL, 7, b'1', '系统日志', NULL, 'super_admin', b'0', b'0', b'1', b'0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `role`(`role`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, 'super_admin', '超级管理员');
INSERT INTO `sys_role` VALUES (3, 'test', '测试');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '真实名字',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电子邮件',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `introduction` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '介绍',
  `state` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态',
  `add_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- initial password: Sswz+886
-- INSERT INTO `sys_user` VALUES (69, 'admin', '管理员', 'admin@admin.com', '76a5b0b692aecba9cf004b8383c8e3e089eec0f7d7a80405f9d461d35d19932e', 'jahKooG9', NULL, '管理员账号', b'1', '2020-12-26 01:03:02');

-- initial password: abcd123490
INSERT INTO `sys_user` VALUES (69, 'admin', '管理员', 'admin@admin.com', '9defd1d9cba7095422e7d76103632b74da02eefaa7ec0089eadb212100459ea8', 'Bck3xXws', NULL, '管理员账号', b'1', '2020-12-26 01:03:02');

-- ----------------------------
-- Table structure for user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `user_role_rel`;
CREATE TABLE `user_role_rel`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int(11) NOT NULL COMMENT '用户编号',
  `role_id` int(11) NOT NULL COMMENT '角色编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user_role_rel
-- ----------------------------
INSERT INTO `user_role_rel` VALUES (3, 69, 2);

SET FOREIGN_KEY_CHECKS = 1;
