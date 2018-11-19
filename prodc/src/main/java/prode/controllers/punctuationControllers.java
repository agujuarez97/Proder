package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import java.util.List;
import java.util.ArrayList;

import models.*;

public class punctuationControllers{

	public static ModelAndView global(Request request, Response response){
		Map rG = new HashMap();
		List<Score> top = Score.findBySQL("select user_id, sum(points) as points from scores group by user_id order by points desc;");
		List<Map> p = new ArrayList<Map>();
		for(int i = 0; i < top.size(); i++){
			List<User> u = User.where("id = ?", top.get(i).get("user_id"));
			Map a = new HashMap();
			a.put("username", u.get(0).get("username"));
			a.put("points", top.get(i).get("points"));
			p.add(a);
		}

		rG.put("ranking", p);

		return new ModelAndView(rG, "./views/global.html");
	}
	
	public static ModelAndView punctuation(Request request, Response response){
		Map rP = new HashMap();
		int id_u = (Integer)request.session().attribute("user");
		List<Map> p = new ArrayList<Map>();

		List<Map> q = new ArrayList<Map>();
		List<Score> scores = Score.where("user_id = ? order by schedure_id asc;", id_u);
		for(int j = 0; j < scores.size(); j++){
			Map b = new HashMap();
			b.put("schedule", "Puntos Fecha " + (Integer)scores.get(j).get("schedure_id"));
			b.put("pointsSchedules", (Integer)scores.get(j).get("points"));
			q.add(b);
		}

		List<Prediction> prediction = Prediction.where("user_id = ? order by schedure_id asc;", id_u);
		for(int i = 0; i < prediction.size(); i++){
			List<Game> games = Game.where("id = ?;", prediction.get(i).get("game_id"));
			Map a = new HashMap();
			Game g = games.get(0);
			if((Integer)g.getResult() != 0) {
				Map m = g.getCompleteGame();
				a.put("schedure_id", prediction.get(i).get("schedure_id"));
				a.put("local",((Team)m.get("local")).getName());
				a.put("golLocal", m.get("golLocal"));
				a.put("visitante",((Team)m.get("visitante")).getName());
				a.put("golVisitante", m.get("golVisitante"));
				if (prediction.get(i).get("result") == m.get("result")){
					a.put("acerto", "¡SI!");
					if ((Integer)prediction.get(i).get("result") == 3)
						a.put("prediction", "¡EMPATE!");
					if ((Integer)prediction.get(i).get("result") == 1)
						a.put("prediction", "¡GANA LOCAL!");
					if ((Integer)prediction.get(i).get("result") == 2)
						a.put("prediction", "¡GANA VISITANTE!");
				} else {
					a.put("acerto", "¡NO!");
					if ((Integer)prediction.get(i).get("result") == 3)
						a.put("prediction", "¡EMPATE!");
					if ((Integer)prediction.get(i).get("result") == 1)
						a.put("prediction", "¡GANA LOCAL!");
					if ((Integer)prediction.get(i).get("result") == 2)
						a.put("prediction", "¡GANA VISITANTE!");
				}
				p.add(a);
			}
		}

		rP.put("points", q);
		rP.put("punctuation", p);
		return new ModelAndView(rP, "./views/punctuation.html");
	}
}
