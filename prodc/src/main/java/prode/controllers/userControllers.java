package controllers;

import models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

public class userControllers{
	
	public static ModelAndView registrarse(Request request, Response response){
		Map map2 = new HashMap();
		return new ModelAndView(map2, "./views/registrarse.html");
	}		

	public static ModelAndView registrar(Request request, Response response){
		User newUser = new User();
		Map result = userControllers.registerUser(request,response);

		if((String)result.get("error") != null){
			return new ModelAndView(result,"./views/registrarse.html"); 
		}
		return new ModelAndView(result, "./views/inicio.html");
	}

	/**
	 * User register.
	 * @param req
	 * @param res
	 * @return
	 */
	public static Map registerUser(Request req, Response res){
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
}