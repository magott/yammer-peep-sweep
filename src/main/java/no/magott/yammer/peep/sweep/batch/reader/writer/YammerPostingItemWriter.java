package no.magott.yammer.peep.sweep.batch.reader.writer;

import java.util.List;

import no.magott.yammer.peep.sweep.client.YammerOperations;
import no.magott.yammer.peep.sweep.domain.SuspensionCandidate;
import no.magott.yammer.peep.sweep.domain.YammerMessage;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemWriter;

/**
 * Does not support restart. To support restart implement {@link ItemStream} and
 * store {@link #replyToId} in {@link ExecutionContext}
 * 
 * @author morten.andersen-gott
 * 
 */
public class YammerPostingItemWriter implements ItemWriter<SuspensionCandidate> {

	private YammerOperations yammerOperations;
	private String headerMessage;

	private String replyToId;

	@Override
	public void write(List<? extends SuspensionCandidate> items) throws Exception {
		for (SuspensionCandidate candidate : items) {
			postHeaderMessageIfNecessary();
			yammerOperations.postPlainReply(createMessage(candidate), replyToId);
		}

	}

	private void postHeaderMessageIfNecessary() {
		if(replyToId==null){
			YammerMessage yammerMessage = yammerOperations.postNewPlainMessage(createHeaderMessage());
			replyToId = yammerMessage.getId();
		}
	}

	String createHeaderMessage() {
		return headerMessage;
	}

	private String createMessage(SuspensionCandidate candidate) {
		return candidate.getYammerUserName() + " suspend by clicking the following url: "+candidate.getSuspensionUrl();
	}
	
	public void setHeaderMessage(String headerMessage) {
		this.headerMessage = headerMessage;
	}


}
