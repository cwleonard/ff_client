package edu.psu.sweng.ff.client;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.psu.sweng.ff.common.League;
import edu.psu.sweng.ff.common.Member;
import edu.psu.sweng.ff.common.Player;

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
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		List<League> l = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			Type collectionType = new TypeToken<List<League>>(){}.getType();
			l = gson.fromJson(json, collectionType);
			
		} else {
			System.err.println(response);
		}
		
		return l;
	
	}

	public static League getById(String id) {
		
		String url = BASE_URL + id;
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		League l = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			l = gson.fromJson(json, League.class);
		} else {
			System.err.println(response);
		}
		
		return l;
	
	}

	public static League update(League l) {
		
		String url = BASE_URL + l.getId();

		Gson gson = new Gson();
		String json = gson.toJson(l);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.type(MediaType.APPLICATION_JSON).entity(json).put(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return l;
		} else {
			System.err.println(response);
		}
		return null;
		
	}

	public static League create(League l) {
		
		String url = BASE_URL;

		Gson gson = new Gson();
		String json = gson.toJson(l);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken).entity(json)
				.type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		URI u = response.getLocation();
		String uri = u.toString();
		String id = uri.substring(uri.lastIndexOf('/') + 1);
		l.setId(Integer.parseInt(id));
		return l;
		
	}
	
	public static boolean join(League l) {
		
		String url = BASE_URL + l.getId() + "/join";

		Gson gson = new Gson();
		String json = gson.toJson(l);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.type(MediaType.APPLICATION_JSON).entity(json).post(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} else {
			System.err.println(response);
		}
		return false;
		
	}
	
	public static boolean invite(League l, List<String> emails) {
		
		String url = BASE_URL + l.getId() + "/invite";

		Gson gson = new Gson();
		String json = gson.toJson(emails);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.type(MediaType.APPLICATION_JSON).entity(json).post(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} else {
			System.err.println(response);
		}
		return false;
		
	}

	public static boolean startDraft(League l) {
		
		String url = BASE_URL + l.getId() + "/startdraft";

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.post(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} else {
			System.err.println(response);
		}
		return false;
		
	}
	
	public static List<Player> getAvailablePlayers(League l) {
		
		String url = BASE_URL + l.getId() + "/players";

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		List<Player> lp = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			Type collectionType = new TypeToken<List<Player>>(){}.getType();
			lp = gson.fromJson(json, collectionType);
			
		} else {
			System.err.println(response);
		}
		
		return lp;
		
	}
	
	public static boolean draftPlayer(League l, Player p) {
		
		String url = BASE_URL + l.getId() + "/draftplayer";

		Gson gson = new Gson();
		String json = gson.toJson(p);
		
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.type(MediaType.APPLICATION_JSON).entity(json)
				.post(ClientResponse.class);
		
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return true;
		} else {
			return false;
		}
	
	}
	
}
