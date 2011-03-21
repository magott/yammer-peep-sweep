package no.magott.yammer.peep.sweep.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.magott.yammer.peep.sweep.domain.User;
import no.magott.yammer.peep.sweep.domain.YammerMessage;
import no.magott.yammer.peep.sweep.domain.YammerMessageHolder;
import no.magott.yammer.peep.sweep.domain.YammerPost;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.oauth1.ProtectedResourceClientFactory;
import org.springframework.web.client.RestTemplate;

public class YammerTemplate implements YammerOperations, InitializingBean {

	private final String YAMMER_BASE = "https://www.yammer.com/";
	private final String USERS = "api/v1/users.json?page={page}";
	private final String MESSAGES = "api/v1/messages.json";

	@Value("${yammer.consumer.key}")
	private String consumerKey;

	@Value("${yammer.consumer.token.secret}")
	private String consumerToken;

	@Value("${yammer.access.token}")
	private String accessToken;

	@Value("${yammer.access.token.secret}")
	private String accessTokenSecret;

	private RestTemplate restTemplate;

	public List<User> listAllUsers(int page) {
		Map<String, String> urlParameters = new HashMap<String, String>();
		urlParameters.put("page", Integer.toString(page));
		ResponseEntity<User[]> users = restTemplate.getForEntity(YAMMER_BASE + USERS, User[].class, urlParameters);
		return Arrays.asList(users.getBody());
	}

	public YammerMessage postNewPlainMessage(String message) {
		YammerMessageHolder yamsHolder = restTemplate.postForObject(YAMMER_BASE + MESSAGES, new YammerPost.Builder().body(message)
				.build(), YammerMessageHolder.class);
		return yamsHolder.getYammerMessages().get(0);
	}

	@Override
	public List<YammerMessage> listMessages() {
		ResponseEntity<YammerMessageHolder> yams = restTemplate.getForEntity(YAMMER_BASE + MESSAGES,
				YammerMessageHolder.class);
		return yams.getBody().getYammerMessages();
	}

	public YammerMessage postPlainReply(String message, String replyToMessageId) {
		YammerMessageHolder yam = restTemplate.postForObject(YAMMER_BASE + MESSAGES, new YammerPost.Builder().body(message)
				.repliedTo(replyToMessageId).build(), YammerMessageHolder.class);
		System.out.println(yam.getYammerMessages().get(0).getId());
		return yam.getYammerMessages().get(0);
	}

	public void afterPropertiesSet() throws Exception {
		restTemplate = ProtectedResourceClientFactory
				.create(consumerKey, consumerToken, accessToken, accessTokenSecret);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJacksonHttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);

	}

}
