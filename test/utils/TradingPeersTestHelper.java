package utils;

import models.Community;
import models.Portfolio;
import models.User;
import play.modules.siena.SienaFixtures;
import play.test.UnitTest;

public class TradingPeersTestHelper extends UnitTest{

	
	public static void loadTestData(){
		String fileName = "data.yml";
		SienaFixtures.deleteAllModels();
		SienaFixtures.loadModels(fileName);
	}
	
	
	public static User getUserTestA(){
		User userTestA = User.findByEmail("testuser_a@test.com");
    	assertNotNull(userTestA);
		return userTestA;
	}
	
	public static User getUserTestB(){
		User userTestA = User.findByEmail("testuser_b@test.com");
    	assertNotNull(userTestA);
		return userTestA;
	}

	public static Community getCommunityTest1(){
		Community community = Community.findByName("test");
    	assertNotNull(community);
		return community;
	}
	
	public static Community getCommunityTest2(){
		Community community = Community.findByName("test2");
    	assertNotNull(community);
		return community;
	}
	
	public static Portfolio getUserTestAPortfolio() {
    	//we should be able to retrieve portfolio from the portfolios query.
    	Portfolio portfolio = getUserTestA().portfolios.get();
    	assertNotNull(portfolio);
		return portfolio;
	}
}
