select * from predictions;
delete from predictions where id > 0;

select user_id, sum(points) as suma from scores group by user_id order by suma desc;