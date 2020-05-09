DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id      BIGINT(20)  NOT NULL COMMENT '主键ID',
    name    VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age     INT(11)     NULL DEFAULT NULL COMMENT '年龄',
    email   VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    role_id BIGINT(20)  NULL DEFAULT NULL COMMENT '角色ID',
    PRIMARY KEY (id)
);

CREATE TABLE person
(
    id           BIGINT UNSIGNED NOT NULL,
    name         VARCHAR(20) COMMENT '姓名',
    gmt_create   DATETIME,
    gmt_modified DATETIME,
    PRIMARY KEY (`id`)
);
