package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

import models.*;

public class fixtureControllers {

	public static ModelAndView searchFixture(Request request, Response response){
		Map m = new HashMap();
		m = getDataFixture(null);
		return new ModelAndView(m, "./views/searchFixture.html");
	}

	public static ModelAndView fixture(Request request, Response response){
		Map fix = new HashMap();
		List<Fixture> fixtures = Fixture.where("id = ?;", request.queryParams("fixture"));
		if(fixtures.size() > 0){
			Fixture fixture = fixtures.get(0);
			List<Schedure> schedules = Schedure.findBySQL("select * from schedures where fixture_id = ?;", fixture.getId());
			List<Map> schedule = new ArrayList<Map>();	

			for(int j=0; j<schedules.size(); j++){
				Schedure scheduleActual = schedules.get(j);
				Map dataSchedule = scheduleActual.getCompleteSchedule();
				Map gamesScehdule = new HashMap();
				List<Game> games = Game.findBySQL("select * from games where schedure_id = ?;", dataSchedule.get("id"));	
				List<Map> p = new ArrayList<Map>();

				for(int i=0; i<games.size(); i++){
					Map a = new HashMap();
					Game gameActual = games.get(i);
					Map dataGame = gameActual.getCompleteGame();
					a.put("idGame",dataGame.get("id"));
					a.put("local",((Team)dataGame.get("local")).getName());
					a.put("visitante",((Team)dataGame.get("visitante")).getName());
					a.put("dateGame", dataGame.get("date"));
					a.put("hourGame", dataGame.get("hour"));
					p.add(a);
				}		
				gamesScehdule.put("idSchedule", dataSchedule.get("id"));
				gamesScehdule.put("games", p);
				schedule.add(gamesScehdule);
			}
			fix.put("nameFixture", fixture.getName());
			fix.put("schedules", schedule);
			return new ModelAndView(fix, "./views/fixture.html");
		} else {
			fix.put("error", "<div class='alert alert-danger'><strong>Error!</strong> Fixture inexistente.</div>");
			return new ModelAndView(fix, "./views/searchFixture.html");
		}
	}
	
	public static ModelAndView subscribefixture(Request request, Response response){
		Map m = new HashMap();
		m = getDataFixture(null);
		return new ModelAndView(m, "./views/subscribefixture.html");
	}
	
	public static ModelAndView registersubscribefixture(Request request, Response response){
		Map m = new HashMap();

		if(request.queryParams("fixture") != null){
			int id_user = (Integer)request.session().attribute("user");
			List<User> users = User.where("id = ?", id_user);
			Map data_user = ((User)users.get(0)).getCompleteUser();
			
			if(data_user.get("fixture") != null){
				List<Prediction> predictions = Prediction.where("user_id = ?", id_user);
				for(int i = 0; i < predictions.size(); i++){
					Prediction prediction = predictions.get(i);
					prediction.delete();
				}
				
				List<Score> scores = Score.where("user_id = ?", id_user);
				for(int i = 0; i < scores.size(); i++){
					Score score = scores.get(i);
					score.delete();
				}
			}
			
			User.update("fixture_id = ?", "id = ?", Integer.parseInt(request.queryParams("fixture")), id_user);
			
			return new ModelAndView(m, "./views/play.html");
		} else {
			String error = "<div class='alert alert-danger'><strong>Error!</strong> Fixture not found.</div>";
			m = getDataFixture(error);
			return new ModelAndView(m, "./views/subscribefixture.html");
		}
	}
	
	private static Map getDataFixture(String error){
		Map m = new HashMap();
		List<Fixture> fixtures = Fixture.findBySQL("select * from fixtures;");
		List<Map> f = new ArrayList<Map>();
		for(int i = 0; i < fixtures.size(); i++){
			Map fixture = new HashMap();
			Map data_fixture = ((Fixture)fixtures.get(i)).getCompleteFixture();
			fixture.put("idFixture", data_fixture.get("id"));
			fixture.put("nameFixture", data_fixture.get("name"));
			f.add(fixture);
		}
		m.put("fixtures", f);
		if(error != null){
			m.put("error", error);
		}
		return m;
	}
}
