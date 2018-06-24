/**
 * Title: Fixture.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */
package prode;

import org.javalite.activejdbc.Model;

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

}