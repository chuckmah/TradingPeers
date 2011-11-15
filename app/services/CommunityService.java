package services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Community;
import models.CommunityMember;
import models.CommunityQuote;
import models.CommunityStat;
import models.Portfolio;
import models.PortfolioEntry;
import models.PortfolioStat;
import models.PortfolioTransaction;
import models.Quote;
import models.User;

public class CommunityService {

	
	public static List<CommunityStat> getCommunityStats(){
		List<Community> communities = Community.all(Community.class).fetch();
		
		List<CommunityStat> communityStats = new ArrayList<CommunityStat>();
		for (Community community : communities) {
			CommunityStat communityStat = new CommunityStat(community);
			
			List<CommunityMember> communityMembers =  CommunityMember.findByCommunity(community);
			
			int userTotal = communityMembers.size();

			communityStat.usersTotal = userTotal;
			communityStats.add(communityStat);
			
		}
		


		
		return communityStats;
	}
	
	public static List<Community> getCommunitiesByUser(User user){
		List<Community> result = new ArrayList<Community>();
		
		List<CommunityMember> userCommunityMembers = CommunityMember.findByUser(user);
		
		for (CommunityMember userCommunityMember : userCommunityMembers) {
			userCommunityMember.community.get();
			result.add(userCommunityMember.community);
		}
		
		return result;
	}
	
	public static List<Community> getCommunCommunities(User user1, User user2){
		List<Community> result = new ArrayList<Community>();
		
		List<CommunityMember> user1CommunityMembers = CommunityMember.findByUser(user1);
		List<CommunityMember> user2CommunityMembers = CommunityMember.findByUser(user2);
		
		
		for (CommunityMember user1CommunityMember : user1CommunityMembers) {
			
			for (CommunityMember user2CommunityMember : user2CommunityMembers) {
				if(user1CommunityMember.community.id.equals(user2CommunityMember.community.id)){
					user1CommunityMember.community.get();
					result.add(user1CommunityMember.community);
				}
			}

		}
		
		return result;
		
	}
	
	public static boolean isMember(User user, Community community){
	
		if(CommunityMember.findByUserAndCommunity(community, user) != null)
			return true;
		return false;
		
	}

	public static  CommunityMember join(User user, Community community) {

		if(!isMember(user, community)){
			CommunityMember communityMember = new CommunityMember(user, community);
			communityMember.insert();
			return communityMember;
		}
		return null;
	}


	public static List<PortfolioStat> getPortfolioStatsByCommunity(Community community) {
		List<CommunityMember> communityMembers =  CommunityMember.findByCommunity(community);
		
		List<PortfolioStat> result = new ArrayList<PortfolioStat> ();
		
		for (CommunityMember communityMember : communityMembers) {
			
			communityMember.user.get();
		
			Portfolio portfolio = communityMember.user.getPortfolio();

			result.add(getPortfolioStat(portfolio));
		}
		
		return result;
	}
	
