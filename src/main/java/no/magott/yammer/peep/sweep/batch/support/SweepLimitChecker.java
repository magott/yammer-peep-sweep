package no.magott.yammer.peep.sweep.batch.support;

import java.util.concurrent.atomic.AtomicInteger;

public class SweepLimitChecker {

	private final int sweepLimit;
	private final AtomicInteger sweepCount;
	
	
	public SweepLimitChecker(int sweepLimit){
		this.sweepLimit = sweepLimit;
		sweepCount = new AtomicInteger();
	}
	
	public void incrementSweepCount(){
		sweepCount.incrementAndGet();
	}
	
	public boolean isLimitReached(){
		return sweepLimit==0?false:sweepCount.get() >= sweepLimit;
	}
	
	
	
}
