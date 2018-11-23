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
	
	public static ModelAndView schedule(Request request, Response response){
		Map pred = new HashMap();
		int fecha = Integer.parseInt(request.queryParams("id").toString());
		List<Schedure> schedules = Schedure.where("id = ?", fecha);
		Map schedule = ((Schedure)schedules.get(0)).getCompleteSchedule();
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
				String error = "TODOS LOS PARTIDOS DE ESTA FECHA YA FUERON PREDECIDOS";
				pred.put("idFecha", fecha);
				pred.put("num", schedule.get("number"));
				pred.put("error", error);
				return new ModelAndView(pred, "./views/scheduleWithoutGames.html");
			} else {
				pred.put("idFecha", fecha);
				pred.put("num", schedule.get("number"));
				pred.put("games", p);
				return new ModelAndView(pred, "./views/schedule.html");
			}
		}else{
			String error = "NO HAY PARTIDOS PARA PREDECIR EN LA FECHA";
			pred.put("idFecha", fecha);
			pred.put("num", schedule.get("number"));
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
		int id_game = Integer.parseInt(request.queryParams("game").toString());
		int goal_local = Integer.parseInt(request.queryParams("local").toString());
		int goal_visitor = Integer.parseInt(request.queryParams("visit").toString());
		List<Game> games = Game.where("id = ?;", id_game);
		Map game = ((Game)games.get(0)).getCompleteGame();
		int fecha = (int)((Schedure)game.get("schedule")).getId();

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
				schedule.put("num", s.get("num"));
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
