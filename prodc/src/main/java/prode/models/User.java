/**
 * Title: User.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package models;

import org.javalite.activejdbc.Model;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;
import models.Score;

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
}
