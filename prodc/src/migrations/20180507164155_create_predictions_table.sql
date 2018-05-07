CREATE TABLE predictions(
	id          int(10) auto_increment PRIMARY KEY,
	result      int(1),
	user_id     int(10),	/*Clave Foranea la cual hace referencua al usuario que creo la prediccion*/
	schedure_id int(10) 	/*Clave Foranea la cual hace referencia a la fecha la cual pertenece la prediccion*/
)ENGINE=InnoDB;