package no.magott.yammer.peep.sweep.listener;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import no.magott.yammer.peep.sweep.batch.support.SweepLimitChecker;
import no.magott.yammer.peep.sweep.domain.SuspensionCandidate;

import org.junit.Test;

public class SweepIncrementingItemProcessorListenerTest{

	@Test
	public void incrementsSweepCountWhenCandidateIsReturnedFromProcessor(){
		SweepIncrementingItemProcessorListner listener = new SweepIncrementingItemProcessorListner();
		listener.setSweepLimitChecker(new SweepLimitChecker(1));
		listener.afterProcess(null, new SuspensionCandidate());
		assertThat(listener.sweepLimitChecker.isLimitReached(), is(true));
	}
	
	@Test
	public void doesNotIncrementSweepCountWhenCandidateReturnedIsNull(){
		SweepIncrementingItemProcessorListner listener = new SweepIncrementingItemProcessorListner();
		listener.setSweepLimitChecker(new SweepLimitChecker(1));
		listener.afterProcess(null, null);
		assertThat(listener.sweepLimitChecker.isLimitReached(), is(false));
	}
	
}
