package no.magott.yammer.peep.sweep.employment;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HttpEmploymentServiceIntegrationTest {

	@Autowired
	private EmploymentService employmentService;
	
	@Test
	public void returnsFalseForInvalidEmployee(){
		assertThat(employmentService, notNullValue());
		assertThat(employmentService.isEmployed("foo.bar"), not(true));
	}
	@Test
	public void returnsFalseForInactiveEmployee(){
		assertThat(employmentService, notNullValue());
		assertThat(employmentService.isEmployed("david.laas"), is(false));
	}
	@Test
	public void returnsTrueForActiveEmployee(){
		assertThat(employmentService, notNullValue());
		assertThat(employmentService.isEmployed("morten.andersen-gott"), not(false));
	}
	
}
