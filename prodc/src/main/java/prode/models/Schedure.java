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
		
		/**
		 * Evaluate the validity of the number.
		 */
		validateNumericalityOf("num").allowNull(false);
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
	public Schedure(int id, int num){
		set("fixture_id", id);
		set("num", num);
	}
	
	/**
	* @return the number of the schedule
	*/
	public Integer getNumber(){
		return this.getInteger("num");
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
		m.put("number",this.getNumber());
		m.put("fixture", this.getFixture());
		return m;
	}
}
