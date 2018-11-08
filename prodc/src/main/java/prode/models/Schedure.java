/**
 * Title: Schedure.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */
package models;

import java.util.*;
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

	/**
	* @return all the information of the fixture
	*/
	public Fixture getFixture(){
		return Fixture.findById(this.get("fixture_id"));
	}

	/**
	* @return all the information of the schedule
	*/
	public Map getCompleteSchedule(){
		Map m = new HashMap();
		m.put("id",this.getId());
		m.put("fixture", this.getFixture());
		return m;
	}
}