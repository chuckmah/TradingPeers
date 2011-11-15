package controllers;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import controllers.Secure.Security;

import models.Community;
import models.CommunityMember;
import models.CommunityQuote;
import models.CommunityStat;
import models.Portfolio;
import models.PortfolioStat;
import models.Quote;
import models.User;
import play.mvc.Controller;
import play.mvc.With;
import services.CommunityService;
import services.QuoteServices;
import services.ServicesException;

@With(Secure.class)
public class Communities extends Controller {

	
	public static void index(){
		
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());



		List<Community> myCommunities = CommunityService.getCommunitiesByUser(user);
		
		List<CommunityStat> communitieStats = CommunityService.getCommunityStats();

		
	  	renderArgs.put("user", user);
		renderArgs.put("communitieStats",communitieStats);	
		renderArgs.put("myCommunities",myCommunities);
		render();		
	}
	
	
	public static void view(String communityName){
		
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
	  	
		Community community = Community.findByName(communityName);
		
		if(CommunityService.isMember(user, community)){
			
			//load portfolios
			
			List<PortfolioStat> portfolioStats = CommunityService.getPortfolioStatsByCommunity(community);
			
			List<CommunityQuote> communityQuotes = CommunityService.getQuotes(community);
			

			renderArgs.put("community",community);
			renderArgs.put("portfolioStats",portfolioStats);
			renderArgs.put("communityQuotes",communityQuotes);
			render();
			
		}else{
			renderTemplate("Communities/notMember.html",community);
		}

		
	}
	
	public static void viewMember(String communityName, Long id){
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
	  	
	  	Community community = Community.findByName(communityName);
		
		if(CommunityService.isMember(user, community)){
			User userToView =  User.findById(id);
			
			if(CommunityMember.findByUserAndCommunity(community, userToView) != null){
				Portfolio portfolio = userToView.getPortfolio();
				renderArgs.put("community",community);
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
	
	public static void viewQuote(String communityName, String symbol){
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
	  	
		Community community = Community.findByName(communityName);
		
		if(CommunityService.isMember(user, community)){
			
			Quote quote = Quote.findBySymbol(symbol);

			CommunityQuote communityQuote = CommunityService.getCommunityQuote(community,quote);
		
			if(communityQuote != null){				
				renderArgs.put("communityQuote", communityQuote);
			}else{
				error("Quote " + symbol +" not found in community " + communityName);
			}
			
			render();
		}
	}
	
	
    public static void join(String communityName) {
		
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
	  	
		Community community = Community.findByName(communityName);
		
		if(CommunityService.join(user, community) == null){
			error("Can't join community !!");
		}
		
		view(community.name);
    }
    

	
}
