package no.magott.yammer.peep.sweep.batch.processor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import no.magott.yammer.peep.sweep.domain.SuspensionCandidate;
import no.magott.yammer.peep.sweep.domain.User;
import no.magott.yammer.peep.sweep.employment.EmploymentService;

public class EmploymentStatusProcessorTest {

	private User testUser = new User.Builder().eMail("foo.bar.foo-bar@foobar.com").name("foo.bar").build();
	private EmploymentStatusProcessor employmentStatusProcessor = new EmploymentStatusProcessor();
	
	
	@Test
	public void filtersOutCurrentEmployees(){
		employmentStatusProcessor.setEmploymentService(createAlwaysEmployeeMock());
		SuspensionCandidate candidate = employmentStatusProcessor.process(testUser);
		assertThat(candidate, is(nullValue()));
	}
	
	@Test
	public void createsSuspensionCandidateForFormerEmployees(){
		employmentStatusProcessor.setEmploymentService(createNeverEmployeeMock());
		SuspensionCandidate candidate = employmentStatusProcessor.process(testUser);
		assertThat(candidate, is(notNullValue()));
		assertThat(candidate.getSuspensionUrl(), is(notNullValue()));
		assertThat(candidate.getEmployeeUserId(), is(notNullValue()));
		assertThat(candidate.getYammerUserName(), is(notNullValue()));
	}	
	
	@Test
	public void canIsolateUsernameFromEmail(){
		String username = employmentStatusProcessor.extractEmployeeUsername(testUser);
		assertThat(username, equalTo("foo.bar.foo-bar"));
	}
	
	public EmploymentService createNeverEmployeeMock(){
		EmploymentService mock = mock(EmploymentService.class);
		when(mock.isEmployed(anyString())).thenReturn(Boolean.FALSE);
		return mock;
	}
	
	public EmploymentService createAlwaysEmployeeMock(){
		EmploymentService mock = mock(EmploymentService.class);
		when(mock.isEmployed(anyString())).thenReturn(Boolean.TRUE);
		return mock;
	}
	
	
	
}
