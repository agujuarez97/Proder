package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

import models.*;

public class predictionControllers{

	public static ModelAndView loadPrediction(Request request, Response response){
		Map f = new HashMap();
		predictionControllers.registerPrediction(request, response);
		return new ModelAndView(f, "./views/play.html");
	}
	
	public static ModelAndView schedule(Request request, Response response){
		Map pred = new HashMap();
		int fecha = Integer.parseInt(request.queryParams("id").toString());
		List<Game> games = Game.findBySQL("select * from games where schedure_id = ?;", fecha);
		List<Map> p = new ArrayList<Map>();
		for(int i=0; i<games.size(); i++){
			Map a = new HashMap();
			Game g = games.get(i);
			Map m = g.getCompleteGame();
			a.put("idGame",m.get("id"));
			a.put("local",((Team)m.get("local")).getName());
			a.put("visitante",((Team)m.get("visitante")).getName());
			p.add(a);
		}
		pred.put("idFecha", fecha);
		pred.put("games", p);
		return new ModelAndView(pred, "./views/schedule.html");
	}

	private static void registerPrediction(Request request, Response response){

		String[] id = {request.queryParams("id1"), request.queryParams("id2"), request.queryParams("id3"), request.queryParams("id4"), request.queryParams("id5")};
		String[] result = {request.queryParams("partido1"), request.queryParams("partido2"), request.queryParams("partido3"), request.queryParams("partido4"), request.queryParams("partido5")};
		int fecha = Integer.parseInt(request.queryParams("f").toString());

		int id_u = (Integer)request.session().attribute("user");

		List<Prediction> prediction = Prediction.findBySQL("select * from predictions where user_id = ? and schedure_id = ?;", id_u, fecha);

		if (prediction.size() == 0){
			for (int i = 0; i < result.length; i++) {
				Prediction p = new Prediction(Integer.parseInt(result[i]), id_u, Integer.parseInt(id[i]), fecha);
				p.saveIt();
			}

			predictionControllers.calculateScore(id_u, fecha);
		}
	}

	private static void calculateScore(int id_usu, int fecha){
		List<Prediction> predicciones = Prediction.where("user_id = ? and schedure_id = ?", id_usu, fecha);

		Score s = new Score(id_usu, 0, fecha);
	   	int aux = 0;

		for (int i = 0; i < predicciones.size(); i++) {

			int id = (Integer)(predicciones.get(i)).get("game_id");
			List<Game> g = Game.where("id = ? and schedure_id = ?", id, fecha);
			List<Result> r = Result.where("id = ?", (Integer)g.get(0).get("result_id"));
			int resg = (Integer)r.get(0).get("result");
			int resp = (Integer)(predicciones.get(i)).get("result");
			
			if(resg == resp){
				aux += 1;
			}
		}
		s.set("points", aux);
		s.saveIt();
	}
}
