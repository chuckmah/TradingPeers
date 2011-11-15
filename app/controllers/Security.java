package controllers;

import controllers.Secure;

import models.User;

public class Security extends controllers.Secure.Security {

	
    /**
     * This method checks that a profile is allowed to view this page/method. This method is called prior
     * to the method's controller annotated with the @Check method. 
     *
     * @param profile
     * @return true if you are allowed to execute this controller method.
     */
    static boolean check(String profile) {
        if("admin".equals(profile)) {
            return User.findByEmail(connected()).isAdmin;
        }
        return false;
    }
    
    /**
     * This method is called during the authentication process. This is where you check if
     * the user is allowed to log in into the system. This is the actual authentication process
     * against a third party system (most of the time a DB).
     *
     * @param username
     * @param password
     * @return true if the authentication process succeeded
     */
    static boolean authenticate(String username, String password) {
    	User user = User.findByEmailAndPassword(username,password);	
        return user != null ? true : false;
    }
    
    /**
    * This method is called before a user tries to sign off.
    * You need to override this method if you wish to perform specific actions (eg. Record the name of the user who signed off)
    */
   static void onDisconnect() {

   }
    

}
