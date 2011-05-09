package no.magott.yammer.peep.sweep.batch.writer;

import java.util.List;

import no.magott.yammer.peep.sweep.client.YammerOperations;
import no.magott.yammer.peep.sweep.domain.SuspensionCandidate;
import no.magott.yammer.peep.sweep.domain.YammerMessage;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Required;

/**
 * Does not support restart. To support restart implement {@link ItemStream} and
 * store {@link #replyToId} in {@link ExecutionContext}
 * 
 * @author morten.andersen-gott
 * 
 */
public class YammerPostingItemWriter implements ItemWriter<SuspensionCandidate> {

	private Logger log = Logger.getLogger(getClass());
	private YammerOperations yammerOperations;
	private String headerMessage;
	private int backOffInSeconds = 30;

	private String replyToId;

	@Override
	public void write(List<? extends SuspensionCandidate> items) throws Exception {
		for (SuspensionCandidate candidate : items) {
			postHeaderMessageIfNecessary();
			postMessage(candidate);
			Thread.sleep(backOffInSeconds*1000);
		}

	}

	private void postMessage(SuspensionCandidate candidate) {
		log.debug("Posting suspension message for "+candidate.getYammerUserName());
		yammerOperations.postPlainReply(createMessage(candidate), replyToId);
	}

	private void postHeaderMessageIfNecessary() {
		if(replyToId==null){
			log.debug("Posting header message");
			YammerMessage yammerMessage = yammerOperations.postNewPlainMessage(createHeaderMessage());
			replyToId = yammerMessage.getId();
		}
	}

	String createHeaderMessage() {
		return headerMessage;
	}

	@Required
	public void setYammerOperations(YammerOperations yammerOperations) {
		this.yammerOperations = yammerOperations;
	}
	
	private String createMessage(SuspensionCandidate candidate) {
		return candidate.getYammerUserName() + " suspend by clicking the following url: "+candidate.getSuspensionUrl();
	}
	
	public void setHeaderMessage(String headerMessage) {
		this.headerMessage = headerMessage;
	}


}
