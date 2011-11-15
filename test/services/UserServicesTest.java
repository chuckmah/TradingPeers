package services;

import java.util.List;

import models.Community;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import utils.TradingPeersTestHelper;

public class UserServicesTest extends UnitTest{
	
	@Before
	public void setUp() {

		TradingPeersTestHelper.loadTestData();
		
	}

	
	@Test(expected=ServicesException.class)
	public void createNewUserAlreadyExistTest() throws ServicesException{
		User user = new User("testuser_a@test.com", "someThing", null);
		UserServices.createNewUser(user);
	}
	
}
