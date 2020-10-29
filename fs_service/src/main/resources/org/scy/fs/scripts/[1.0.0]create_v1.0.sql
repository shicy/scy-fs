-- ========================================================
-- 文件系统
-- Author shicy
-- Created on <2020-10-28>
-- ========================================================

-- CREATE SCHEMA `db_fs` DEFAULT CHARACTER SET utf8mb4 ;
-- USE `db_fs`;


-- -----------------------------------------------------
-- Table `register`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `register` (
  `id` INT NOT NULL COMMENT '编号',
  `key` CHAR(32) NOT NULL,
  `name` VARCHAR(100) NULL COMMENT '名称',
  `size` BIGINT NULL COMMENT '当前使用容量',
  `limit` BIGINT NULL COMMENT '允许最大容量',
  `state` TINYINT NULL COMMENT '状态，0-无效 1-有效',
  `createTime` BIGINT NULL,
  `updateTime` BIGINT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `u_register_key_idx` (`key` ASC))
COMMENT = '注册表';


-- -----------------------------------------------------
-- Table `entity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `entity` (
  `id` INT NOT NULL COMMENT '编号',
  `key` CHAR(32) NOT NULL,
  `uuid` CHAR(32) NULL COMMENT '唯一编号',
  `name` VARCHAR(200) NULL COMMENT '名称',
  `ext` VARCHAR(20) NULL COMMENT '后缀名称',
  `size` BIGINT NOT NULL COMMENT '文件大小',
  `directory` TINYINT NULL COMMENT '是不是目录，0-文件 1-目录',
  `parentId` INT NOT NULL COMMENT '上级编号',
  `parentIds` VARCHAR(150) NULL COMMENT '所有上级编号集',
  `state` TINYINT NULL COMMENT '状态，0-无效 1-有效',
  `createTime` BIGINT NULL,
  `updateTime` BIGINT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `u_entity_uuid_idx` (`uuid` ASC),
  INDEX `n_entity_name_idx` (`name` ASC))
COMMENT = '注册表';
