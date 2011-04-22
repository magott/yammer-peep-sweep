package no.magott.yammer.peep.sweep.listener;

import no.magott.yammer.peep.sweep.batch.support.SweepLimitChecker;
import no.magott.yammer.peep.sweep.domain.SuspensionCandidate;
import no.magott.yammer.peep.sweep.domain.User;

import org.springframework.batch.core.ItemProcessListener;

public class SweepIncrementingItemProcessorListner implements ItemProcessListener<User, SuspensionCandidate>{

	SweepLimitChecker sweepLimitChecker;
	
	@Override
	public void beforeProcess(User item) {
		
	}

	@Override
	public void afterProcess(User item, SuspensionCandidate result) {
		if(result!=null){
			sweepLimitChecker.incrementSweepCount();
		}
	}

	@Override
	public void onProcessError(User item, Exception e) {
		
	}

	public void setSweepLimitChecker(SweepLimitChecker sweepLimitChecker) {
		this.sweepLimitChecker = sweepLimitChecker;
	}
	
}
