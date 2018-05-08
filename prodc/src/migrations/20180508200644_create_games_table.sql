CREATE TABLE games(
	id           int(10) auto_increment PRIMARY KEY,
	datee        date,
	hour         DATETIME,
	golLocal     int check (golLocal>=0),
	golVisitante int check (golVisitante>=0),
	team_local_id      int(11), --  FK hace referencia a eElocal -- FK hace referencia a Evisitante
	team_visitante_id	   int(11),
	schedure_id  int(10), -- FK hace referencia a Fecha
	created_at   DATETIME,
	updated_at   DATETIME
)ENGINE=InnoDB;

-- Ver como refereniar 2 FK de la misma tabla