	public static PortfolioStat getPortfolioStat(Portfolio portfolio ){
		
		//init the user in portfolio;
		portfolio.user.get();		
		
		List<PortfolioEntry> portfolioEntries = portfolio.loadPortfolioEntries();
		List<PortfolioTransaction> portfolioTransactions = portfolio.loadPortfolioTransactions();
		
		PortfolioStat portfolioStat  = new PortfolioStat(portfolio, portfolioEntries.size(), portfolioTransactions.size());

		//Calculate the market value of a portfolio
		BigDecimal marketValue = portfolio.balance;
		for (PortfolioEntry portfolioEntry : portfolioEntries) {
			portfolioEntry.quote.get();
			Quote quote = portfolioEntry.quote;
			
			BigDecimal quotePrice = quote.marketPrice;
			BigDecimal qty = new BigDecimal(portfolioEntry.shareQty);
			//calculate market value
			BigDecimal entryMarketValue = quotePrice.multiply(qty);
			marketValue = marketValue.add(entryMarketValue);
		}
		
		marketValue= marketValue.setScale(2,RoundingMode.HALF_UP);
		
		portfolioStat.marketValue  = marketValue;
		
		BigDecimal 	startingBalance =	new BigDecimal(UserServices.STARTING_BALANCE);
		
		//calculate change
		BigDecimal change = (marketValue.subtract(startingBalance)).divide(startingBalance, 4, RoundingMode.HALF_UP);
	
		change = change.multiply(new BigDecimal(100));
		change = change.setScale(2,RoundingMode.HALF_UP);
		
		if(startingBalance.doubleValue() < marketValue.doubleValue()){
			portfolioStat.marketValueChange = "+" + change.toPlainString() + "%";
		}else{
			portfolioStat.marketValueChange = change.toPlainString() + "%";
		}
		
		return portfolioStat;
		
	}
	
	
	public static CommunityQuote getCommunityQuote(Community community, Quote quote){
		List<CommunityMember> communityMembers =  CommunityMember.findByCommunity(community);
		
		CommunityQuote result = new CommunityQuote(community, quote);
		
		for (CommunityMember communityMember : communityMembers) {
			communityMember.user.get();
			Portfolio portfolio = communityMember.user.getPortfolio();
			
			//iterate trough PortfolioEntry
			List<PortfolioEntry> portfolioEntries = portfolio.loadPortfolioEntries();
			for (PortfolioEntry portfolioEntry : portfolioEntries) {
				
				String symbol = portfolioEntry.quote.symbol;
				
				if(symbol.equals(quote.symbol)){
					portfolioEntry.portfolio.get();
					portfolioEntry.portfolio.user.get();
					result.portfolioEntries.add(portfolioEntry);
				}
				
			}
			
			//iterate trough getPortfolioTransaction
			List<PortfolioTransaction> portfolioTransactions = portfolio.loadPortfolioTransactions();
			for (PortfolioTransaction portfolioTransaction : portfolioTransactions) {
				String symbol = portfolioTransaction.quote.symbol;
				
				if(symbol.equals(quote.symbol)){
					portfolioTransaction.portfolio.get();
					portfolioTransaction.portfolio.user.get();
					result.portfolioTransactions.add(portfolioTransaction);
				}
				
			}
			
			
		}
		
		return result;
	}
	
	public static List<CommunityQuote> getQuotes(Community community){
		List<CommunityMember> communityMembers =  CommunityMember.findByCommunity(community);
		

		Map<String,CommunityQuote> resultMap = new HashMap<String,CommunityQuote> ();

		for (CommunityMember communityMember : communityMembers) {
			communityMember.user.get();
			Portfolio portfolio = communityMember.user.getPortfolio();
			
			//iterate trough PortfolioEntry
			List<PortfolioEntry> portfolioEntries = portfolio.loadPortfolioEntries();
			
			for (PortfolioEntry portfolioEntry : portfolioEntries) {

				String symbol = portfolioEntry.quote.symbol;
				
				if(resultMap.get(symbol) != null){
					CommunityQuote communityQuote = resultMap.get(symbol);
					communityQuote.portfolioEntries.add(portfolioEntry);
					
				}else{
					CommunityQuote communityQuote = new CommunityQuote(community,portfolioEntry.quote );		
					communityQuote.portfolioEntries.add(portfolioEntry);
					resultMap.put(symbol, communityQuote);
				}
			}
			
			//iterate trough getPortfolioTransaction
			List<PortfolioTransaction> portfolioTransactions = portfolio.loadPortfolioTransactions();
			
			for (PortfolioTransaction portfolioTransaction : portfolioTransactions) {

				String symbol = portfolioTransaction.quote.symbol;
				
				if(resultMap.get(symbol) != null){
					CommunityQuote communityQuote = resultMap.get(symbol);
					communityQuote.portfolioTransactions.add(portfolioTransaction);
					
				}else{
					CommunityQuote communityQuote = new CommunityQuote(community,portfolioTransaction.quote );		
					communityQuote.portfolioTransactions.add(portfolioTransaction);
					resultMap.put(symbol, communityQuote);
				}
			}
			
		}
		
		
		return new ArrayList<CommunityQuote>(resultMap.values());
	}
	
}
