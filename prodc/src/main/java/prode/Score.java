package prode;

import java.util.*;
import org.javalite.activejdbc.Model;
import prode.Prediction;
import prode.Result;
import prode.Game;

public class Score extends Model{

	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("user_id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("points").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("schedure_id").allowNull(true);
	}

	/*Constructor*/
	public Score(){

	}

	/*Constructor*/
	public Score(int id_user, int pun){
		set("user_id", id_user);
	//	set("schedure_id", id_sch);
		set("points",  pun);
	}


 	/*CALCULA LOS PUNTOS EN TOTAL QUE OBTUVO UN DETERMINADO USUARIO*/
	public void calculateScore(int id_usu){

		List<Prediction> predicciones = Prediction.where("user_id = ? ", id_usu);

		List<Score> s = Score.where("user_id = ?", id_usu);

	   	if(s.size() != 0){

	   		int aux = 0;

			for (int i = 0; i < predicciones.size(); i++) {

				int id = (Integer)(predicciones.get(i)).get("game_id");
				List<Game> g = Game.where("id = ? ", id);
				List<Result> r = Result.where("id = ?", (Integer)g.get(0).get("result_id"));
				int resg = (Integer)r.get(0).get("result");
				int resp = (Integer)(predicciones.get(i)).get("result");
				
				if(resg == resp){
					aux += 1;
				}
			}
			s.get(0).set("points", aux).saveIt();
		} else {

			Score nuevoscore = new Score(id_usu, 0);
		   	int aux = 0;

			for (int i = 0; i < predicciones.size(); i++) {

				int id = (Integer)(predicciones.get(i)).get("game_id");
				List<Game> g = Game.where("id = ? ", id);
				List<Result> r = Result.where("id = ?", (Integer)g.get(0).get("result_id"));
				int resg = (Integer)r.get(0).get("result");
				int resp = (Integer)(predicciones.get(i)).get("result");
				
				if(resg == resp){
					aux += 1;
				}
			}
			nuevoscore.set("points", aux);
			nuevoscore.saveIt();
		}
	}

	/*public void calculateScore(int id_usu, int fecha){
		List<Prediction> predicciones = Prediction.where("user_id = ? and schedure_id = ?", id_usu, fecha);

		Score s = new Score(id_usu, fecha, 0);
	   	int aux = 0;

		for (int i = 0; i < predicciones.size(); i++) {

			int id = (Integer)(predicciones.get(i)).get("game_id");
			List<Game> g = Game.where("id = ? and schedure_id = ?", id, fecha);
			List<Result> r = Result.where("id = ?", (Integer)g.get(0).get("result_id"));
			int resg = (Integer)r.get(0).get("result");
			int resp = (Integer)(predicciones.get(i)).get("result");
			
			if(resg == resp){
				aux += 1;
			}
		}
		s.set("points", aux);
		s.saveIt();*/
}