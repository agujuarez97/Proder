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
		int count = 0;
		if(games.size()>0){
			for(int i=0; i<games.size(); i++){
				Map a = new HashMap();
				Game g = games.get(i);
				Map m = g.getCompleteGame();
				List<Prediction> pre = Prediction.where("game_id = ?;", m.get("id"));
				if(pre.size() == 0) {
					a.put("idGame",m.get("id"));
					a.put("local",((Team)m.get("local")).getName());
					a.put("visitante",((Team)m.get("visitante")).getName());
					p.add(a);
					count++;
				}
			}
			if(count == 0) {
				String error = "TODOS LOS PARTIDOS DE ESTA FEHCA YA FUERON PREDECIDOS";
				pred.put("idFecha", fecha);
				pred.put("error", error);
				return new ModelAndView(pred, "./views/scheduleWithoutGames.html");
			} else {
				pred.put("idFecha", fecha);
				pred.put("games", p);
				return new ModelAndView(pred, "./views/schedule.html");
			}
		}else{
			String error = "NO HAY PARTIDOS PARA PREDECIR EN LA FECHA";
			pred.put("idFecha", fecha);
			pred.put("error", error);
			return new ModelAndView(pred, "./views/scheduleWithoutGames.html");
		}
	}

	private static void registerPrediction(Request request, Response response){

		int id_u = (Integer)request.session().attribute("user");
		int fecha = Integer.parseInt(request.queryParams("f").toString());
		int id_game = Integer.parseInt(request.queryParams("game").toString());
		int goal_local = Integer.parseInt(request.queryParams("local").toString());
		int goal_visitor = Integer.parseInt(request.queryParams("visit").toString());
		List<Game> games = Game.where("id = ?;", id_game);

		int result = 0;
		if(goal_local > goal_visitor){
			result = 1;
		} else if(goal_local < goal_visitor){
			result = 2;
		} else {
			result = 3;
		}

		Prediction p = new Prediction(result, id_u, id_game, fecha);
		p.saveIt();
	}
}
