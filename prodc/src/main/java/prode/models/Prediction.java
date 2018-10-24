/**
 * Title: Prediction.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package models;

import java.util.*;
import org.javalite.activejdbc.Model;
import spark.Request;
import spark.Response;
import models.Score;

public class Prediction extends Model{
	
	static{

		/**
		 * Evaluate the validity of the result.
		 */
		validateNumericalityOf("result").allowNull(false);

		/**
		 * Evaluate the validity of the user.
		 */
		validateNumericalityOf("user_id").allowNull(false);

		/**
		 *  Evaluate the validity of the game.
		 */
		validateNumericalityOf("game_id").allowNull(false);
	}

	/**
	 *  Builder.
	 */
	public Prediction(){

	}

	/**
	 * Builder.
	 * @param re
	 * @param id_u
	 * @param id_g
	 * @param id_s
	 */
	public Prediction(int re, int id_u, int id_g, int id_s){
		set("result", re);
		set("user_id", id_u);
		set("game_id", id_g);
		set("schedure_id", id_s);
	}

	/**
	 * Register a prediction.
	 * @param req
	 * @param res
	 */
	public void registerPrediction(Request req, Response res){

		String[] id = {req.queryParams("id1"), req.queryParams("id2"), req.queryParams("id3"), req.queryParams("id4"), req.queryParams("id5")};
		String[] result = {req.queryParams("partido1"), req.queryParams("partido2"), req.queryParams("partido3"), req.queryParams("partido4"), req.queryParams("partido5")};
		int fecha = Integer.parseInt(req.queryParams("f").toString());

		int id_u = (Integer)req.session().attribute("user");

		List<Prediction> prediction = Prediction.findBySQL("select * from predictions where user_id = ? and schedure_id = ?;", id_u, fecha);

		if (prediction.size() == 0){
			for (int i = 0; i < result.length; i++) {
				Prediction p = new Prediction(Integer.parseInt(result[i]), id_u, Integer.parseInt(id[i]), fecha);
				p.saveIt();
			}

			Score s = new Score();
			s.calculateScore(id_u, fecha);
		}
	}
}