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

	private String createHeaderMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	private String createMessage(SuspensionCandidate candidate) {
		// TODO Auto-generated method stub
		return null;
	}


}
