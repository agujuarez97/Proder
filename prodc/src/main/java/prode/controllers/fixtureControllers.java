package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.*;

import models.*;

public class fixtureControllers {

	public static ModelAndView buscarFixture(Request request, Response response){
		Map m = new HashMap();
		return new ModelAndView(m, "./views/buscarFixture.html");
	}

	public static ModelAndView fixture(Request request, Response response){
		Map fix = new HashMap();
		List<Fixture> fixtures = Fixture.findBySQL("select * from fixtures where name = ?;", request.queryParams("fixture"));
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
					a.put("goalLocal", dataGame.get("golLocal"));
					a.put("goalVisitante", dataGame.get("golVisitante"));
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
			return new ModelAndView(fix, "./views/buscarFixture.html");
		}
	}
}