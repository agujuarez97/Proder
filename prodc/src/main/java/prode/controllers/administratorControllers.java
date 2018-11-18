package controllers;

import models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class administratorControllers{

	public static ModelAndView loadgame(Request request, Response response){
		Map m = new HashMap();
		m = dataRegisterGame(null);
		return new ModelAndView(m, "./views/registergame.html");
	}

	public static ModelAndView loadschedule(Request request, Response response){
		Map m = new HashMap();
		m = dataRegisterSchedule(null);
		return new ModelAndView(m, "./views/registerschedule.html");
	}
	
	public static ModelAndView registergame(Request request, Response response){
		Map m = new HashMap();		
		List<Team> local = Team.where("id = ?;", request.queryParams("local"));
		List<Team> visitor = Team.where("id = ?;", request.queryParams("visitor"));
		
		if(local.size() > 0 && visitor.size() > 0){
			Map team_local = local.get(0).getCompleteTeam();
			Map team_visitor = visitor.get(0).getCompleteTeam();
			
			if((int)team_local.get("id") != (int)team_visitor.get("id")){
				List<Fixture> fixtures = Fixture.where("id = ?", request.queryParams("fixture"));
		
				if(fixtures.size() > 0){
					List<Schedure> schedule = Schedure.where("id = ? and fixture_id = ?", request.queryParams("schedule"), request.queryParams("fixture"));
				
					if(schedule.size() > 0){	
						Game game = new Game(request.queryParams("date").toString(), request.queryParams("hour").toString(), 0, 0, (int)team_local.get("id"), (int)team_visitor.get("id"), Integer.parseInt(request.queryParams("schedule").toString()), 0);
						game.saveIt();
						return new ModelAndView(m, "./views/administrator.html");
					} else {
						Map register_error = new HashMap();
						String error = "<div class='alert alert-danger'><strong>Error!</strong> Schedule not found.</div>";
						register_error = dataRegisterGame(error);
						return new ModelAndView(register_error, "./views/registergame.html");
					}
				} else {
					Map register_error = new HashMap();
					String error = "<div class='alert alert-danger'><strong>Error!</strong> Fixture not found.</div>";
					register_error = dataRegisterGame(error);
					return new ModelAndView(register_error, "./views/registergame.html");
				}
			} else {
				Map register_error = new HashMap();
				String error = "<div class='alert alert-danger'><strong>Error!</strong> The teams can not be the same.</div>";
				register_error = dataRegisterGame(error);
				return new ModelAndView(register_error, "./views/registergame.html");
			}
		} else {
			Map register_error = new HashMap();
			String error = "<div class='alert alert-danger'><strong>Error!</strong> Team not found.</div>";
			register_error = dataRegisterGame(error);
			return new ModelAndView(register_error, "./views/registergame.html");
		}
	}

	public static ModelAndView registerschedule(Request request, Response response){
		Map m = new HashMap();
		
		List<Fixture> fixture = Fixture.findBySQL("select * from fixtures where name = ?;", request.queryParams("fixture"));

		if(fixture.size() > 0){
			Schedure schedule = new Schedure((int)fixture.get(0).get("id"));
			schedule.saveIt();
			return new ModelAndView(m, "./views/administrator.html");
			
		} else {
			m.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Fixture inexistente.</div>");
			return new ModelAndView(m, "./views/registerschedule.html");
		}
	}
	
	public static ModelAndView loadresult(Request request, Response response){
		Map m = new HashMap();
		
		List<Game> games = Game.findBySQL("select * from games where result_id = 0;");
		if(games.size() > 0){
			m.put("load", 1);
			List<Map> p = new ArrayList<Map>();
			for(int i=0; i<games.size(); i++){
				Map a = new HashMap();
				Game g = games.get(i);
				Map m_game = g.getCompleteGame();
				a.put("idGame",m_game.get("id"));
				a.put("local",((Team)m_game.get("local")).getName());
				a.put("visitante",((Team)m_game.get("visitante")).getName());
				p.add(a);
			}
			m.put("games", p);
		} else {
			String msg = "No hay partidos disponibles en este momento...";
			m.put("msg", msg);
		}
		
		return new ModelAndView(m, "./views/registerresult.html");
	}
	
	public static ModelAndView registerresultgame(Request request, Response response){
		Map m = new HashMap();
		
		int amount = request.queryParams().size() - 1;
		String[] id = new String[amount];
		String[] goalLocal = new String[amount];
		String[] goalVisit = new String[amount];
		
		List<Game> games = Game.findBySQL("select * from games;");
		int index = 0;
		for(int i = 0; i < games.size(); i++){
		
			Map game = ((Game)games.get(i)).getCompleteGame();
			int id_game = (int)game.get("id");
			if(request.queryParams("id"+id_game) != null){
				if(id_game == Integer.parseInt(request.queryParams("id"+id_game))){
					id[index] = request.queryParams("id"+id_game);
					goalLocal[index] = (String)request.queryParams("local"+id_game);
					goalVisit[index] = (String)request.queryParams("visit"+id_game);
					index++;
				}
			}
		}
		
		for (int i = 0; i < id.length; i++) {
			if(id[i] != null){
				int result = 0;
				int gl = Integer.parseInt(goalLocal[i]);
				int gv = Integer.parseInt(goalVisit[i]);
				if(gl > gv){
					result = 1;
				} else if(gl < gv){
					result = 2;
				} else {
					result = 3;
				}
				Game.update("result_id = ?, goalLocal = ?, goalVisitor = ?", "id = ?", result, gl, gv, id[i]);
				administratorControllers.calculateScore(Integer.parseInt(id[i]), result);
			}
		}
		return new ModelAndView(m, "./views/administrator.html");
	}
	
	private static void calculateScore(int id_game, int result){
		List<Game> games = Game.where("id = ?", id_game);
		Map game = games.get(0).getCompleteGame();
		Map schedule = ((Schedure)game.get("schedule")).getCompleteSchedule();
		int id_schedule = (int)schedule.get("id");
		List<Prediction> predictions = Prediction.where("game_id = ?", id_game);
		if(predictions.size() > 0){
			for(int i = 0; i < predictions.size(); i++){
				Map pred = predictions.get(i).getCompletePrediction();
				Map user = ((User)pred.get("user")).getCompleteUser();
				int id_user = (int)user.get("id");
				List<Score> scores = Score.where("user_id = ? and schedure_id = ?", id_user, id_schedule);
				int result_pred = (int)pred.get("result");
				if(scores.size() > 0){
					if(result_pred == result){
						Map score = scores.get(0).getCompletePrediction();
						int id_score = (int)score.get("id");
						int user_score = (int)score.get("points");
						user_score++;
						Score.update("points = ?", "id = ?", user_score, id_score);
					}					
				} else {
					int score_user = 0;
					if(result_pred == result){
						score_user++;
					}
					Score newScore = new Score(id_user, score_user, id_schedule);
					newScore.saveIt();
				}
			}
		}
	}

	private static Map dataRegisterSchedule(String error){
		Map m = new HashMap();
		
		List<Fixture> fixtures = Fixture.findBySQL("select * from fixtures order by name asc;");
		List<Map> f = new ArrayList<Map>();
		for(int i = 0; i < fixtures.size(); i++){
			Map data_fixture = new HashMap();
			Map fixture = fixtures.get(i).getCompleteFixture();
			data_fixture.put("nameFixture", fixture.get("name"));
			f.add(data_fixture);
		}
		m.put("fixtures", f);
		return m;
	}
	
	private static Map dataRegisterGame(String error){
		Map m = new HashMap();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		m.put("currentDate", dtf.format(now));
		
		List<Team> teams = Team.findBySQL("select * from teams order by name asc;");
		List<Map> t = new ArrayList<Map>();
		for(int i = 0; i < teams.size(); i++){
			Map data_team = new HashMap();
			Map team = teams.get(i).getCompleteTeam();
			data_team.put("idTeam", team.get("id"));
			data_team.put("nameTeam", team.get("nombre"));
			t.add(data_team);
		}
		m.put("teams", t);
		
		List<Schedure> schedules = Schedure.findBySQL("select * from schedures;");
		List<Map> s = new ArrayList<Map>();
		for(int i = 0; i < schedules.size(); i++){
			Map data_schedule = new HashMap();
			Map schedule = schedules.get(i).getCompleteSchedule();
			data_schedule.put("idSchedule", schedule.get("id"));
			s.add(data_schedule);
		}
		m.put("schedules", s);
		
		List<Fixture> fixtures = Fixture.findBySQL("select * from fixtures order by name asc;");
		List<Map> f = new ArrayList<Map>();
		for(int i = 0; i < fixtures.size(); i++){
			Map data_fixture = new HashMap();
			Map fixture = fixtures.get(i).getCompleteFixture();
			data_fixture.put("idFixture", fixture.get("id"));
			data_fixture.put("nameFixture", fixture.get("name"));
			f.add(data_fixture);
		}
		m.put("fixtures", f);
		
		if(error != null){
			m.put("error", error);
		}
		
		return m;
	}
	
}
