package prode;

import org.javalite.activejdbc.Model;

public class Score extends Model{

	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("user_id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("points").allowNull(false);
	}

	/*Constructor*/
	public Score(){

	}

	/*Constructor*/
	public Score(int id_user, int pun){
		set("user_id", id_user);
		set("points",  pun);
	}
}