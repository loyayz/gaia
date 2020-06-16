CREATE TABLE `sys_dict`
(
    `id`           bigint(20) unsigned NOT NULL,
    `group_name`   varchar(50)         NOT NULL COMMENT '分组名',
    `code`         varchar(20)         NOT NULL COMMENT '字典编码',
    `name`         varchar(50)         NOT NULL COMMENT '字典名',
    `sort`         int(11)             NOT NULL COMMENT '序号',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_sd_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据字典';

CREATE TABLE `sys_dict_item`
(
    `id`           bigint(20) unsigned NOT NULL,
    `dict_code`    varchar(20)         NOT NULL COMMENT '字典编码',
    `name`         varchar(50)         NOT NULL COMMENT '数据名',
    `value`        varchar(50)         NOT NULL COMMENT '数据值',
    `sort`         int(11)             NOT NULL COMMENT '序号',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_sdi_code` (`dict_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='数据字典项';

CREATE TABLE `sys_setting`
(
    `id`           bigint(20) unsigned NOT NULL,
    `code`         varchar(20)         NOT NULL COMMENT '编码',
    `name`         varchar(50)         NOT NULL COMMENT '名称',
    `value`        varchar(500)        NOT NULL COMMENT '值',
    `gmt_create`   datetime DEFAULT NULL,
    `gmt_modified` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_ss_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统配置';
