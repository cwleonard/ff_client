package edu.psu.sweng.ff.client;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Test;

import edu.psu.sweng.ff.common.League;
import edu.psu.sweng.ff.common.Member;
import edu.psu.sweng.ff.common.Player;
import edu.psu.sweng.ff.common.Roster;
import edu.psu.sweng.ff.common.Team;

public class TeamsTest {

	@Test
	public void testCreate() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		Teams.setUserToken(token);
		
		Member m = Members.getTokenOwner();
		
		League l = new League();
		l.setName("team test league 1");
		l.setAutoDraft(true);
		l.setCommissioner(m);
		l.setWeek(1);
		
		l = Leagues.create(l);

		assertTrue(l.getId() > 0);

		Team t = new Team();
		t.setLeagueId(l.getId());
		t.setLogo("logo");
		t.setName("test team A");
		t.setOwner(m);
		
		t = Teams.create(t);
		
		assertTrue(t.getId() > 0);

	}

	@Test
	public void testUpdate() {
		
		String token = Members.authenticate("test", "password");
		Teams.setUserToken(token);
		
		List<Team> teams = Teams.getByOwner();
		assertTrue(teams.size() > 0);

		Team toUpdate = teams.get(0);
		String newName = "updated " + toUpdate.getName();
		toUpdate.setName(newName);
		Teams.update(toUpdate);
		
		Team afterUpdate = Teams.getById(String.valueOf(toUpdate.getId()));
		
		assertEquals(newName, afterUpdate.getName());

	}

	@Test
	public void testLoad() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		Teams.setUserToken(token);

		Member m = Members.getTokenOwner();

		League l = new League();
		l.setName("team test league 2");
		l.setAutoDraft(true);
		l.setCommissioner(m);
		l.setWeek(1);
		
		l = Leagues.create(l);

		Team t = new Team();
		t.setLeagueId(l.getId());
		t.setLogo("logo");
		t.setName("test team B");
		t.setOwner(m);
		
		t = Teams.create(t);
		
		
		Team t2 = Teams.getById(String.valueOf(t.getId()));
		
		assertEquals(t.getId(), t2.getId());
		assertEquals(t.getName(), t2.getName());
		
	}

	@Test
	public void testLoadRosters() {
		
		String token = Members.authenticate("test", "password");
		Leagues.setUserToken(token);
		Teams.setUserToken(token);

		Member m = Members.getTokenOwner();

		League l = new League();
		l.setName("team test league 3");
		l.setAutoDraft(true);
		l.setCommissioner(m);
		l.setWeek(1);
		
		l = Leagues.create(l);

		Team t = new Team();
		t.setLeagueId(l.getId());
		t.setLogo("logo");
		t.setName("test team C");
		t.setOwner(m);
		
		Roster r = t.getRoster(1);
		Player p = new Player();
		p.setId(UUID.randomUUID().toString());
		p.setFirstName("Test");
		p.setLastName("Player");
		r.addStartingPlayer(p);
		
		t = Teams.create(t);
		
		Team t2 = Teams.getById(String.valueOf(t.getId()));
		
		assertTrue(t2.getRoster(1).getStartingPlayers().size() == 1);
		
	}

	@Test
	public void testLoadByOwner() {
		
		String token = Members.authenticate("test", "password");
		Teams.setUserToken(token);

		List<Team> list = Teams.getByOwner();
		assertTrue(list.size() > 0) ;
		
	}
	
}
