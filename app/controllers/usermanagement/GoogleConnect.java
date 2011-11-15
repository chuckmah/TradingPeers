package controllers.usermanagement;

import models.User;

import org.apache.commons.lang.StringUtils;

import controllers.Secure;
import controllers.Secure.Security;

import play.modules.gae.GAE;
import play.mvc.Controller;
import play.mvc.Router;
import play.mvc.Scope.Session;
import services.ServicesException;
import services.UserServices;

public class GoogleConnect extends Controller {

	
    public static void googleLogin(boolean remember){
    	//put the rememberMe flag and redirectUrl in the session (flash will not work)
    	
    	if(flash.get("url") != null){
        	session.put("redirectUrl", flash.get("url"));
    	}
    	session.put("rememberMe", remember);
    	
        GAE.login("usermanagement.GoogleConnect.callback");
    }
	
	public static void callback() throws Throwable {


		
		boolean remeberMe = false;
		if(!StringUtils.isEmpty(session.get("rememberMe"))){
			if(Boolean.valueOf(session.get("rememberMe"))){
				remeberMe = true;
			}
		   	session.remove("rememberMe");
		}
		
		if(session.get("redirectUrl") != null){
	    	flash.put("url", session.get("redirectUrl"));
	    	session.remove("url");
		}

		
		if (GAE.isLoggedIn()) {

			String userId = GAE.getUser().getUserId();
			String userName = GAE.getUser().getNickname();
			String email = GAE.getUser().getEmail();
			
			User user = null;
			
			if(!StringUtils.isEmpty(userId) ){
				user = User.findByGoogleId(userId);

				if (user == null) {
						
						user = new User();
						user.email = email;
						user.googleId = userId;
						user.userName = userName;
						
	        			renderArgs.put("newUser", user);
	        			renderTemplate("UserRegistration/register.html");
						
				  }

					Secure.authenticate(user.email, user.password,remeberMe);
			
			}else{
				flash.error("Unable to connect using Google Connect");
				flash.keep("url");
				Secure.signin();
			}
		}
	}
	
	

}
