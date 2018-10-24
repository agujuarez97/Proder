/**
 * Title: Schedure.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */
package models;

import org.javalite.activejdbc.Model;

public class Schedure extends Model{
	
	static{

		/**
		 * Evaluate the validity of the fixture.
		 */
		validateNumericalityOf("fixture_id").allowNull(false);
	}

	/**
	 * Builder.
	 */
	public Schedure(){

	}

	/**
	 * Builder.
	 * @param id
	 */
	public Schedure(int id){
		set("fixture_id", id);
	}
}