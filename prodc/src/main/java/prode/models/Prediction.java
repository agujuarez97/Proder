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
	* @return result of the prediction
	*/
	public Integer getResult(){
		return this.getInteger("result");
	}
	
	/**
	* @return the user who made the prediction
	*/
	public User getUser(){
		return User.findById(this.get("user_id"));
	}
	
	/**
	* @return the game in which the prediction is made
	*/
	public Game getGame(){
		return Game.findById(this.get("game_id"));
	}
	
	/**
	* @return the schedure corresponding to the prediction
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
		m.put("result",this.getResult());
		m.put("user",this.getUser());
		m.put("game",this.getGame());
		m.put("schedure",this.getSchedure());
		return m;
	}
}
