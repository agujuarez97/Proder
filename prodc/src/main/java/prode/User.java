package prode;

import org.javalite.activejdbc.Model;

public class User extends Model {

	static{
		/*Evalua que el nombre que tengo en la columna sea valido(no vacio) en caso contrario tira un mensaje avisando*/
		validatePresenceOf("username").message("Please, provide your username");

		/*Evalua que la contraseña que tengo en la columna sea valida(no vacia) en caso contrario tira un mensaje avisando*/
	    validatePresenceOf("password").message("Please, provide your password ");

	    /*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("fixture_id ").allowNull(false);
	}

	/*Constructor sin parametros*/
	public User(){

	}

	/*Constructor con parametros*/
	public User(String name, String pas){
		set("username", name);
		set("password", pas);
	}

}
