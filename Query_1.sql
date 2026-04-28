ALTER TABLE bookings
    MODIFY COLUMN check_in_date datetime NOT NULL COMMENT '入住日期',
    MODIFY COLUMN check_out_date datetime NOT NULL COMMENT '退房日期';

ALTER TABLE bookings
MODIFY COLUMN check_in_date datetime NOT NULL COMMENT '入住日期',
MODIFY COLUMN check_out_date datetime NOT NULL COMMENT '退房日期';

ALTER TABLE bookings MODIFY COLUMN check_out_date datetime NULL COMMENT '退房日期';