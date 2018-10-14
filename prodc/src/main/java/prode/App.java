
/**
 * Title: App.
 * Main class.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

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
import com.codahale.metrics.*;
import java.util.concurrent.TimeUnit;


public class App {

	/**
	* Variable la cual es un registro que almacena todas las metricas(cada una con su respectivo nombre)
	*/
	static final MetricRegistry metrics = new MetricRegistry();
		
	public static void main( String[] args ){

	/**
	* Metrica la cual va a verificar cuantas veces se a solicitado ver el ranking, estos nos sirve para comprobar
	* si es una pagina importante del sistema
	*/
	Meter requestsRanking = metrics.meter("requests-ranking");

	/**
	* Metrica la cual verifica cuantas personas se han registrado en el sistema
	*/
	Meter requestsSignIn = metrics.meter("requests-Sign-In");

	/**
	* Metrica la cual es un contenedor que va a contener la cantidad de personas que se encuentran actualmente jugando
	* cada vez que una persona acceda a la pagina play se aumenta en 1 y cada vez que la persona salga se descrementa en 1
	*/
	Counter numPeoplePlaying = metrics.counter(metrics.name(App.class, "number-of-people-on-the-play-page"));

	staticFiles.location("/public");

	before((req, res) ->{
		Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1/prode?nullNamePatternMatchesAll=true", "root", "root");
	});

	after((req,res) ->{
		Base.close();
	});

/*--------------------------------------------------------------------------------------------*/

	get("/inicio", (req, res) -> {
		Map map = new HashMap();

		return new ModelAndView(map, "./views/inicio.html");
	},  new MustacheTemplateEngine()
	);

	Map map2 = new HashMap();

	get("/registrarse", (req, res) -> {
		return new ModelAndView(map2, "./views/registrarse.html");
	},  new MustacheTemplateEngine()
	);
/*--------------------------------------------------------------------------------------------*/

	post("/registrar", (req, res) -> {       
		User newUser = new User();
		Map result = newUser.registerUser(req,res);

		if((String)result.get("error") != null){
			return new ModelAndView(result,"./views/registrarse.html"); 
		}
		requestsSignIn.mark();
		return new ModelAndView(result, "./views/inicio.html");
	},  new MustacheTemplateEngine()
	);
/*---------------------------------------------------------------------------------------------*/

	get("/login", (req, res) -> {
		User user = new User();
		Map logresul = user.checkUser(req, res);

		if((Integer)logresul.get("user") != 0){
			numPeoplePlaying.inc();
			req.session().attribute("user", (Integer)logresul.get("user"));
			return new ModelAndView(logresul, "./views/play.html");
		}

		return new ModelAndView(logresul,"./views/inicio.html");
	},  new MustacheTemplateEngine()
	);

/*----------------------------------------------------------------------------------------------*/
	Map fecha1 = new HashMap();

	get("/schedule1", (req, res) -> {
		return new ModelAndView(fecha1, "./views/schedule1.html");
	},  new MustacheTemplateEngine()
	);

/*----------------------------------------------------------------------------------------------*/
	Map fecha2 = new HashMap();

	get("/schedule2", (req, res) -> {
		return new ModelAndView(fecha2, "./views/schedule2.html");
	},  new MustacheTemplateEngine()
	);

/*----------------------------------------------------------------------------------------------*/
	Map fecha3 = new HashMap();

	get("/schedule3", (req, res) -> {
		return new ModelAndView(fecha3, "./views/schedule3.html");
	},  new MustacheTemplateEngine()
	);

/*----------------------------------------------------------------------------------------------*/
	Map fecha4 = new HashMap();

	get("/schedule4", (req, res) -> {
		return new ModelAndView(fecha4, "./views/schedule4.html");
	},  new MustacheTemplateEngine()
	);

/*----------------------------------------------------------------------------------------------*/
	Map f = new HashMap();

	post("/loadPredictions", (req, res) -> {
		Prediction pre = new Prediction();
		pre.registerPrediction(req, res);

		return new ModelAndView(f, "./views/play.html");
	},  new MustacheTemplateEngine()
	);

/*----------------------------------------------------------------------------------------------*/

	get("/global", (req, res) -> {
		requestsRanking.mark();
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
	},  new MustacheTemplateEngine()
	);

/*---------------------------------------------------------------------------------------------*/

	get("/punctuation", (req, res) -> {

		Map rP = new HashMap();
		int id_u = (Integer)req.session().attribute("user");
		List<Score> top = Score.findBySQL("select * from scores where user_id = ? order by user_id desc;", id_u);
		List<Map> p = new ArrayList<Map>();
		for(int i = 0; i < top.size(); i++){
			Map a = new HashMap();
			a.put("schedure_id", top.get(i).get("schedure_id"));
			a.put("points", top.get(i).get("points"));
			p.add(a);
		}

		rP.put("punctuation", p);

		return new ModelAndView(rP, "./views/punctuation.html");
	},  new MustacheTemplateEngine()
	);	

/*----------------------------------------------------------------------------------------------*/

	Map cs = new HashMap();

	get("/singoff", (req, res) -> {
		numPeoplePlaying.dec();
		if(req.session().attribute("user") != null){
			req.session().removeAttribute("user");
		}
		return new ModelAndView(cs, "./views/inicio.html");
	},  new MustacheTemplateEngine()
	);	

	startReport();
	
	}

	static void startReport() {
		ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
		.convertRatesTo(TimeUnit.SECONDS)
		.convertDurationsTo(TimeUnit.MILLISECONDS)
		.build();
		reporter.start(1, TimeUnit.SECONDS);
	}

}
