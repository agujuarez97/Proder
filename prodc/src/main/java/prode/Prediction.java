package prode;

import java.util.*;
import org.javalite.activejdbc.Model;
import spark.Request;
import spark.Response;
import prode.Score;

public class Prediction extends Model{
	
	static{

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("result").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("user_id").allowNull(false);

		/*Evalua que el valor que hay en la columna se valido es decir sea un entero, y no permite que tengo valor nulo*/
		validateNumericalityOf("game_id").allowNull(false);
	}

	/*Constructor*/
	public Prediction(){

	}

	/*Constructor*/
	public Prediction(int re, int id_u, int id_g, int id_s){
		set("result", re);
		set("user_id", id_u);
		set("game_id", id_g);
		set("schedure_id", id_s);
	}

	public void registerPrediction(Request req, Response res){

		String[] id = {req.queryParams("id1"), req.queryParams("id2"), req.queryParams("id3"), req.queryParams("id4"), req.queryParams("id5")};
		String[] result = {req.queryParams("partido1"), req.queryParams("partido2"), req.queryParams("partido3"), req.queryParams("partido4"), req.queryParams("partido5")};
		int fecha = Integer.parseInt(req.queryParams("f").toString());

		int id_u = (Integer)req.session().attribute("user");

		for (int i = 0; i < result.length; i++) {
			Prediction p = new Prediction(Integer.parseInt(result[i]), id_u, Integer.parseInt(id[i]), fecha);
			p.saveIt();
		}

		Score s = new Score();
		s.calculateScore(id_u);
	}
}