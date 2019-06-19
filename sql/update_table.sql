//更新表结构
ALTER TABLE `pay361`.`agent_info` 
ADD COLUMN `login_error_count` int(4) DEFAULT 0 COMMENT '登录错误次数' AFTER `last_login_time`;
ALTER TABLE `pay361`.`platform_admin_info` 
ADD COLUMN `login_error_count` int(4) DEFAULT 0 COMMENT '登录错误次数' AFTER `last_login_time`;
ALTER TABLE `pay361`.`shop_user_info` 
ADD COLUMN `login_error_count` int(4) DEFAULT 0 COMMENT '登录错误次数' AFTER `sub_open_key`;

//2019-06-17
ALTER TABLE `361pay_db`.`passageway_info` 
ADD COLUMN `mapping_flag` int(2) DEFAULT 0 COMMENT '是否启用为映射通道  0 否  1 是' AFTER `income_type`;