package no.magott.yammer.peep.sweep.batch.reader.writer;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class YammerPostingItemWriterTest {

	@Autowired
	private YammerPostingItemWriter yammerPostingItemWriter;
	
	@Test
	public void headerTextHasLinebreaks(){
		String headerMessage = yammerPostingItemWriter.createHeaderMessage();
		assertThat(headerMessage, notNullValue());
		assertThat(headerMessage, containsString("\n"));
	}
	
}
