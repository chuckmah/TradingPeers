package controllers.usermanagement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import models.User;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import controllers.Secure;

import play.Logger;
import play.Play;
import play.exceptions.UnexpectedException;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.ws.WSUrlFetch;

import play.modules.gae.GAE;
import play.mvc.*;
import play.mvc.Scope.Session;
import services.facebook.FacebookManager;
import services.facebook.FacebookProfil;

public class FBConnect extends Controller {

    public static String id = "171067816283919";
    public static String apiKey = "171067816283919";
    public static String secret = "0ca4cea84bead9d43ed3b89bc6126b39";
    public static String model;
    public static String landUrl;
	

    
    public static void fbLogin(boolean remember){
    	
    	//put the rememberMe flag and redirectUrl in the session (flash will not work)
    	
    	if(flash.get("url") != null){
        	session.put("redirectUrl", flash.get("url"));
    	}

    	session.put("rememberMe", remember);
    	redirect(FBConnect.getLoginUrl("email"));
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
    	
        String code = params.get("code");
        String error = params.get("error");
        if(error != null){


            flash.keep("url");
            flash.error("error_reason=" + WS.encode(params.get("error_reason")) + 
                    "&error_description=" + WS.encode(params.get("error_description")) +
                    "&error=" + WS.encode(params.get("error")));
            //Redirect to singIn
			Secure.signin();

        }
        if(code != null){
            String authUrl =  getAuthUrl(code);
            WSUrlFetch ws = new WSUrlFetch();
            String response = ws.newRequest(authUrl,"utf-8").get().getString();
            String accessToken = null;
            Integer expires = null;
            String[] pairs = response.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length != 2) {
                    throw new UnexpectedException("Module fbconnect got an unexpected auth response from facebook");
                } else {
                    if (kv[0].equals("access_token")) {
                        accessToken = kv[1];
                    }
                    if (kv[0].equals("expires")) {
                        expires = Integer.valueOf(kv[1]);
                    }
                }
            }
            

            if (accessToken != null && expires != null) {

            	    /*
                        String uri = "https://graph.facebook.com/me?access_token="+WS.encode(accessToken);
                        JsonObject jsonData = ws.newRequest(uri,"utf-8").get().getJson().getAsJsonObject();
                        jsonData.add("accessToken", new JsonPrimitive(accessToken));
                        jsonData.add("expires", new JsonPrimitive(expires));  
                        String email = jsonData.get("email").getAsString();
                    */    
            	FacebookProfil fbProfil = FacebookManager.getFacebookProfil(accessToken);
                 
            	
                User user = null;
            	if(fbProfil != null){
            		user = User.findByFbId(fbProfil.getFacebookId());
            		if(user == null){
            			user = new User();
            			user.email = fbProfil.getEmail();
            			user.userName = fbProfil.getUsername();
            			user.fbId = fbProfil.getFacebookId();
            			
            			
            			renderArgs.put("newUser", user);
            			renderTemplate("UserRegistration/register.html");
            			
            		}else{
        				Secure.authenticate(user.email, user.password, remeberMe);
        			}
                } else {
                    flash.keep("url");
                    flash.error("could not connect user using facebook ");
                    //Redirect to singIn
        			Secure.signin();
                }
            	
            }
        }
    }
    

    
    private static String getAuthUrl(String authCode){
        return String.format("https://graph.facebook.com/oauth/access_token?client_id=%s&redirect_uri=%s&client_secret=%s&code=%s", WS.encode(id), WS.encode(Router.getFullUrl("FBConnect.callback")), WS.encode(secret), WS.encode(authCode));
    }

	public static String getLoginUrl(String scope) {
        String url = String.format("https://www.facebook.com/dialog/oauth?client_id=%s&display=%s&redirect_uri=%s", WS.encode(id), WS.encode("page"), WS.encode(Router.getFullUrl("FBConnect.callback")));
        if(scope != null){
            url += "&scope="+WS.encode(scope);
        }
        return url;
	}
    
}
