package services;

import java.util.List;

import models.Community;
import models.CommunityMember;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import utils.TradingPeersTestHelper;

public class CommunityServiceTest extends UnitTest {

	@Before
	public void setUp() {

		TradingPeersTestHelper.loadTestData();
		
		
	}
	
	@Test
	public void isMemberTest(){
		User userA = TradingPeersTestHelper.getUserTestA();
		Community community1 = TradingPeersTestHelper.getCommunityTest1();
		Community community2 = TradingPeersTestHelper.getCommunityTest2();
		
		assertTrue(CommunityService.isMember(userA, community1));
		assertFalse(CommunityService.isMember(userA, community2));
	}

	
	@Test
	public void joinTest(){
		User userA = TradingPeersTestHelper.getUserTestA();
		Community community1 = TradingPeersTestHelper.getCommunityTest1();
		Community community2 = TradingPeersTestHelper.getCommunityTest2();
		
		CommunityMember communityMember = CommunityService.join(userA, community1);
		
		
		//is null can't join an already joined community
		assertNull(communityMember);
		
		communityMember = CommunityService.join(userA, community2);
		
		assertNotNull(communityMember);
		assertNotNull(communityMember.id);
	}
	
	
	@Test
	public void retrieveCommunCommunityTest(){
		
		
		List<Community> communities = CommunityService.getCommunCommunities(TradingPeersTestHelper.getUserTestA(), TradingPeersTestHelper.getUserTestB());
		
		assertNotNull(communities);
		assertEquals(1, communities.size());
		
		
	}
	
	@Test
	public void retrieveCommunCommunityTest2(){
		
		
		List<Community> communities = CommunityService.getCommunCommunities(TradingPeersTestHelper.getUserTestA(), TradingPeersTestHelper.getUserTestB());
		
		assertNotNull(communities);
		assertEquals(1, communities.size());
	
	}
}
