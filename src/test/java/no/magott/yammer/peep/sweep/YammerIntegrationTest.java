package no.magott.yammer.peep.sweep;

import java.util.ArrayList;
import java.util.List;

import no.magott.yammer.peep.sweep.domain.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.oauth1.ProtectedResourceClientFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class YammerIntegrationTest {

	private final String YAMMER_BASE = "https://www.yammer.com/api/v1/users.json";

	private List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
	{
		messageConverters.add(new MappingJacksonHttpMessageConverter());
	}
	
	@Value("${yammer.consumer.key}")
	private String consumerKey;

	@Value("${yammer.consumer.token.secret}")
	private String consumerKeySecret;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${yammer.access.token}")
	private String accessToken;

	@Value("${yammer.access.token.secret}")
	private String accessTokenSecret;

	@Test
	public void canRetrieveUserDataFromYammerAndParseToJavaObjecs() {
		restTemplate = ProtectedResourceClientFactory.create(consumerKey, consumerKeySecret,
				accessToken, accessTokenSecret);
		restTemplate.setMessageConverters(messageConverters);
		ResponseEntity<User[]> response = restTemplate.getForEntity(
				YAMMER_BASE,User[].class);
		for (User user : response.getBody()) {
			System.out.println(user.toString());
		}

	}
	

}
