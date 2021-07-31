-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
    `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    `name` varchar(120) NOT NULL COMMENT '商品名称',
    `number` int NOT NULL COMMENT '库存数量',
    `start_time` timestamp DEFAULT '0000-00-00 00:00:00' NOT NULL COMMENT '秒杀开启时间',
    `end_time` timestamp DEFAULT '0000-00-00 00:00:00' NOT NULL COMMENT '秒杀结束时间',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (seckill_id),
    key idx_start_time(start_time),
    key idx_end_time(end_time),
    key idx_create_time(create_time)
) ENGINE=InnoDB AUTO_INCREMENT 1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据
INSERT INTO
    seckill(name, number, start_time, end_time)
VALUES
    ('1000元秒杀iphone12', 100, '2021-07-26 00:00:00', '2021-07-27 00:00:00'),
    ('500元秒杀ipad8', 200, '2021-07-26 00:00:00', '2021-07-27 00:00:00'),
    ('300元秒杀小米11', 300, '2021-07-26 00:00:00', '2021-07-27 00:00:00'),
    ('200元秒杀红米Note10Pro', 400, '2021-07-26 00:00:00', '2021-07-27 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息
CREATE TABLE success_killed(
    `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
    `user_phone` bigint NOT NULL COMMENT '用户手机号',
    `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识，-1表示无效，0成功，1已付款，2已发货',
    `create_time` timestamp NOT NULL COMMENT '创建时间',
    PRIMARY KEY (seckill_id, user_phone),
    key idx_create_time(create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';