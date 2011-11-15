package models;
import org.junit.*;
import java.util.*;

import play.Logger;
import play.modules.siena.SienaFixtures;
import play.test.*;
import utils.TradingPeersTestHelper;
import models.*;

public class UsersTest extends UnitTest {

	@Before
	public void setUp() {
		
		TradingPeersTestHelper.loadTestData();
		
	}
	
    @Test
    public void testUsers() {

    	assertNotNull(User.findByEmailAndPassword("choc_mah@hotmail.com", "G1q1q9!"));
    	assertNull(User.findByEmailAndPassword("choc_mah@hotmail.com", "G1q1q9!_notValid"));
    	assertNull(User.findByEmailAndPassword("choc_mah@hotmail.com_notValid", "G1q1q9!"));
    	
    }
    

    

}
