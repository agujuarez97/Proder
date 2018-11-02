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
}