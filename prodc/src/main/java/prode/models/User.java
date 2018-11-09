/**
 * Title: User.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package models;

import java.util.*;
import org.javalite.activejdbc.Model;

public class User extends Model {

	static{
		
		/**
		 * Evaluate the validity of the name.
		 */
		validatePresenceOf("username").message("Please, provide your username");

		/**
		 * Evaluate the validity of the password.
		 */
	    validatePresenceOf("password").message("Please, provide your password ");

	    /**
		 * Evaluate the validity of the fixture.
		 */
		validateNumericalityOf("fixture_id").allowNull(true);
	}

	/**
	 * Builder.
	 */
	public User(){

	}

	/**
	 * Builder.
	 * @param name
	 * @param pas
	 */
	public User(String name, String pas){
		set("username", name);
		set("password", pas);
	}

	/**
  	* @return the name of the user
  	*/
	public String getUserName(){
		return this.getString("username");
	}

	/**
  	* @return the password of the user
  	*/
	public String getPassword(){
		return this.getString("password");
	}

	/**
  	* @return all the information of the fixture
  	*/
	public Fixture getFixture(){
		return Fixture.findById(this.get("fixture_id"));
	}

	/**
	* @return all the information of the user
	*/
	public Map getCompleteUser(){
		Map m = new HashMap();
		m.put("id", this.getId());
		m.put("username", this.getUserName());
		m.put("password", this.getPassword());
		m.put("fixture", this.getFixture());
		return m;
	}
}
