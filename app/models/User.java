package models;


import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;

import play.data.binding.NoBinding;
import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.mvc.Scope.Session;

import siena.*;



@Table("users")
public class User extends Model {

	@Id(Generator.AUTO_INCREMENT)
    public Long id;
	
	@Column("userName")
    @Max(25)
	@Required
	@play.data.validation.MaxSize(20) 
	@play.data.validation.MinSize(4)
	@play.data.validation.Match("^([A-Za-z]|[0-9]|_)+$")
    public String userName;
	
	@Column("email")
    @Max(255)
	@Required
	@play.data.validation.Email
    public String email;
    
	@Column("fbId")
    @Max(255)
    public String fbId;
	
	@Column("googleId")
    @Max(255)
    public String googleId;
	
	@Column("password")
    @Max(20)
    @Password
    @Required
    @play.data.validation.MaxSize(20) 
    @play.data.validation.MinSize(4)
	@play.data.validation.Match("^([A-Za-z]|[0-9]|_)+$")
    public String password;
  
	@Column("isAdmin")
	@play.data.binding.NoBinding("profile")
    public boolean isAdmin;
    
	
	@Filter("user")
    public Query<Portfolio> portfolios; 

    public User() {
	}
    
    public User(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.isAdmin = false;
    }
    
    public User(String email, String userName) {
        this.email = email;
        this.userName = userName;
        this.isAdmin = false;

    }



	public String toString()  {
        return "User(" + email + ")";
    }

	public static User findByEmailAndPassword(String username,
			String password) {
		
		return User.all(User.class).filter("email", username).filter("password", password).get();
	}

	public static User findByEmail(String email) {
		
		return User.all(User.class).filter("email", email).get();
	}
   
	
	public static User findByFbId(String fbId) {
		
		return User.all(User.class).filter("fbId", fbId).get();
	}
   
	
	public static User findByGoogleId(String googleId) {
		
		return User.all(User.class).filter("googleId", googleId).get();
	}
   
	
	public static User findByUserName(String userName) {

		return User.all(User.class).filter("userName", userName).get();
	}

	public  Portfolio getPortfolio() {
		
		return  portfolios.get();

	}

	public static User findById(Long id) {
		
		return User.all(User.class).filter("id", id).get();
	}


    
}
