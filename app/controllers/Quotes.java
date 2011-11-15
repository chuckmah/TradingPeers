package controllers;

import play.mvc.Controller;
import services.QuoteServices;

public class Quotes extends Controller {

	
	public static void refreshAllQuotes(){
		
		QuoteServices.refreshAllQuotes();
	}
	
}
