package edu.psu.sweng.ff.client;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.psu.sweng.ff.common.League;

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
	public void testLoad() {
		
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
	
}
