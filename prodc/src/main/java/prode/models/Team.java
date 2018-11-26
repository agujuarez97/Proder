/**
 * Title: Team.
 * This class extend Model.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */
package models;

import java.util.*;
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

    /**
  * @return the name of the team
  */
  public String getName(){
    return this.getString("name");
  }

  /**
  * @return all the information of the team
  */
  public Map getCompleteTeam(){
    Map m = new HashMap();
    m.put("id", this.getId());
    m.put("nombre", this.getName());
    return m;
  }
}