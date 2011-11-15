package controllers;

import com.google.apphosting.api.UserServicePb.UserService;

import controllers.Secure;
import models.User;
import play.data.binding.As;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.Scope.RenderArgs;
import services.ServicesException;
import services.UserServices;

public class UserRegistration extends Controller {



	

	public static void register(){
		flash.keep();
		render();
	}
	
	public static void executeRegistration(@As("profile") @Valid  User newUser, String verifyPassword, boolean remember){
	
		//Validate user object.

        validation.required(verifyPassword);
        validation.equals(verifyPassword, newUser.password).message("validation.password.noMatch");

        if(validation.hasErrors()) {
        	// params.flash(); // add http parameters to the flash scope
        	 render("@register", newUser, verifyPassword, remember);
        }
		//save the user in the database
		try {
			UserServices.createNewUser(newUser);
		} catch (ServicesException e) {
			flash.error(e.getMessage());
			//params.flash(); // add http parameters to the flash scope
			render("@register", newUser,verifyPassword, remember);
		}

	    //connect the user (will be redirected to /)
		Secure.authenticate(newUser.email, newUser.password, false);
		
		
	}
}
