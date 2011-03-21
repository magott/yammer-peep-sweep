package no.magott.yammer.peep.sweep.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class representing a Yammer message when retrieving messages. Immutable, use {@link Builder} to set properties
 * @author morten.andersen-gott
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class YammerPost {

	public YammerPost(){}
	
	@JsonProperty
	private String id;
	
	@JsonProperty
	private String body;
	
	
	@SuppressWarnings("unused")
	@JsonProperty("replied_to_id")
	private String repliedToId;

	public String getId() {
		return id;
	}
	
	public String getBody() {
		return body;
	}
	
	public String toString(){
		//TODO: Replace this with a less arbitrary toString once design is settled
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	public static class Builder{
		private String body;
		private String id;
		private String repliedToId;

		public Builder(){}
		
		public Builder body(String body){
			this.body=body;
			return this;
		}
		
		public Builder id(String id){
			this.id=id;
			return this;
		}
		
		public Builder repliedTo(String id){
			this.repliedToId = id;
			return this;
		}
		
		public YammerPost build(){
			YammerPost yam = new YammerPost();
			yam.id=id;
			yam.body=body;
			yam.repliedToId=repliedToId;
			return yam;
		}
	}
	
}
