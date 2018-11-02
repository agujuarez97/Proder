package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class startControllers{

	public static ModelAndView start(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/inicio.html");
	}
	
}
