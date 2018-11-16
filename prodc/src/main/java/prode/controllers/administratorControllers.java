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

	public static ModelAndView addschedule(Request request, Response response){
		Map m = new HashMap();
		return new ModelAndView(m, "./views/registerschedule.html");
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
	
}
