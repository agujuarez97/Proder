package prode;

import org.javalite.activejdbc.Model;

public class Result extends Model{
	
	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("result").allowNull(false);
	}

	/*Constructor*/
	public Result(){

	}

	/*Constructor*/
	public Result(int res){
		set("result", res);
	}
}