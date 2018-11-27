/**
 * Title: startControllers
 * Description: This class controls the start to the game
 * @author: Agustin Juarez, Gaston Plisga, Matias Suarez  
*/
package controllers;

import spark.Request;
import spark.Response;

import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class startControllers{

	/**
	* Description: This method redirects the inicio.html view
	* @return: ModelAndView
	*/
	public static ModelAndView start(Request request, Response response){
		Map map = new HashMap();
		return new ModelAndView(map, "./views/inicio.html");
	}
	
}
