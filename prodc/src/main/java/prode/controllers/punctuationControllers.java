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

}
