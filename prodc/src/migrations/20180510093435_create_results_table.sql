/*TABLA LA CUAL NOS PERMITE GUARDAR LA INFORMACION SOBRE EL RESULTADO DE UN JUEGO*/

CREATE TABLE results(
	id      int(20) auto_increment PRIMARY KEY,	/*ATRIBUTO EL CUAL NOS PERMITE IDENTIFICAR UNIVOCAMENTE UN RESULTADO DE TODOS LOS EXISTENTES EN EL PRODE*/
	result  int(1)
)ENGINE=InnoDB;
