package prode;

import org.javalite.activejdbc.Model;

public class Fixture extends Model{

	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("id").allowNull(false);

		/*Evalua que el nombre que tengo en la columna sea valido(no vacio)*/
		validatePresenceOf("name");
	}
}