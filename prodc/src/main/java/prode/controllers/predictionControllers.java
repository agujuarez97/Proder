package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

import models.*;

public class predictionControllers{

	public static ModelAndView loadPrediction(Request request, Response response){
		predictionControllers.registerPrediction(request, response);
		Map completeschedule = new HashMap();
		List <Schedure> schedules = Schedure.findBySQL("select * from schedures");
		List <Map> mapschedule = new ArrayList <Map>();
		for (Schedure s: schedules) {
			mapschedule.add(s.getCompleteSchedule());
		}
		completeschedule.put("fechas", mapschedule);
		return new ModelAndView(completeschedule, "./views/play.html");
	}
	
	public static ModelAndView schedule(Request request, Response response){
		Map pred = new HashMap();
		int fecha = Integer.parseInt(request.queryParams("id").toString());
		List<Game> games = Game.findBySQL("select * from games where result_id = 0 and schedure_id = ?;", fecha);
		List<Map> p = new ArrayList<Map>();
		if(games.size()>0){
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
		}else{
			String error = "NO HAY PARTIDOS PARA PREDECIR EN LA FECHA";
			pred.put("idFecha", fecha);
			pred.put("error", error);
			return new ModelAndView(pred, "./views/scheduleWithoutGames.html");
		}
	}

	private static void registerPrediction(Request request, Response response){
		int amount = request.queryParams().size() - 1;
		String[] id = new String[amount/2];
		String[] result = new String[amount/2];
		int index = 1;
		
		int fecha = Integer.parseInt(request.queryParams("f").toString());
		List<Game> games = Game.findBySQL("select * from games where schedure_id = ? and result_id = 0;", fecha);
		
		for(int i=0; i < amount; i+=2){
			Map g = games.get(index-1).getCompleteGame();
			int idGame = (int)g.get("id");
			id[index-1] = request.queryParams("id"+idGame);
			result[index-1] = request.queryParams("partido"+idGame);
			index++;
		}
		int id_u = (Integer)request.session().attribute("user");
		List<Prediction> prediction = Prediction.findBySQL("select * from predictions where user_id = ? and schedure_id = ?;", id_u, fecha);
		if(prediction.size() == 0){
			for(int i = 0; i < id.length; i++){
				if(id[i] != null){
					Prediction user_prediction = new Prediction(Integer.parseInt(result[i]), id_u, Integer.parseInt(id[i]), fecha);
					user_prediction.saveIt();
				}
			}
		} else {
			for(int p = 0; p < prediction.size(); p++){
				Map pred = prediction.get(p).getCompletePrediction();
				Map game = ((Game)pred.get("game")).getCompleteGame();
				int id_game = (int)game.get("id");
				boolean exists = false;
				for (int i = 0; i < id.length; i++) {
					if(id_game == Integer.parseInt(id[i])){
						exists = true;
					}
				}
				if(!exists){
					Prediction user_prediction = new Prediction(Integer.parseInt(result[p]), id_u, Integer.parseInt(id[p]), fecha);
					user_prediction.saveIt();
				}
			}
		}
	}

}
