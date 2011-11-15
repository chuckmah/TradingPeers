package controllers;

import java.util.List;

import controllers.Secure.Security;

import models.Community;
import models.Portfolio;
import models.User;
import play.mvc.Controller;
import play.mvc.With;
import services.CommunityService;
import services.UserServices;

@With(Secure.class)
public class Portfolios extends Controller {
	
	
	public static void view(Long id){
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
		//Community community = Community.findByName(communityName);
		User userToView =  User.findById(id);
		List<Community> relatedCommunities = CommunityService.getCommunCommunities(user, userToView);
		
		
		Portfolio portfolio = userToView.getPortfolio();
		if(relatedCommunities.size() > 0){
			renderArgs.put("user", userToView);
			renderArgs.put("portfolio", portfolio);
			renderArgs.put("portfolioEntries", portfolio.loadPortfolioEntries());
			renderArgs.put("transactions", portfolio.loadPortfolioTransactions());
			render();
		}else{
			renderTemplate("Profiles/notRelated.html");
		}

	}
}
