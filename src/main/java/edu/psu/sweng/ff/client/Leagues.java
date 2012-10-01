package edu.psu.sweng.ff.client;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.psu.sweng.ff.common.League;
import edu.psu.sweng.ff.common.LeagueList;
import edu.psu.sweng.ff.common.Member;

public class Leagues {

	private final static String TOKEN_HEADER = "X-UserToken";
	
	private final static String BASE_URL = "http://www.amphibian.com/ff_server/resource/league/";

	private static String userToken;
	
	private static Client c = Client.create();
	
	public static void setUserToken(String t) {
		userToken = t;
	}

	public static List<League> getByMember(Member m) {
	
		String url = BASE_URL + "?member=" + m.getId();
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
		
		List<League> l = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			LeagueList ll = response.getEntity(LeagueList.class);
			l = ll.getLeagues();
		} else {
			System.err.println(response);
		}
		
		return l;
	
	}

	public static League getById(String id) {
		
		String url = BASE_URL + id;
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
		
		League l = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			l = response.getEntity(League.class);
		} else {
			System.err.println(response);
		}
		
		return l;
	
	}

	public static League update(League l) {
		
		String url = BASE_URL + l.getId();
		
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.entity(l).put(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return l;
		} else {
			System.err.println(response);
		}
		return null;
		
	}

	public static League create(League l) {
		
		String url = BASE_URL;
		
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken).entity(l)
				.post(ClientResponse.class);
		URI u = response.getLocation();
		String uri = u.toString();
		String id = uri.substring(uri.lastIndexOf('/') + 1);
		l.setId(Integer.parseInt(id));
		return l;
		
	}

	
}