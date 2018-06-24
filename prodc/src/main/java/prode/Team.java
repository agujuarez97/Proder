package prode;

import org.javalite.activejdbc.Model;

public class Team extends Model{

	static{

		/**
		 * Evaluate the validity of the team name.
		 */
    	validatePresenceOf("name").message("Please, provide the name of the team");
  	}

  	/**
  	 * Builder.
  	 */
 	  public Team(){

    }

  	/**
  	 * Builder.
  	 * @param nom
  	 */
  	public Team(String nom){
  		set("name", nom);
  	}
}