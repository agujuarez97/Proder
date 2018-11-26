/**
 * Title: userControllers
 * Description: This class controls all the actions belonging to an user
 * @author: Agustin Juarez, Gaston Plisga, Matias Suarez  
*/
package controllers;

import models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

public class userControllers{
	
	/**
	* Description: This method redirects the registrarse.html view
	* @return: ModelAndView
	*/
	public static ModelAndView registrarse(Request request, Response response){
		Map map2 = new HashMap();
		return new ModelAndView(map2, "./views/registrarse.html");
	}		

	/**
	* Description: This method registers the user and if everything went OK it redirects it to the view start.html and but to the view register.html 
	* @return: ModelAndView
	*/
	public static ModelAndView registrar(Request request, Response response){
		User newUser = new User();
		Map result = userControllers.registerUser(request,response);

		if((String)result.get("error") != null){
			return new ModelAndView(result,"./views/registrarse.html"); 
		}
		return new ModelAndView(result, "./views/inicio.html");
	}

	/**
	* Description: This method checks that the user's data is correct, if everything went OK it redirects it to the play.html view but to the inicio.html view
	* @return: ModelAndView
	*/
	public static ModelAndView login(Request request, Response response){
		User user = new User();
		Map loginResult = userControllers.checkUser(request, response);

		if((Integer)loginResult.get("user") != 0){
			Integer idUser = (Integer)loginResult.get("user");
			request.session().attribute("user", idUser);
			List<User> uniqueUser = User.where("id = ?", idUser);
			user = uniqueUser.get(0);
			
			if (user.getRange() == 0){
				Map completeschedule = new HashMap();
				List <Schedure> schedules = Schedure.findBySQL("select * from schedures");
				List <Map> mapschedule = new ArrayList <Map>();
				for (Schedure s: schedules) {
					mapschedule.add(s.getCompleteSchedule());
				}
				completeschedule.put("fechas", mapschedule);
				return new ModelAndView(completeschedule, "./views/play.html");
			} else if (user.getRange() == 1){
				Map adminConfig = new HashMap();
				return new ModelAndView(adminConfig, "./views/administrator.html");
			}
		}

		return new ModelAndView(loginResult,"./views/inicio.html");
	}

	/**
	* Description: This method closes the user's session 
	* @return: ModelAndView
	*/
	public static ModelAndView singoff(Request request, Response response){
		Map cs = new HashMap();
		if(request.session().attribute("user") != null){
			request.session().removeAttribute("user");
		}
		return new ModelAndView(cs, "./views/inicio.html");
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
		    	questt.put("error","<div class='alert alert-danger'><strong>Error!</strong> Existing username.</div>");
		        return questt;
		    }
		    else{
		    	if(!(e.equals(result[0])) && p.equals(result[1])){
		      		questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Existing password.</div>");
		      		return questt;
		      	}
		      	else{
		      		questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Existing user.</div>");
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
	private static Map checkUser(Request req, Response res){
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
					questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> User not valid.</div>");
					return questt;
				}
				else{
					questt.put("user", 0);
					questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Incorrect password.</div>");
					return questt;
				}
			}
			
		}
		else{
			questt.put("user", 0);
			questt.put("error", "<div class='alert alert-danger'><strong>Error!</strong> User not valid.</div>");
			return questt;
		}
	}

}
