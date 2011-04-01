package no.magott.yammer.peep.sweep.employment;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import no.magott.yammer.peep.sweep.whitelist.WhiteListService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.params.AuthPNames;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.util.UriTemplate;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * This service uses the {@link #lookupUrl} adding the username. Interprets http 500 as invalid employment.
 * In the case of http 200 the returned xml content is examined to determine if the employee has status as
 * an active or inactive employee 
 * 
 * Examples of values for {@link #setLookupUrl(String)} :
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

	private static final String INACTIVE_STATUS_CODE = "0";
	
	private static Logger log = Logger.getLogger(HttpEmploymentService.class);
	
	private AuthScope authScope =  new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthScope.ANY_SCHEME);
	private List<String> authenticationPreferences;
	private Jaxp13XPathTemplate xPathTemplate = new Jaxp13XPathTemplate();
	private HttpContext localContext = new BasicHttpContext();

	private WhiteListService whiteListService = new NoWhiteListService();

	private UriTemplate uriTemplate;
	private Credentials credentials;
	private DefaultHttpClient httpclient;
	
	@Override
	public boolean isEmployed(String username) {
		if(whiteListService.isWhiteListed(username)){
			return true;
		}
		
		HttpResponse response = callServerWithUser(username);
		
		boolean employed = isEmployed(response);
		if(employed){
			log.debug(username + " is employed");
		}else{
			log.info("BUSTED! " +username + " is no longer employed");
		}
		return employed;
	}


	private boolean isEmployed(HttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
		if(HttpStatus.OK == httpStatus){
			return isActiveEmployee(response);
		}else if(HttpStatus.INTERNAL_SERVER_ERROR == httpStatus){
			consumeResponse(response);
			return false;
		}else{
			throw new EmploymentServiceException("Unexpected HttpStatus "+httpStatus+ " Http Response headers: ");//+responseEntity.getHeaders());
		}
	}

	/**
	 * Method only exists to wrap checked exceptions as unchecked
	 * @param localContext
	 * @param httpget
	 * @return
	 */
	private HttpResponse callServerWithUser(String username) {
		try {
			HttpGet httpget = new HttpGet(uriTemplate.expand(username));
			return httpclient.execute(httpget, localContext);
		} catch (ClientProtocolException e) {
			throw new EmploymentServiceException("Unexpected exception while calling http method",e);
		} catch (IOException e) {
			throw new EmploymentServiceException("Unexpected exception while calling http method",e);
		}
	}

	/**
	 * Need to consume the response to be able to reuse the httpclient
	 * @param response
	 */
	private void consumeResponse(HttpResponse response){
		try {
			EntityUtils.consume(response.getEntity());
		} catch (IOException e) {
			log.warn("Exception occured while consuming response", e);
		}
	}

	private boolean isActiveEmployee(HttpResponse response) {
		try {
			String xml = EntityUtils.toString(response.getEntity());
			Source source = new DOMSource(createDom(xml));
			String asString = xPathTemplate.evaluateAsString("/ArrayOfPropertyData/PropertyData[Name='EmploymentStatusCd']/Value",source);
			return !asString.equals(INACTIVE_STATUS_CODE);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	public Document createDom(String string) throws Exception{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource( new StringReader( string ) );
	    Document d = builder.parse( is );
	    return d;
	}


	public void setLookupUrl(String base) {
		Assert.notNull(base, "LookupUrl cannot be null");
		this.uriTemplate = new UriTemplate(base);
	}
	
	public void setWhiteListService(WhiteListService whiteListService) {
		this.whiteListService = whiteListService;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(credentials, "Credentials cannot be null");
		httpclient = new DefaultHttpClient();
		if(credentials!=null){
			httpclient.getCredentialsProvider().setCredentials(authScope, credentials);
		}
		if(!CollectionUtils.isEmpty(authenticationPreferences)){
			httpclient.getParams().setParameter(AuthPNames.TARGET_AUTH_PREF, authenticationPreferences);			
		}
	}
	
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public void setAuthenticationPreferences(List<String> authenticationPreferences) {
		this.authenticationPreferences = authenticationPreferences;
	}
	
	class NoWhiteListService implements WhiteListService{
		@Override
		public boolean isWhiteListed(String username) {
			return false;
		}
	}

}
