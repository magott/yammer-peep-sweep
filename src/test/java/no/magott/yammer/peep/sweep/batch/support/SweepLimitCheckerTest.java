package no.magott.yammer.peep.sweep.batch.support;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class SweepLimitCheckerTest {

	@Test
	public void neverReachesLimitWhenLimitSetToZero(){
		SweepLimitChecker limitChecker = new SweepLimitChecker(0);
		assertThat(limitChecker.isLimitReached(), is(false));
		limitChecker.incrementSweepCount();
		assertThat(limitChecker.isLimitReached(), is(false));
		limitChecker.incrementSweepCount();
		assertThat(limitChecker.isLimitReached(), is(false));
	}
	
	@Test
	public void limitIsReachedAfterTwoSweepsWhenLimitIsSetToTwo(){
		SweepLimitChecker limitChecker = new SweepLimitChecker(2);
		assertThat(limitChecker.isLimitReached(), is(false));
		limitChecker.incrementSweepCount();
		assertThat(limitChecker.isLimitReached(), is(false));
		limitChecker.incrementSweepCount();
		assertThat(limitChecker.isLimitReached(), is(true));
	}
	
}
