package no.magott.yammer.peep.sweep.client;

import java.util.List;

import no.magott.yammer.peep.sweep.domain.User;
import no.magott.yammer.peep.sweep.domain.YammerMessage;

public interface YammerOperations {

	List<User> listAllUsers(int page);
	
	YammerMessage postNewPlainMessage(String message);
	
	YammerMessage postPlainReply(String message, String replyToMessageId);

	List<YammerMessage> listMessages();
}
