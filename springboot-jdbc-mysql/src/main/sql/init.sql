CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增id',
    name VARCHAR(100) default null COMMENT '姓名',
    email VARCHAR(100) default null COMMENT '邮箱'
)ENGINE=InnoDB DEFAULT CHARSET=utf8  COMMENT = '用户表';