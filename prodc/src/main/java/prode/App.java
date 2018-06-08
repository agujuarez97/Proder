package prode;

import org.javalite.activejdbc.Base;

import prode.User;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App
{
    public static void main( String[] args ){

        //Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");

        /*User u = new User("Agustin", "chqq2018", 1);
        u.saveIt();*/

        //Base.close();

/*--------------------------------------------------------------------------------------------*/
        Map map = new HashMap();
        map.put("hola", "<li><a href='/registrarse'>Jugar</a></li>");
      	get("/inicio", (req, res) -> {
           return new ModelAndView(map, "./views/inicio.html");
        }, new MustacheTemplateEngine()
      	);
/*--------------------------------------------------------------------------------------------*/
		Map mapregs = new HashMap();

		get("/registrarse", (req, res) -> {
           return new ModelAndView(mapregs, "./views/registrarse.html");
        }, new MustacheTemplateEngine()
      	);
    }
}
