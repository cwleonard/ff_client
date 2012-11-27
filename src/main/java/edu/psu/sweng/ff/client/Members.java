package edu.psu.sweng.ff.client;

import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import edu.psu.sweng.ff.common.Member;

public class Members {

	private final static String TOKEN_HEADER = "X-UserToken";
	
	private final static String BASE_URL = "https://amphibian.com/ff_server/resource/member/";

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
	
	public static String authenticate(String username, String password) {
		
		String url = BASE_URL + "authenticate";
		
		MultivaluedMap f = new MultivaluedMapImpl();
		f.add("username", username);
		f.add("password", password);
		WebResource r = c.resource(url);
		ClientResponse response = r.type(MediaType.APPLICATION_FORM_URLENCODED).entity(f).post(ClientResponse.class);

		String token = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			MultivaluedMap headers = response.getHeaders();
			token = (String) headers.getFirst(TOKEN_HEADER);
			userToken = token;
		} else {
			System.err.println(response);
		}
		
		return token;
		
	}
	
	public static void setUserToken(String t) {
		userToken = t;
	}

	public static Member getTokenOwner() {
		
		String url = BASE_URL;
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		Member m = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			m = gson.fromJson(json, Member.class);
		} else {
			System.err.println(response);
		}
		
		return m;
	
	}

	public static Member getByUserName(String un) {
	
		String url = BASE_URL + un;
	
		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		Member m = null;
		if (response.getStatus() == Status.OK.getStatusCode()) {
			String json = response.getEntity(String.class);
			Gson gson = new Gson();
			m = gson.fromJson(json, Member.class);
		} else {
			System.err.println(response);
		}
		
		return m;
	
	}

	public static Member update(Member m) {
		
		String url = BASE_URL + m.getUserName();

		Gson gson = new Gson();
		String json = gson.toJson(m);

		WebResource r = c.resource(url);
		ClientResponse response = r.header(TOKEN_HEADER, userToken)
				.type(MediaType.APPLICATION_JSON).entity(json).put(ClientResponse.class);
		if (response.getStatus() == Status.OK.getStatusCode()) {
			return m;
		} else {
			System.err.println(response);
		}
		return null;
		
	}

	public static Member create(Member m) {
		
		String url = BASE_URL;
		
		Gson gson = new Gson();
		String json = gson.toJson(m);
		
		WebResource r = c.resource(url);
		ClientResponse response = r.entity(json)
				.type(MediaType.APPLICATION_JSON).post(ClientResponse.class);
		URI u = response.getLocation();
		String uri = u.toString();
		String id = uri.substring(uri.lastIndexOf('/') + 1);

		MultivaluedMap headers = response.getHeaders();
		String token = (String) headers.getFirst(TOKEN_HEADER);
		userToken = token;

		m = getByUserName(m.getUserName());
		m.setAccessToken(token);

		return m;
		
	}

	
}
