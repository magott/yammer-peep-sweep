package no.magott.yammer.peep.sweep;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import no.magott.yammer.peep.sweep.client.YammerOperations;
import no.magott.yammer.peep.sweep.domain.User;
import no.magott.yammer.peep.sweep.domain.YammerMessage;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class YammerTemplateIntegrationTest {

	@Autowired
	private YammerOperations yammerTemplate;
	
	private static String replyId;

	@Test
	public void canListMessages(){
		List<YammerMessage> yams = yammerTemplate.listMessages();
		System.out.println(yams);
	}
	
	@Test
	public void canListUsers(){
		List<User> users = yammerTemplate.listAllUsers(2);
		assertThat(users.size(), equalTo(50));
		for (User user : users) {
			System.out.println(user);
		}
	}
	
	@Test
	@Ignore("This will post to yammer, use with care")
	public void canPostMessage() {
		YammerMessage message = yammerTemplate.postNewPlainMessage("@morten-andersen-gott is sorry about the spam, but it isn't like Yammer provides a dev environment");
		assertThat(message.getRepliedToId(), nullValue());
		assertThat(message.getId(), notNullValue());
		replyId = message.getId();
		System.out.println(replyId);
	}
	
	@Test
	@Ignore("This will post to yammer, use with care")
	public void canPostReply(){
		System.out.println("ReplyId: "+replyId);
		YammerMessage reply = yammerTemplate.postPlainReply("this is morten again, doing a reply", replyId);
		assertThat(reply.getRepliedToId(), notNullValue());
		assertThat(reply.getId(), notNullValue());
		assertThat(reply.getId(), not(equalTo(replyId)));
	}
	

}
