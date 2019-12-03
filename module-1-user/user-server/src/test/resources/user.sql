
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE IF NOT EXISTS `t_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `nick_name` varchar(32) NOT NULL DEFAULT '' COMMENT '昵称',
  `avatar` varchar(256) NOT NULL DEFAULT '' COMMENT '头像',
  `gender` int NOT NULL DEFAULT '0' COMMENT '0.未知, 1.男, 2.女',
  `country` varchar(32) NOT NULL DEFAULT '' COMMENT '国家',
  `province` varchar(32) NOT NULL DEFAULT '' COMMENT '省份',
  `city` varchar(32) NOT NULL DEFAULT '' COMMENT '城市',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '1 表示已禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '1 已删除, 默认是 0 未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户';

/*
openId  是 微信号 与 公众平台 关联后得到的用户标识, 同一个用户对不同应用(如 app 和 小程序)的 openId 是不同的
unionId 是 微信号 与 开放平台 关联后得到的用户标识, 不同的应用可以放到不同的开放平台下, 这些不同应用(如 app 和 小程序)获取到的 unionId 是相同的

要用到 unionId 就需要把涉及到的 公众号、网站、app、小程序 都加入到同一个开放平台账号下

公众号: https://developers.weixin.qq.com/doc/offiaccount/User_Management/Get_users_basic_information_UnionID.html#UinonId
网站:   https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html
app:   https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Authorized_API_call_UnionID.html
小程序: https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html
       https://developers.weixin.qq.com/miniprogram/introduction/#%E5%B0%8F%E7%A8%8B%E5%BA%8F%E7%BB%91%E5%AE%9A%E5%BE%AE%E4%BF%A1%E5%BC%80%E6%94%BE%E5%B9%B3%E5%8F%B0%E5%B8%90%E5%8F%B7

公众平台: https://mp.weixin.qq.com   主要用来做公众号、小程序
开放平台: https://open.weixin.qq.com 主要用来做 app、网站
商户平台: https://pay.weixin.qq.com  主要用来做支付
*/

DROP TABLE IF EXISTS `t_user_auth`;
CREATE TABLE IF NOT EXISTS `t_user_auth` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL COMMENT '用户 id',
  `login_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0.站内登录, 1.第三方登录',
  `auth_type` int NOT NULL DEFAULT '1' COMMENT '1.用户名, 2.邮箱, 3.手机号, 4.weixin, 5.qq, 6.weibo',
  `identifier` varchar(32) NOT NULL COMMENT '标识(用户名, 邮箱地址, 手机号 或 第三方登录的 openId 等)',
  `credential` varchar(32) NOT NULL COMMENT '凭据(密码 或 第三方登录的凭证 access_token 等)',
  `third_expires` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '第三方登录的凭证超时时间',
  `third_refresh` varchar(32) NOT NULL DEFAULT '' COMMENT '第三方授权凭据扩展: refresh_token',
  `third_union_id` varchar(32) NOT NULL DEFAULT '' COMMENT 'union_id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户授权';


DROP TABLE IF EXISTS `t_user_login_history`;
CREATE TABLE IF NOT EXISTS `t_user_login_history` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL COMMENT '用户 id',
  `auth_type` int NOT NULL DEFAULT '1' COMMENT '1.用户名, 2.邮箱, 3.手机号, 4.weixin, 5.qq, 6.weibo',
  `login_type` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '0.站内登录, 1.第三方登录',
  `login_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '登录时间',
  `login_ip` bigint NOT NULL DEFAULT '' COMMENT '登录地址(使用整数存储 ip)',
  `login_user_agent` varchar(128) NOT NULL DEFAULT '' COMMENT '登录时的 user-agent',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`is_deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '1 已删除, 默认是 0 未删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录历史';
