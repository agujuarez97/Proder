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
	* @return the user to whom the score belongs
	*/
	public User getUser(){
		return User.findById(this.get("user_id"));
	}
	
	/**
	* @return the points obtained by the user
	*/
	public int getPoints(){
		return this.getInteger("points");
	}
	
	/**
	* @return the schedure to which the points correspond
	*/
	public Schedure getSchedure(){
		return Schedure.findById(this.get("schedure_id"));
	}
	
	/**
	* @return all the information of the prediction
	*/
	public Map getCompletePrediction(){
		Map m = new HashMap();
		m.put("id",this.getId());
		m.put("user",this.getUser());
		m.put("points",this.getPoints());
		m.put("schedure",this.getSchedure());
		return m;
	}
}
