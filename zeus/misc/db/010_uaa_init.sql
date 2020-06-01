CREATE TABLE `uaa_user`
(
    `id`           bigint(20) unsigned NOT NULL,
    `name`         varchar(50)         NOT NULL COMMENT '用户名',
    `mobile`       varchar(20)                  DEFAULT NULL COMMENT '电话',
    `email`        varchar(100)                 DEFAULT NULL COMMENT '邮箱',
    `info`         text COMMENT '详情',
    `locked`       tinyint(4)          NOT NULL DEFAULT '0' COMMENT '是否锁定',
    `deleted`      tinyint(4)          NOT NULL DEFAULT '0' COMMENT '是否删除',
    `gmt_create`   datetime                     DEFAULT NULL,
    `gmt_modified` datetime                     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uu_del` (`deleted`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户';

CREATE TABLE `uaa_user_account`
(
    `id`           bigint(20) unsigned NOT NULL,
    `user_id`      bigint(20) unsigned NOT NULL COMMENT '用户',
    `type`         varchar(20)         NOT NULL COMMENT '账号类型',
    `name`         varchar(50)         NOT NULL COMMENT '账号名',
    `password`     varchar(255)        NOT NULL COMMENT '密码',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_uua_name` (`type`, `name`),
    KEY `idx_uua_uid` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户账号';

CREATE TABLE `uaa_user_role`
(
    `id`           bigint(20) unsigned NOT NULL,
    `user_id`      bigint(20) unsigned NOT NULL COMMENT '用户',
    `role_id`      bigint(20) unsigned NOT NULL COMMENT '角色',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uur_uid` (`user_id`),
    KEY `idx_uur_rid` (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色';

CREATE TABLE `uaa_role_permission`
(
    `id`           bigint(20) unsigned NOT NULL,
    `role_id`      bigint(20) unsigned NOT NULL COMMENT '角色编码',
    `type`         varchar(20)         NOT NULL COMMENT '权限类型',
    `ref_id`       bigint(20) unsigned NOT NULL COMMENT '关联对象',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_urp_role` (`role_id`, `type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色权限';

CREATE TABLE `uaa_app`
(
    `id`           bigint(20) unsigned NOT NULL,
    `name`         varchar(50)         NOT NULL COMMENT '名称',
    `remote`       tinyint(4)          NOT NULL DEFAULT '0' COMMENT '远程组件',
    `url`          varchar(100)        NOT NULL COMMENT '地址',
    `remark`       varchar(200)                 DEFAULT NULL COMMENT '备注',
    `sort`         int(11)             NOT NULL COMMENT '序号',
    `gmt_create`   datetime                     DEFAULT NULL,
    `gmt_modified` datetime                     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='应用';

CREATE TABLE `uaa_app_role`
(
    `id`           bigint(20) unsigned NOT NULL,
    `app_id`       bigint(20) unsigned NOT NULL COMMENT '应用',
    `name`         varchar(50)         NOT NULL COMMENT '角色名',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uar_app` (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='应用角色';

CREATE TABLE `uaa_app_menu_meta`
(
    `id`           bigint(20) unsigned NOT NULL,
    `app_id`       bigint(20) unsigned NOT NULL COMMENT '应用',
    `pid`          bigint(20) unsigned NOT NULL COMMENT '上级菜单',
    `dir`          tinyint(4)          NOT NULL DEFAULT '0' COMMENT '是否目录',
    `code`         varchar(20)         NOT NULL COMMENT '编码',
    `name`         varchar(50)         NOT NULL COMMENT '名称',
    `url`          varchar(100)        NOT NULL COMMENT '链接',
    `icon`         varchar(50)         NOT NULL COMMENT '图标',
    `sort`         int(11)             NOT NULL COMMENT '序号',
    `gmt_create`   datetime                     DEFAULT NULL,
    `gmt_modified` datetime                     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uamm_app` (`app_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='应用菜单元数据';

CREATE TABLE `uaa_app_menu_action`
(
    `id`           bigint(20) unsigned NOT NULL,
    `menu_meta_id` bigint(20) unsigned NOT NULL COMMENT '菜单',
    `code`         varchar(20)         NOT NULL COMMENT '编码',
    `name`         varchar(50)         NOT NULL COMMENT '名称',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uama_menu` (`menu_meta_id`, `code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='应用菜单功能';

CREATE TABLE `uaa_menu`
(
    `id`           bigint(20) unsigned NOT NULL,
    `app_id`       bigint(20) unsigned NOT NULL COMMENT '应用',
    `pid`          bigint(20) unsigned NOT NULL COMMENT '上级id',
    `menu_meta_id` bigint(20) unsigned NOT NULL COMMENT '菜单元数据',
    `name`         VARCHAR(50)         NOT NULL COMMENT '名称',
    `icon`         VARCHAR(50)         NOT NULL COMMENT '图标',
    `hidden`       tinyint(4)          NOT NULL DEFAULT '0' COMMENT '是否隐藏',
    `sort`         INT(11)             NOT NULL COMMENT '序号',
    `gmt_create`   DATETIME                     DEFAULT NULL,
    `gmt_modified` DATETIME                     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_um_app` (`app_id`),
    KEY `idx_um_pid` (`pid`),
    KEY `idx_um_meta` (`menu_meta_id`),
    KEY `idx_um_hidden` (`hidden`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单';

