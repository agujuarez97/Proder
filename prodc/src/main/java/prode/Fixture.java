package prode;

import org.javalite.activejdbc.Model;

public class Fixture extends Model{

	static{

		/*Evalua que el nombre que tengo en la columna sea valido(no vacio)*/
		validatePresenceOf("name");
	}

	/*Contructor*/
	public Fixture(){

	}

	/*Constructor*/
	public Fixture(String name){
		set("name", name);
	}

}