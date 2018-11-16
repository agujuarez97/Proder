
/**
 * Title: App.
 * Main class.
 * @author. Agustin Juarez, Gaston Plisga, Matias Suarez . 
 */

package prode;

import org.javalite.activejdbc.Base;
import controllers.*;
import static spark.Spark.*;
import static spark.Spark.staticFileLocation;
import spark.template.mustache.MustacheTemplateEngine;

public class App {

	public static void main( String[] args ){

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

	get("/login", userControllers::login, new MustacheTemplateEngine());

	get("/singoff", userControllers::singoff, new MustacheTemplateEngine());	

	get("/schedule", predictionControllers::schedule, new MustacheTemplateEngine());
	
	post("/loadPredictions", predictionControllers::loadPrediction, new MustacheTemplateEngine());

	get("/global", punctuationControllers::global, new MustacheTemplateEngine());

	get("/punctuation", punctuationControllers::punctuation, new MustacheTemplateEngine());	

	get("/loadgame", administratorControllers::loadgame, new MustacheTemplateEngine());
	
	post("/registergame", administratorControllers::registergame, new MustacheTemplateEngine());
	
	}
}
