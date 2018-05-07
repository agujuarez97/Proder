CREATE TABLE predicts(
	prediction_id int(10), /*CLave Foranea la cual hace referencia a la prediccion*/
	result_id     int(10), /*Clave Foranea la cual hace referencia al resultado el cual correspode*/
	score_id      int(10), /*Clave Foranea la cual hace referencia a la puntuacion del usuario*/
	PRIMARY KEY (prediction_id,result_id)
)ENGINE=InnoDB;