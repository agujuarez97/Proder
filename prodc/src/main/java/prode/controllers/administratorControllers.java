package controllers;

import models.*;
import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

public class administratorControllers{

	public static ModelAndView loadgame(Request request, Response response){
		Map m = new HashMap();
		return new ModelAndView(m, "./views/registergame.html");
	}

	public static ModelAndView loadschedule(Request request, Response response){
		Map m = new HashMap();
		return new ModelAndView(m, "./views/registerschedule.html");
	}

	public static ModelAndView removeschedule(Request request, Response response){
		Map m = new HashMap();
		return new ModelAndView(m, "./views/deleteschedule.html");
	}
	
	public static ModelAndView registergame(Request request, Response response){
		Map m = new HashMap();
		
		List<Team> local = Team.findBySQL("select * from teams where name = ?;", request.queryParams("local"));
		List<Team> visitor = Team.findBySQL("select * from teams where name = ?;", request.queryParams("visitor"));
		
		if(local.size() > 0 && visitor.size() > 0){
		
			Map team_local = local.get(0).getCompleteTeam();
			Map team_visitor = visitor.get(0).getCompleteTeam();
			
			Game game = new Game(request.queryParams("date"), request.queryParams("hour"), 0, 0, (int)team_local.get("id"), (int)team_visitor.get("id"), Integer.parseInt(request.queryParams("schedule").toString()), 0);
			game.saveIt();
			
		}
		
		return new ModelAndView(m, "./views/administrator.html");
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

	public static ModelAndView deleteschedule(Request request, Response response){
		Map m = new HashMap();
		
		List<Fixture> fixture = Fixture.findBySQL("select * from fixtures where name = ?;", request.queryParams("fixture"));
		List<Schedure> schedules = Schedure.findBySQL("select * from schedures where id = ?;", request.queryParams("schedule"));

		if(fixture.size() > 0 && schedules.size() > 0){
			List<Schedure> schedule = Schedure.findBySQL("select * from schedures where fixture_id = ? and id = ?;", (int)fixture.get(0).get("id"), request.queryParams("schedule"));
			if(schedule.size() > 0){
				List<Game> games = Game.findBySQL("select * from games where schedure_id = ?;", (int)schedule.get(0).get("id"));
				for (Game g: games) {
					g.delete();
				}
				schedule.get(0).delete();
				return new ModelAndView(m, "./views/administrator.html");
			} else{
				m.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Schedule inexistente in this fixture.</div>");
				return new ModelAndView(m, "./views/deleteschedule.html");
			}
		} else {
			if(fixture.size() == 0){
				m.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Fixture inexistente.</div>");
				return new ModelAndView(m, "./views/deleteschedule.html");
			} else {
				if(schedules.size() == 0){
					m.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Schedule inexistente in the Prode.</div>");
					return new ModelAndView(m, "./views/deleteschedule.html");
				} else {
					m.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Invalid data.</div>");
					return new ModelAndView(m, "./views/deleteschedule.html");
				}
			}
		}
	}
	
	public static ModelAndView loadresult(Request request, Response response){
		Map m = new HashMap();
		
		List<Game> games = Game.findBySQL("select * from games where result_id = 0;");
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
	
}
