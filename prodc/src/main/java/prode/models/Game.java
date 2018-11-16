/**
 * Title: Game.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package models;

import java.util.*;
import org.javalite.activejdbc.Model;

public class Game extends Model{
	
	static{

		/**
		 * Evaluate the validity of the goals.
		 */
		validateNumericalityOf("goalLocal").allowNull(false);

		/**
		 * Evaluate the validity of the goals.
		 */
		validateNumericalityOf("goalVisitor").allowNull(false);

		/**
		 * Evaluate the validity of the local team.
		 */
		validateNumericalityOf("team_local_id").allowNull(false);

		/**
		 * Evaluate the validity of the visiting team.
		 */
		validateNumericalityOf("team_visitor_id").allowNull(false);

		/**
		 * Evaluate the validity of the schedure.
		 */
		validateNumericalityOf("schedure_id").allowNull(false);

		/**
		 * Evaluate the validity of the result.
		 */
		validateNumericalityOf("result_id").allowNull(false);
	}

	/**
	 * Builder
	 */
	public Game(){

	}

	/**
	 * Builder
	 * @param gl
	 * @param gv
	 * @param id_tl
	 * @param id_tv
	 * @param id_f
	 * @param id_r
	 */
	public Game(String date, String hr, int gl, int gv, int id_tl, int id_tv, int id_f, int id_r){
		set("day_game", date);
		set("hour_game", hr);
		set("goalLocal", gl);
		set("goalVisitor", gv);
		set("team_local_id", id_tl);
		set("team_visitor_id", id_tv);
		set("schedure_id", id_f);
		set("result_id", id_r);
	}
	
	/**
	* @return the date of the game
	*/
	public String getDate(){
		return this.getString("day_game");
	}
	
	/**
	* @return the hour of the game
	*/
	public String getHour(){
		return this.getString("hour_game");
	}

	/**
	* @return the goals converted by the local team
	*/
	public Integer getGoalLocal(){
		return this.getInteger("goalLocal");
	}

	/**
	* @return the goals converted by the visiting team
	*/
	public Integer getGoalVisitor(){
		return this.getInteger("goalVisitor");
	}

	/**
	* @return the shcedure of the match
	*/
	public Schedure getSchedure(){
		return Schedure.findById(this.get("schedure_id"));
	}

	/**
	* @return all the information of the visiting team
	*/
	public Team getTeamVisit(){
		return Team.findById(this.get("team_visitor_id"));
	}

	/**
	* @return all the information of the local team
	*/
	public Team getTeamLocal(){
		return Team.findById(this.get("team_local_id"));
	}

	/**
	* @return result of the game
	*/
	public Result getResult(){
		return Result.findById(this.get("result_id"));
	}

	/**
	* @return all the information of the game
	*/
	public Map getCompleteGame(){
		Map m = new HashMap();
		m.put("id",this.getId());
		m.put("date", this.getDate());
		m.put("hour", this.getHour());
		m.put("golLocal",this.getGoalLocal());
		m.put("golVisitante",this.getGoalVisitor());
		m.put("local", this.getTeamLocal());
		m.put("visitante",this.getTeamVisit());
		m.put("result", this.getResult());
		return m;
	}
}
