CREATE TABLE games(
	id int(10) auto_increment PRIMARY KEY not null,
	fecha date,
	hora DATETIME,
	golLocal int check (golLocal>=0),
	golVisitante int check (golVisitante>=0),
	team_id int(11), --  FK hace referencia a eElocal
	team_id int(11), -- FK hace referencia a Evisitante
	schedure_id int(10), -- FK hace referencia a Fecha
	created_at DATETIME,
	updated_at DATETIME
	
)ENGINE=InnoDB;

-- Ver como refereniar 2 FK de la misma tabla