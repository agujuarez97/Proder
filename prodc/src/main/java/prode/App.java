package prode;

import org.javalite.activejdbc.Base;

import prode.User;

import static spark.Spark.*;
import static spark.Spark.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App
{
    public static void main( String[] args ){

    	//Permite levantar CSS, JS e IMAGENES
    	
    	staticFiles.location("/public");

    	before((req, res) ->{
        	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");
        });

        after((req,res) ->{
        	Base.close();
        });
        /*User u = new User("Agustin", "chqq2018", 1);
        u.saveIt();*/

/*--------------------------------------------------------------------------------------------*/
        Map map = new HashMap();

      	get("/inicio", (req, res) -> {
           return new ModelAndView(map, "./views/inicio.html");
        }, new MustacheTemplateEngine()
      	);

      	Map map2 = new HashMap();

      	get("/registrarse", (req, res) -> {
           return new ModelAndView(map2, "./views/registrarse.html");
        }, new MustacheTemplateEngine()
      	);
/*--------------------------------------------------------------------------------------------*/

      	post("/registrar", (req, res) -> {       
	    User newUser = new User();
	    Map result = newUser.registerUser(req,res);

	    if((String)result.get("error") != null){
	      return new ModelAndView(result,"./views/registrarse.html"); 
	    }
	    return new ModelAndView(result, "./views/inicio.html");
	  	}, new MustacheTemplateEngine()
	  	);
/*---------------------------------------------------------------------------------------------*/

	  	get("/login", (req, res) -> {
	  	User user = new User();
	  	Map logresul = user.checkUser(req, res);

	  	if((String)logresul.get("error") != null){
	  		return new ModelAndView(logresul,"./views/inicio.html");
	  	}
		return new ModelAndView(logresul, "./views/registrarse.html");
	  	}, new MustacheTemplateEngine()
	  	);
    }
}
