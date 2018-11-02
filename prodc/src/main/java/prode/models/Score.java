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
}