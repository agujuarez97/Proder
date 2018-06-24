select * from scores;


select user_id, sum(points) as suma from scores group by user_id order by suma desc;