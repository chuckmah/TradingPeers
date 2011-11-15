package services.facebook;

import com.restfb.Facebook;
import com.restfb.types.User;

public class FacebookProfil {

	private String facebookId;
	
	private String email;
	
	private String username;

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

