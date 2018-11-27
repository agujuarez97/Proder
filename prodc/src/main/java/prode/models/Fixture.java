/**
 * Title: Fixture.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */
package models;

import org.javalite.activejdbc.Model;
import java.util.*;

public class Fixture extends Model{

	static{
		
		
		/**
		 * Evaluate the validity of the name.
		 */
		validatePresenceOf("name");
	}

	/**
	 * Builder.
	 */	
	public Fixture(){

	}

	/**
	 * Builder.
	 * @param name.
	 */
	public Fixture(String name){
		set("name", name);
	}

	/**
	* @return the name of the fixture
	*/
	public String getName(){
		return this.getString("name");
	}
	
	/**
	* @return all the information of the fixture
	*/
	public Map getCompleteFixture(){
		Map m = new HashMap();
		m.put("id",this.getId());
		m.put("name", this.getName());
		return m;
	}

}
