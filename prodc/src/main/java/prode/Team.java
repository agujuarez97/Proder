package prode;

import org.javalite.activejdbc.Model;

public class Team extends Model{

	static{

		/*Evalua que el nombre que tengo en la columna sea valido(no vacio) en caso contrario tira un mensaje avisando*/
    	validatePresenceOf("name").message("Please, provide the name of the team");
  	}

  	/*Constructor*/
 	  public Team(){

    }

  	/*Constructor*/
  	public Team(String nom){
  		set("name", nom);
  	}
}