/**
 * Title: Result.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package models;

import org.javalite.activejdbc.Model;

public class Result extends Model{
	
	static{

		/**
		 * Evaluate the validity of the result.
		 */
		validateNumericalityOf("result").allowNull(false);
	}

	/**
	 * Builder.
	 */
	public Result(){

	}

	/**
	 * Builder.
	 * @param res
	 */
	public Result(int res){
		set("result", res);
	}
}