CREATE TABLE results(
	id      int(20) auto_increment PRIMARY KEY,
	game_id int(10), /*Clave Foranea la cual hace referencia al partido que corresponde dicho resultado*/
	result  int(1)
)ENGINE=InnoDB;
