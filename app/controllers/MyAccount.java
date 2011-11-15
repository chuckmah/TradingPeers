package controllers;

import models.User;
import controllers.Secure.Security;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class MyAccount extends Controller {

	
	public static void index() {
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
		render();
	}
	
}
