package no.magott.yammer.peep.sweep.batch.processor;

import no.magott.yammer.peep.sweep.domain.SuspensionCandidate;
import no.magott.yammer.peep.sweep.domain.User;
import no.magott.yammer.peep.sweep.employment.EmploymentService;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Required;

public class EmploymentStatusProcessor implements ItemProcessor<User, SuspensionCandidate> {

	private EmploymentService employmentService;
	
	@Override
	public SuspensionCandidate process(User user) {
		String employeeUsername = extractEmployeeUsername(user);
		if(employmentService.isEmployed(employeeUsername)){
			return null; //If employed, filter out
		}
		return newSuspensionCandidate(user, employeeUsername);
	}
	
	SuspensionCandidate newSuspensionCandidate(User user, String username) {
		SuspensionCandidate suspensionCandidate = new SuspensionCandidate();
		suspensionCandidate.setEmployeeUserId(username);
		suspensionCandidate.setSuspensionUrl(user.getSuspendUrl());
		suspensionCandidate.setYammerUserName("@"+user.getName());
		return suspensionCandidate;
	}

	String extractEmployeeUsername(User user) {
		String eMail = user.getContact().getEmailAdresses().get(0).getAddress();
		int atPosition = eMail.indexOf('@');
		return eMail.substring(0, atPosition);
	}

	@Required
	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

}
