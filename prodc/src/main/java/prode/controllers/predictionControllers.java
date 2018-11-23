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
		completeschedule = mapSchedulesPredicition(request, response);
		
		return new ModelAndView(completeschedule, "./views/prediction.html");
	}

	public static ModelAndView seePredictions(Request request, Response response){
		Map re = new HashMap();
		int id = (Integer)request.session().attribute("user");
		List<Prediction> predictions = Prediction.where("user_id =?;", id);
		List<Map> p = new ArrayList<Map>();

		for(int i = 0; i < predictions.size(); i++){
			List<Game> game = Game.where("id = ?;", predictions.get(i).get("game_id"));
			Map a = new HashMap();
			Game g = game.get(0);
			Map m = g.getCompleteGame();
			a.put("schedure_id", predictions.get(i).get("schedure_id"));
			a.put("local",((Team)m.get("local")).getName());
			a.put("golLocal", m.get("golLocal"));
			a.put("visitante",((Team)m.get("visitante")).getName());
			a.put("golVisitante", m.get("golVisitante"));
			if ((Integer)predictions.get(i).get("result") == 3)
				a.put("prediction", "¡EMPATE!");
			if ((Integer)predictions.get(i).get("result") == 1)
				a.put("prediction", "¡GANA LOCAL!");
			if ((Integer)predictions.get(i).get("result") == 2)
				a.put("prediction", "¡GANA VISITANTE!");

			p.add(a);
		}
		re.put("predictions", p);
		return new ModelAndView(re, "./views/seepredictions.html"); 
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
	
	public static ModelAndView loadschedulesprediction(Request request, Response response){
		Map m = new HashMap();
		m = mapSchedulesPredicition(request, response);
		return new ModelAndView(m, "./views/prediction.html");
	}
	
	public static ModelAndView backToStart(Request request, Response response){
		Map m = new HashMap();
		return new ModelAndView(m, "./views/play.html");
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
	
	private static Map mapSchedulesPredicition(Request request, Response response){
		Map m = new HashMap();
		int id_user = (Integer)request.session().attribute("user");
		List<User> users = User.where("id = ?", id_user);
		Map user = ((User)users.get(0)).getCompleteUser();
		if(user.get("fixture") != null){
			Fixture user_fixture = (Fixture)user.get("fixture");
			m.put("nameFixture", user_fixture.get("name"));
		
			List<Schedure> schedules = Schedure.where("fixture_id = ?", user_fixture.get("id"));
			List<Map> mapschedule = new ArrayList <Map>();
			for (int i = 0; i < schedules.size(); i++) {
				Schedure s = (Schedure)schedules.get(i);
				Map map_schedule = s.getCompleteSchedule();
				Map schedule = new HashMap();
				schedule.put("id", s.get("id"));
				mapschedule.add(schedule);
			}
			m.put("fechas", mapschedule);
		} else {
			String msg = "Aún no realizó la subscripción a ningún fixture. Primero realice una subscripción para poder comenzar a jugar.";
			m.put("msg", msg);
		}
		return m;
	}
	
}
