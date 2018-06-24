/**
 * Title: Game.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package prode;

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
		validateNumericalityOf("team_visitante_id").allowNull(false);

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
	public Game(int gl, int gv, int id_tl, int id_tv, int id_f, int id_r){
		set("golLocal", gl);
		set("golVisitante", gv);
		set("team_local_id", id_tl);
		set("team_visitante_id", id_tv);
		set("schedure_id", id_f);
		set("result_id", id_r);
	}
}