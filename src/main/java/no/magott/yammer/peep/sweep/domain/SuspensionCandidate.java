package no.magott.yammer.peep.sweep.domain;

public class SuspensionCandidate {

	private String yammerUserName;
	private String employeeUserId;
	private String suspensionUrl;
	
	public void setYammerUserName(String yammerUserName) {
		this.yammerUserName = yammerUserName;
	}
	public String getYammerUserName() {
		return yammerUserName;
	}
	public void setEmployeeUserId(String employeeUserId) {
		this.employeeUserId = employeeUserId;
	}
	public String getEmployeeUserId() {
		return employeeUserId;
	}
	public void setSuspensionUrl(String suspensionUrl) {
		this.suspensionUrl = suspensionUrl;
	}
	public String getSuspensionUrl() {
		return suspensionUrl;
	}
	
	
	
}
