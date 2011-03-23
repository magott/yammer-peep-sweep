package no.magott.yammer.peep.sweep.employment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import no.magott.yammer.peep.sweep.ssl.SSLUtilities;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * This service uses the {@link #lookupUrl} adding the username. Interprets http 404 as invalid employment
 * http 200 is valid employment.
 * 
 * Examples of lookupUrl:
 * <ul>
 * <li>https://www.mycompany/employes/{username}</li>
 * <li>https://www.mycompany/employes/{user}/profile</li>
 * </ul>
 * where the placeholder will be replaced by the username in the {@link #isEmployed(String)} method
 * 
 * @author morten.andersen-gott
 *
 */
public class HttpEmploymentService implements EmploymentService, InitializingBean {

	private String lookupUrl;
	private RestTemplate restTemplate;
	private String username;
	private String password;
	
	
	
	@Override
	public boolean isEmployed(String username) {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(lookupUrl, String.class, username);
		HttpStatus httpStatus = responseEntity.getStatusCode();
		if(HttpStatus.OK == httpStatus){
			return true;
		}else if(HttpStatus.NOT_FOUND == httpStatus){
			return false;
		}else{
			throw new RuntimeException("Unexpected HttpStatus "+httpStatus+ "Http Response headers: "+responseEntity.getHeaders());
		}
	}

	
	public void setLookupUrl(String base) {
		this.lookupUrl = base;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
//		HttpClient client = setUpNTLMAuth();
		HttpClient client = setUpBasicAuth();
		CommonsClientHttpRequestFactory commons = new CommonsClientHttpRequestFactory(client);
		restTemplate = new RestTemplate(commons);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
			@Override
			public void handleError(ClientHttpResponse response){
				//No-op, want to handle error codes on client side	
			}
		});
	}
	
	private HttpClient setUpNTLMAuth(){
		HttpClient client = new HttpClient();
//		Protocol.registerProtocol("https", 
//				new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
		
		client.getState().setCredentials(
		new AuthScope(null, -1, AuthScope.ANY_REALM),
		new NTCredentials(username, password,"peep-sweep","DIR")
		);

        List<String> authPrefs = new ArrayList<String>();
        authPrefs.add(AuthPolicy.NTLM);
		client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
		return client;
	}


	private HttpClient setUpBasicAuth() {
		HttpClient client = new HttpClient();
		UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username,password);
		client.getState().setCredentials(new AuthScope(null, -1, AuthScope.ANY_REALM), credentials);
		return client;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
