package prode;

import org.javalite.activejdbc.Base;
import java.util.List;
import java.util.ArrayList;


import spark.Request;
import spark.Response;
import prode.User;
import prode.Prediction;
import prode.Score;

import static spark.Spark.*;
import static spark.Spark.staticFileLocation;

import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App {

	private static Map getSession(Request req, Response res){
		Map a = new HashMap();
		if(req.session().attribute("user")!=null){
			a.put("user_id", (Integer)req.session().attribute("user"));
		}
		return a;
	}

    public static void main( String[] args ){

    	//Permite levantar CSS, JS e IMAGENES
    	
    	staticFiles.location("/public");

    	before((req, res) ->{
        	Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");
        });

        after((req,res) ->{
        	Base.close();
        });

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

	  	if((Integer)logresul.get("user") != 0){
	  		req.session().attribute("user", (Integer)logresul.get("user"));
			return new ModelAndView(logresul, "./views/play.html");
	  	}

	  	return new ModelAndView(logresul,"./views/inicio.html");
	  	}, new MustacheTemplateEngine()
	  	);

/*----------------------------------------------------------------------------------------------*/
		Map fecha1 = new HashMap();

      	get("/schedule1", (req, res) -> {
           return new ModelAndView(fecha1, "./views/schedule1.html");
        }, new MustacheTemplateEngine()
      	);

/*----------------------------------------------------------------------------------------------*/
		Map fecha2 = new HashMap();

      	get("/schedule2", (req, res) -> {
           return new ModelAndView(fecha2, "./views/schedule2.html");
        }, new MustacheTemplateEngine()
      	);

/*----------------------------------------------------------------------------------------------*/
		Map fecha3 = new HashMap();

      	get("/schedule3", (req, res) -> {
           return new ModelAndView(fecha3, "./views/schedule3.html");
        }, new MustacheTemplateEngine()
      	);

/*----------------------------------------------------------------------------------------------*/
		Map fecha4 = new HashMap();

      	get("/schedule4", (req, res) -> {
           return new ModelAndView(fecha4, "./views/schedule4.html");
        }, new MustacheTemplateEngine()
      	);

/*----------------------------------------------------------------------------------------------*/
      	Map f = new HashMap();

      	post("/loadPredictions", (req, res) -> {
      	   Prediction pre = new Prediction();
      	   pre.registerPrediction(req, res);

           return new ModelAndView(f, "./views/play.html");
        }, new MustacheTemplateEngine()
      	);

/*----------------------------------------------------------------------------------------------*/

		get("/global", (req, res) -> {
		   Map rG = new HashMap();
		   List<Score> top = Score.findBySQL("select user_id, sum(points) as points from scores group by user_id order by points desc;");
		   List<Map> p = new ArrayList<Map>();
		   for(int i = 0; i < top.size(); i++){
		   		List<User> u = User.where("id = ?", top.get(i).get("user_id"));
		   		Map a = new HashMap();
		   		a.put("username", u.get(0).get("username"));
		   		a.put("points", top.get(i).get("points"));
		   		p.add(a);
		   }

		   rG.put("ranking", p);

           return new ModelAndView(rG, "./views/global.html");
        }, new MustacheTemplateEngine()
      	);

/*----------------------------------------------------------------------------------------------*/

		Map cs = new HashMap();

		get("/singoff", (req, res) -> {
		   if(req.session().attribute("user") != null){
		   		req.session().removeAttribute("user");
		   }
           return new ModelAndView(cs, "./views/inicio.html");
        }, new MustacheTemplateEngine()
      	);	
	}

}
