
package prode;

import org.javalite.activejdbc.Model;

public class Schedure extends Model{
	
	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("fixture_id").allowNull(false);
	}

	/*Constructor*/
	public Schedure(){

	}

	/*Cnstructor*/
	public Schedure(int id){
		set("fixture_id", id);
	}
}