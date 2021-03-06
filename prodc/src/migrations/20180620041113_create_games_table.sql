
/*TABLA LA CUAL NOS PERMITE GUARDAR TODA LA INFORMACION DE LAS PARTIDOS*/

CREATE TABLE games(
	id           int(10) auto_increment PRIMARY KEY, /*ATRIBUTO EL CUAL PERMITE IDENTIFICAR UNIVOCAMENTE UN JUEGO*/
	day_game     varchar(10),
	hour_game    varchar(5),
	goalLocal    int check (goalLocal>=0),
	goalVisitor  int check (goalVisitor>=0),
	team_local_id      int(11), 					/*ATRIBUTO EL CUAL HACE REFERENCIA A CUAL ES EL EQUIPO LOCAL QUE TIENE EL JUEGO, ES DECIR ES FOREIGN KEY DE LA TABLA TEAMS*/
	team_visitor_id	   int(11),						/*ATRIBUTO EL CUAL HACE REFERENCIA A CUAL ES EL EQUIPO VISITANTE QUE TIENE EL JUEGO, ES DECIR ES FOREIGN KEY DE LA TABLA TEAMS*/
	schedure_id  int(10), 							/*ATRIBUTO EL CUAL HACE REFERENCIA A CUAL ES LA FECHA EN AL QEU SE JUEGA EN JUEGO, ES DECIR ES FOREIGN KET DE LA TABLA SCHEDURES*/
	result_id	 int(10),							/*ATRUBUTO EL CUAL HACE RECERENCIA A CUAL FUE EL RESULTADO DEL PARTIDO, ES DECIR ES FOREIGN KEY DE LA TABLA RESULTS*/
	created_at   DATETIME,
	updated_at   DATETIME
)ENGINE=InnoDB;
