package no.magott.yammer.peep.sweep.whitelist;

import java.util.List;

public class SimpleWhiteListService implements WhiteListService{

	private List<String> whiteListedUsers;
	
	public boolean isWhiteListed(String username) {
		return whiteListedUsers.contains(username);
	}

	public void setWhiteListedUsers(List<String> whiteListedUsers) {
		this.whiteListedUsers = whiteListedUsers;
	} 
	
}
