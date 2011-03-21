package no.magott.yammer.peep.sweep.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private static final String SUSPEND_URL = "https://www.yammer.com/accenture.com/users/%s/suspend_confirm";

	@JsonProperty
	private String id;
	
	@JsonProperty
	private String name;
	
	@JsonProperty
	private String url;
	
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getSuspendUrl(){
		return String.format(SUSPEND_URL, id);
	}
	
	public String toString(){
		return new ToStringBuilder(this).append("Username", name).append("suspendurl", getSuspendUrl()).toString();
	}
	
}
