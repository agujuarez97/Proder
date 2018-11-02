
/**
 * Title: App.
 * Main class.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package prode;

import org.javalite.activejdbc.Base;
import java.util.List;
import java.util.ArrayList;

import controllers.*;

import spark.Request;
import spark.Response;
import models.User;
import models.Prediction;
import models.Score;

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

	get("/inicio", startControllers::start, new MustacheTemplateEngine());

	get("/registrarse", userControllers::registrarse, new MustacheTemplateEngine());

	post("/registrar", userControllers::registrar, new MustacheTemplateEngine());
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

	get("/schedule1", predictionControllers::scheduleOne, new MustacheTemplateEngine());

	get("/schedule2", predictionControllers::scheduleTwo, new MustacheTemplateEngine());

	get("/schedule3", predictionControllers::scheduleThree, new MustacheTemplateEngine());

	get("/schedule4", predictionControllers::scheduleFour, new MustacheTemplateEngine());

	post("/loadPredictions", predictionControllers::loadPrediction, new MustacheTemplateEngine());

/*----------------------------------------------------------------------------------------------*/

	get("/global", punctuationControllers::global, new MustacheTemplateEngine());

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
