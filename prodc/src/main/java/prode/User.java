package prode;

import org.javalite.activejdbc.Model;
import java.util.List;
import spark.Request;
import spark.Response;
import spark.QueryParamsMap;
import java.util.HashMap;
import java.util.Map;

public class User extends Model {

	static{
		/*Evalua que el nombre que tengo en la columna sea valido(no vacio) en caso contrario tira un mensaje avisando*/
		validatePresenceOf("username").message("Please, provide your username");

		/*Evalua que la contraseña que tengo en la columna sea valida(no vacia) en caso contrario tira un mensaje avisando*/
	    validatePresenceOf("password").message("Please, provide your password ");

	    /*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("fixture_id").allowNull(true);
	}

	/*Constructor sin parametros*/
	public User(){

	}

	/*Constructor con parametros*/
	public User(String name, String pas){
		set("username", name);
		set("password", pas);
	}

	public Map registerUser(Request req, Response res){
	    String[] result = {req.queryParams("username"),(String)req.queryParams("pas")};
	    String body = req.body();
	    Map questt = new HashMap();

	    List<User> unico = User.where("username = ?", result[0]);
	    Boolean result2 = unico.size()==0;
	    if(result2){
	      User u = new User(result[0], (String)req.queryParams("pas"));
	      u.saveIt();
	    }
	    else{
	      User u = unico.get(0);
	      String e = (String)u.get("username");
	      if(e.equals(result[0])){
	      	questt.put("error","<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> Ese nombre de usuario ya existe, pruebe con uno diferente.</div>");
	        return questt;
	      }
	    }
	    return questt;
  }

  public Map checkUser(Request req, Response res){
  	String[] result = {req.queryParams("username"),(String)req.queryParams("password")};
	String body = req.body();
	Map questt = new HashMap();

	List<User> unico = User.where("username = ? and password = ?", result[0], result[1]);
	Boolean resu2 = unico.size()==0;
	if(resu2){
		questt.put("error", "<div class='alert alert-danger' id='alert-danger'><strong>Error!</strong> Usuario no registrado.</div>");
		return questt;
	}
	questt.put("error", null);
	return questt;
  }

}
