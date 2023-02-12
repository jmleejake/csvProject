/*190630 청구서 작업을 편하게 하기위한 테이블*/
create table tb_time (
seq_id bigint unsigned primary key auto_increment /*区分ID*/
, start_date datetime /*일 시작시간*/
, end_date datetime /*일 종료시간*/
, work_time varchar(10) /*워킹아워*/
, dbsts varchar(1) default 'S' /*status: 작업시작(S) 작업종료(E)*/
) default charset = utf8;



/*190630 청구서작업을 위한 테이블 테스트*/
insert into tb_time (start_date)
values (now());

select * from tb_time;

update tb_time
set end_date = now()
, work_time = timediff(end_date, start_date)
, dbsts = 'E'
where seq_id = 7;

SELECT DATE_ADD(now(), INTERVAL -1 DAY); 

/*//190630 청구서작업을 위한 테이블 테스트*/


alter table rakuten_frozen_info modify column order_no varchar(40) comment '注文番号';

alter table rakuten_frozen_info modify column order_tel1 varchar(8) comment '注文者電話番号1';
alter table rakuten_frozen_info modify column order_tel2 varchar(8) comment '注文者電話番号2';
alter table rakuten_frozen_info modify column order_tel3 varchar(8) comment '注文者電話番号3';

alter table rakuten_frozen_info modify column delivery_tel1 varchar(8) comment '送付先電話番号1';
alter table rakuten_frozen_info modify column delivery_tel2 varchar(8) comment '送付先電話番号2';
alter table rakuten_frozen_info modify column delivery_tel3 varchar(8) comment '送付先電話番号3';


alter table rakuten_info modify column order_no varchar(40) comment '注文番号';

alter table rakuten_info modify column order_tel1 varchar(8) comment '注文者電話番号1';
alter table rakuten_info modify column order_tel2 varchar(8) comment '注文者電話番号2';
alter table rakuten_info modify column order_tel3 varchar(8) comment '注文者電話番号3';

alter table rakuten_info modify column delivery_tel1 varchar(8) comment '送付先電話番号1';
alter table rakuten_info modify column delivery_tel2 varchar(8) comment '送付先電話番号2';
alter table rakuten_info modify column delivery_tel3 varchar(8) comment '送付先電話番号3';

select order_no,order_tel1,order_tel2,order_tel3 from rakuten_info;