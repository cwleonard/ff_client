package edu.psu.sweng.ff.client;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.psu.sweng.ff.common.League;
import edu.psu.sweng.ff.common.Member;
import edu.psu.sweng.ff.common.Player;
import edu.psu.sweng.ff.common.Roster;
import edu.psu.sweng.ff.common.Team;

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
	
	@Test
	public void testInvitations() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		
		String ln = generateRandomLeagueName();
		League l = new League();
		l.setName(ln);
		l.setAutoDraft(true);
		l.setCommissioner(Members.getTokenOwner());
		l.setWeek(1);
		
		l = Leagues.create(l);
		assertTrue(l.getId() > 0);

		List<String> emails = new ArrayList<String>();
		emails.add("casey@amphibian.com");
		assertTrue(Leagues.invite(l, emails));
		
		// create a new member with the same email address
		String newUserName = this.generateRandomUserName();
    	Member m = new Member();
		m.setUserName(newUserName);
		m.setEmail("casey@amphibian.com");
		m.setFirstName("John");
		m.setLastName("Doe");
		m.setHideEmail(false);
		m.setMobileNumber("717-555-1212");
		m.setPassword("test_password");
		
		// make sure invite is there right from the creation
		Member c = Members.create(m);
		assertTrue(c.getInvitations().size() > 0);
		
		// make sure invite is there after load as well
		c = Members.getByUserId(String.valueOf(c.getId()));
		assertTrue(c.getInvitations().size() > 0);
		
	}
	
	@Test
	public void testStartDraft() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		Teams.setUserToken(token);
		Member m = Members.getTokenOwner();
		
		League l = new League();
		l.setName("start draft test league");
		l.setAutoDraft(true);
		l.setCommissioner(Members.getTokenOwner());
		l.setWeek(1);
		
		l = Leagues.create(l);

		Team t = new Team();
		t.setLeagueId(l.getId());
		t.setLogo("logo");
		t.setName("draft test team 1");
		t.setOwner(m);
		
		t = Teams.create(t);
		
		Team t2 = new Team();
		t2.setLeagueId(l.getId());
		t2.setLogo("logo");
		t2.setName("draft test team 2");
		t2.setOwner(m);
		
		t2 = Teams.create(t2);
		
		assertTrue(Leagues.startDraft(l));
		
		// now check team rosters
		t = Teams.getById(String.valueOf(t.getId()));
		Roster r = t.getRoster(1);
		assertEquals(10, r.getStartingPlayers().size());
		assertEquals(10, r.getBenchPlayers().size());
		
		t2 = Teams.getById(String.valueOf(t2.getId()));
		Roster r2 = t2.getRoster(1);
		assertEquals(10, r2.getStartingPlayers().size());
		assertEquals(10, r2.getBenchPlayers().size());
		
		
	}
	
	@Test
	public void testGetAvailablePlayers() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		Teams.setUserToken(token);
		Member m = Members.getTokenOwner();
		
		League l = new League();
		l.setName("get players test league");
		l.setAutoDraft(false);
		l.setCommissioner(Members.getTokenOwner());
		l.setWeek(1);
		
		l = Leagues.create(l);

		Team t = new Team();
		t.setLeagueId(l.getId());
		t.setLogo("logo");
		t.setName("draft test team 1");
		t.setOwner(m);
		
		t = Teams.create(t);
		
		Team t2 = new Team();
		t2.setLeagueId(l.getId());
		t2.setLogo("logo");
		t2.setName("draft test team 2");
		t2.setOwner(m);
		
		t2 = Teams.create(t2);
		
		assertTrue(Leagues.startDraft(l));
		
		List<Player> players = Leagues.getAvailablePlayers(l);
		assertTrue(players.size() > 0);
		
		//TODO: make sure a drafted player is no longer available
		
	}
	
	
	
    private String generateRandomUserName() {
    	
    	String un = "johndoe";
    	int r = 1000 + (int)(Math.random() * ((999999 - 1000) + 1));
    	return un + r;
    	
    }

    private String generateRandomLeagueName() {
    	
    	String ln = "test league ";
    	int r = 1000 + (int)(Math.random() * ((999999 - 1000) + 1));
    	return ln + r;
    	
    }

	
}
