/**
 * Title: Result.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package models;

import java.util.*;
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

	/**
	 * @return the result
	 */
	public Integer getResult(){
		return this.getInteger("result");
	}

	/**
	* @return all the information of the result
	*/
	public Map getCompleteResult(){
		Map m = new HashMap();
		m.put("id", this.getId());
		m.put("resultado", this.getResult());
		return m;
	}
}