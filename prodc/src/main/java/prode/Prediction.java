package prode;

import org.javalite.activejdbc.Model;

public class Prediction extends Model{
	
	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("result").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("user_id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("game_id").allowNull(false);
	}

	/*Constructor*/
	public Prediction(){

	}

	/*Constructor*/
	public Prediction(int re, int id_u, int id_g){
		set("result", re);
		set("user_id", id_u);
		set("game_id", id_g);
	}
}