package no.magott.yammer.peep.sweep.batch;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PeepSweepBatchIntegrationTest extends JobLauncherTestUtils{

	@Test
	public void runBatch() throws Exception{
		JobExecution jobExecution = launchJob();
		assertThat(jobExecution.getExitStatus().getExitCode(), equalTo(ExitStatus.COMPLETED.getExitCode()));
	}
	
	@Test
	@Ignore
	public void runPostingStep(){
		JobExecution jobExecution = launchStep("postListToYammer");
		assertThat(jobExecution.getExitStatus().getExitCode(), equalTo(ExitStatus.COMPLETED.getExitCode()));
		
	}
	
}
