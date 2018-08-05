/*TABLA LA CUAL NOS PERMITE GUARDAR LA PUNTUACION DE UN USUARIO EN EL PRODE*/

CREATE TABLE scores(
	id         int(10) auto_increment PRIMARY KEY, /*ATRIBUTO EL CUAL NOS PERMITE IDENTIFICAR UNIVOCAMENTE UNA PUNTUACION*/
	user_id    int(11), 						   /*ATRIBUTO EL CUAL HACE REFERENCIA AL USUARIO AL QUE PERTENECE DICHA PUNTUACION ES DECIRES FOREIGN KEY DE LA TABLA USERS*/
	schedure_id int(10),
	points 	   int(20)
)ENGINE=InnoDB;