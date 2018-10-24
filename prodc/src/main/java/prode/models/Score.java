/**
 * Titulo: Score.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */
package models;

import java.util.*;
import org.javalite.activejdbc.Model;
import models.Prediction;
import models.Result;
import models.Game;

public class Score extends Model{

	static{

		/**
		 * Evaluate the validity of the user.
		 */
		validateNumericalityOf("user_id").allowNull(false);

		/**
		 * Evaluate the validity of the points.
		 */
		validateNumericalityOf("points").allowNull(false);

		/**
		 * Evaluate the validity of the schedure.
		 */
		validateNumericalityOf("schedure_id").allowNull(false);
	}

	/**
	 * Builder.
	 */
	public Score(){

	}

	/**
	 * Builder.
	 * @param id_user
	 * @param pun
	 * @param id_sch
	 */
	public Score(int id_user, int pun, int id_sch){
		set("user_id", id_user);
		set("points",  pun);
		set("schedure_id", id_sch);
	}

	/**
	 * Calculate the score
	 * @param id_usu
	 * @param fecha
	 */
	public void calculateScore(int id_usu, int fecha){
		List<Prediction> predicciones = Prediction.where("user_id = ? and schedure_id = ?", id_usu, fecha);

		Score s = new Score(id_usu, 0, fecha);
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
		s.saveIt();
	}
}