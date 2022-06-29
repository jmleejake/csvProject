/**
앵귤러 학습 자바스크립트!!

drop table tb_todo;
create table tb_todo (
	seq_id bigint unsigned primary key auto_increment comment '구분ID'
	,cntnt VARCHAR(500) comment '내용'
	,todo_end_yn CHAR(1) default 'F' comment '완료여부'
    ,reg_dt datetime default now() comment '등록일시'
    ,upd_dt datetime comment '수정일시'
) default charset = utf8 comment '할일목록';

 */
const app = angular.module('todo', []);