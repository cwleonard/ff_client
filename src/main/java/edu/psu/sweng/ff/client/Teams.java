package edu.psu.sweng.ff.client;

import java.lang.reflect.Type;
import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

import edu.psu.sweng.ff.common.Team;

public class Teams {

	private final static String TOKEN_HEADER = "X-UserToken";
	
	private final static String BASE_URL = "https://amphibian.com/ff_server/resource/team/";

	private static String userToken;
	
	private static Client c = Client.create();

	//TODO: don't do this if we had a "real" (not self-signed) ssl certificate
	static {
		
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
		    public X509Certificate[] getAcceptedIssuers(){return null;}
		    public void checkClientTrusted(X509Certificate[] certs, String authType){}
		    public void checkServerTrusted(X509Certificate[] certs, String authType){}
		}};

		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("TLS");
		    sc.init(null, trustAllCerts, new SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
	}

	public static void setUserToken(String t) {
		userToken = t;
	}

	public static List<Team> getByOwner() {
	
		String url = BASE_URL;
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		List<Team> teams = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			Type collectionType = new TypeToken<List<Team>>(){}.getType();
			teams = gson.fromJson(json, collectionType);
			
		} else {
			System.err.println(response);
		}
		
		return teams;
	
	}

	public static Team getById(String id) {
		
		String url = BASE_URL + id;
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		Team t = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			t = gson.fromJson(json, Team.class);
		} else {
			System.err.println(response);
		}
		
		return t;
	
	}

	public static Team update(Team t) {
		
		String url = BASE_URL + t.getId();

		Gson gson = new Gson();
		String json = gson.toJson(t);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.type(MediaType.APPLICATION_JSON).entity(json).put(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return t;
		} else {
			System.err.println(response);
		}
		return null;
		
	}

	public static Team create(Team t) {
		
		String url = BASE_URL;

		Gson gson = new Gson();
		String json = gson.toJson(t);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken).entity(json)
				.type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		URI u = response.getLocation();
		String uri = u.toString();
		String id = uri.substring(uri.lastIndexOf('/') + 1);
		t.setId(Integer.parseInt(id));
		return t;
		
	}

	
}
