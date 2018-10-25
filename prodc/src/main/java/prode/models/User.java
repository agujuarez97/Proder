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

	/**
	 * User register.
	 * @param req
	 * @param res
	 * @return
	 */
	public Map registerUser(Request req, Response res){
	    String[] result = {req.queryParams("username"),(String)req.queryParams("pas")};
	    String body = req.body();
	    Map questt = new HashMap();

	    List<User> unico = User.where("username = ? or password = ?", result[0], result[1]);
	    Boolean result2 = unico.size()==0;

		if(result2){
			User u = new User(result[0], (String)req.queryParams("pas"));
		    u.saveIt();
		    return questt;
		}
		else{
			User u = unico.get(0);
		    String e = (String)u.get("username");
		    String p = (String)u.get("password");
		    if(e.equals(result[0]) && !(p.equals(result[1]))){
		    	questt.put("error","<div class='alert alert-danger'><strong>Error!</strong> Nombre de usuario existente.</div>");
		        return questt;
		    }
		    else{
		    	if(!(e.equals(result[0])) && p.equals(result[1])){
		      		questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Contraseña existente.</div>");
		      		return questt;
		      	}
		      	else{
		      		questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Usuario existente.</div>");
		      		return questt;
		      	}
		    }
		}
  }

	/**
	 * User check.
	 * @param req
	 * @param res
	 * @return
	 */
  public Map checkUser(Request req, Response res){
  	String[] result = {req.queryParams("username"),(String)req.queryParams("password")};
	String body = req.body();
	Map questt = new HashMap();

	List<User> unico = User.where("username = ? or password = ?", result[0], result[1]);
	Boolean resu2 = unico.size()!=0;
	if(resu2){
		User u = unico.get(0);
		String e = (String)u.get("username");
		String p = (String)u.get("password");
		if(e.equals(result[0]) && p.equals(result[1])){
			questt.put("user", unico.get(0).get("id"));
			return questt;
		}
		else{
			if(!(e.equals(result[0])) && p.equals(result[1])){
				questt.put("user", 0);
				questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Usuario incorrecto.</div>");
				return questt;
			}
			else{
				questt.put("user", 0);
				questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Contraseña incorrecta.</div>");
				return questt;
			}
		}
		
	}
	else{
		questt.put("user", 0);
		questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Usuario no existente.</div>");
		return questt;
	}
  }

}
