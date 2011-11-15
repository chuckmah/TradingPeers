package services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import models.Community;
import models.CommunityMember;
import models.Portfolio;
import models.User;

public class UserServices {

	public static int STARTING_BALANCE = 10000;

	
	public static User createNewUser(User user) throws ServicesException {
	
		if(User.findByEmail(user.email) != null){
			throw new ServicesException("validation.email.notUnique");
		}
		if(User.findByUserName(user.userName) != null){
			throw new ServicesException("validation.userName.notUnique");
		}
		
		
        user.insert();
        
		
		 //create the portfolio
       
       Portfolio porfolio = new Portfolio(new BigDecimal(STARTING_BALANCE));
       porfolio.user = user;
       
       porfolio.insert();
      
        
        
        return user;
	}



}
