package no.magott.yammer.peep.sweep.domain;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.Arrays;
import java.util.List;

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
	
	@JsonProperty
	private Contact contact;

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getSuspendUrl() {
		return String.format(SUSPEND_URL, id);
	}
	
	public Contact getContact() {
		return contact;
	}
	
	public boolean equals(Object other){
		if (other instanceof User) {
			User otherUser = (User) other;
			return id==otherUser.id;
		}
		return false;
	}

	public String toString() {
		ToStringBuilder builder =  new ToStringBuilder(this).append("Username", name)
		.append("suspendurl", getSuspendUrl());
		for (Email email : getContact().getEmailAdresses()) {
			builder.append("Email",email.toString());
		}
		return builder.toString();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Contact {
		@JsonProperty("email_addresses")
		private Email[] emailAddresses;

		public List<Email> getEmailAdresses() {
			return Arrays.asList(emailAddresses);
		}
	}

	public static class Email {
		@JsonProperty
		private String type;
		@JsonProperty
		private String address;

		public String getAddress() {
			return address;
		}

		public String getType() {
			return type;
		}

		@Override
		public String toString() {
			return type + " : " + address;
		}
	}

	public static class Builder {
		private String name;
		private String id;
		private String email;

		public Builder id(String id) {
			this.id = id;
			return this;
		}
		
		public Builder name(String name){
			this.name=name;
			return this;
		}
		
		public Builder eMail(String email){
			this.email=email;
			return this;
		}

		public User build() {
			User user = new User();
			user.id = id;
			user.name = name;
			if(email!=null){
				user.contact= new Contact();
				Email eMail = new Email();
				eMail.address=email;
				eMail.type="primary";
				user.contact.emailAddresses=new Email[]{eMail};
			}
			return user;
		}
	}

}
