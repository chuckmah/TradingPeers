package services.facebook;


import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;

public class FacebookManager {

	public static FacebookProfil getFacebookProfil(String tokenId){
		FacebookProfil fbProfil = new FacebookProfil();
		
		FacebookClient facebookClient = new DefaultFacebookClient(tokenId);
		
		User user= facebookClient.fetchObject("me", User.class);
		fbProfil.setFacebookId(user.getId());
		fbProfil.setEmail(user.getEmail());
		fbProfil.setUsername(user.getUsername());
		
		return fbProfil;
		
	}
}
