package prode;

import org.javalite.activejdbc.Model;

public class Game extends Model{
	
	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("id").allowNull(false);

		/*Evalua que la cantidad de goles del equipo local sea valida es decir un entero, y no permite que sea nulo*/
		validateNumericalityOf("goalLocal").allowNull(false);

		/*Evalua que la cantidad de goles del equipo visitante sea valida es decir un entero, y no permite que sea nulo*/
		validateNumericalityOf("goalVisitor").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("team_local_id").allowNull(false);

		validateNumericalityOf("team_visitante_id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("schedure_id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("result_id").allowNull(false);
	}

	/*Constructor*/
	public Game(){

	}

	/*Constructor*/
	public Game(int gl, int gv, int id_tl, int id_tv, int id_f, int id_r){
		set("golLocal", gl);
		set("golVisitante", gv);
		set("team_local_id", id_tl);
		set("team_visitante_id", id_tv);
		set("schedure_id", id_f);
		set("result_id", id_r);
	}
}