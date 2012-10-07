package edu.psu.sweng.ff.client;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.psu.sweng.ff.common.League;
import edu.psu.sweng.ff.common.Member;

public class LeaguesTest {

	@Test
	public void testCreate() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		
		League l = new League();
		l.setName("test league 1");
		l.setAutoDraft(true);
		l.setCommissioner(Members.getTokenOwner());
		l.setWeek(1);
		
		l = Leagues.create(l);
		assertTrue(l.getId() > 0);

	}

	@Test
	public void testJoin() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		Member testMember = Members.getTokenOwner();
		List<League> leagues = Leagues.getByMember(testMember);
		assertTrue(leagues.size() > 0);
		League l = leagues.get(0);

		// create a new member to join one of test's leagues
		String newUserName = this.generateRandomUserName();
    	Member m = new Member();
		m.setUserName(newUserName);
		m.setEmail("test@test.net");
		m.setFirstName("John");
		m.setLastName("Doe");
		m.setHideEmail(false);
		m.setMobileNumber("717-555-1212");
		m.setPassword("test_password");
		
		Member c = Members.create(m);
		
		String token2 = Members.authenticate(newUserName, "test_password");
		Leagues.setUserToken(token2);
    	
    	Leagues.join(l);
    	
    	List<League> ll = Leagues.getByMember(c);
    	assertTrue(ll.size() == 1);

    	League justJoined = ll.get(0);
    	assertEquals(justJoined.getId(), l.getId());
		
	}
	
	@Test
	public void testLoadbyId() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		
		League l = new League();
		l.setName("test league 2");
		l.setAutoDraft(true);
		l.setCommissioner(Members.getTokenOwner());
		l.setWeek(1);
		
		l = Leagues.create(l);
		
		League l2 = Leagues.getById(String.valueOf(l.getId()));
		
		assertEquals(l.getId(), l2.getId());
		
	}
	
	@Test
	public void testLoadByMember() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);

		List<League> list = Leagues.getByMember(Members.getTokenOwner());
		assertTrue(list.size() > 0) ;
		
	}
	
    private String generateRandomUserName() {
    	
    	String un = "johndoe";
    	int r = 1000 + (int)(Math.random() * ((999999 - 1000) + 1));
    	return un + r;
    	
    }

	
}
