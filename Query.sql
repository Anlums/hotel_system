
# 清除数据
TRUNCATE TABLE bookings;
TRUNCATE TABLE rooms;

# ctrl + b
-- auto-generated definition
create table rooms
(
    id          bigint auto_increment comment '房间ID'
        primary key,
    room_number bigint                             not null,
    type        varchar(50)                        not null comment '房型 (如: 单人间, 双人间, 总统套房)',
    price       decimal(10, 2)                     not null comment '每晚价格',
    status      tinyint  default 0                 null comment '状态: 0-空闲, 1-已预订, 2-维护中',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint room_number
        unique (room_number)
)
    comment '房间信息表';
-- auto-generated definition
create table bookings
(
    id             bigint auto_increment comment '预订ID'
        primary key,
    room_number    bigint                             not null comment '房间编号',
    guest_name     varchar(50)                        not null comment '入住人姓名',
    phone          varchar(20)                        not null comment '联系电话',
    check_in_date  date                               not null comment '入住日期',
    check_out_date date                               not null comment '退房日期',
    total_amount   decimal(10, 2)                     not null comment '订单总金额',
    status         tinyint  default 1                 null comment '订单状态: 1-已预约, 2-已入住, 3-已取消',
    create_time    datetime default CURRENT_TIMESTAMP null,
    constraint fk_bookings_room_number
        foreign key (room_number) references rooms (room_number)
)
    comment '预订记录表';

create index idx_room_id
    on bookings (room_number);


ALTER TABLE bookings                    -- 修改 bookings 表
    ADD CONSTRAINT fk_bookings_room_number  -- 添加一个名为 fk_bookings_room_number 的约束
        FOREIGN KEY (room_number)               -- 将 room_number 字段设为外键
            REFERENCES rooms(room_number);          -- 引用 rooms 表的 room_number 字段
UPDATE rooms
SET type = '爱情房间'
WHERE type = '亲情房间';


# -- 修改字段名
# ALTER TABLE rooms CHANGE COLUMN room_nubmer room_number bigint auto_increment comment '房间号';
# alter table bookings change column id booking_id bigint auto_increment comment '订单ID'
#     primary key;
# alter table bookings change column room_id room_number bigint not null comment '房间编号';
#
# INSERT INTO `rooms` (`room_id`, `type`, `price`, `status`) VALUES
#    ('101', '标准单人间', 199.00, 0),
#    ('201', '商务双人间', 350.00, 0),
#    ('301', '豪华大床房', 588.00, 0),
#    ('888', '总统套房', 1888.00, 0);


# -- 先删除旧主键(如果有)
# ALTER TABLE rooms DROP PRIMARY KEY;
#
# -- 修改字段并添加主键
# ALTER TABLE rooms
#     CHANGE COLUMN rooms_id id BIGINT NOT NULL AUTO_INCREMENT COMMENT '房间ID',
#     ADD PRIMARY KEY (id);
