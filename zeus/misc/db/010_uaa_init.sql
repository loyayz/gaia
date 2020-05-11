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
    `user_id`      bigint(20)          NOT NULL COMMENT '用户',
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
    `role_code`    varchar(20)         NOT NULL COMMENT '角色',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uur_uid` (`user_id`),
    KEY `idx_uur_rid` (`role_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色';

CREATE TABLE `uaa_user_operation`
(
    `id`           bigint(20) unsigned NOT NULL,
    `user_id`      bigint(20) unsigned DEFAULT NULL COMMENT '用户',
    `type`         varchar(20)         DEFAULT NULL COMMENT '操作类型',
    `content`      text COMMENT '内容',
    `gmt_create`   datetime            DEFAULT NULL,
    `gmt_modified` datetime            DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_uuo_uid` (`user_id`),
    KEY `idx_uuo_type` (`type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户操作记录';

CREATE TABLE `uaa_role`
(
    `id`           bigint(20) unsigned NOT NULL,
    `code`         varchar(20)         NOT NULL COMMENT '编号',
    `name`         varchar(50)         NOT NULL COMMENT '角色名',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ur_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色';

