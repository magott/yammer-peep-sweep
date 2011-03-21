package no.magott.yammer.peep.sweep.domain;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class representing a Yammer message when retrieving data from Yammer.
 * @author morten.andersen-gott
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class YammerMessage {

	public YammerMessage(){}
	
	@JsonProperty
	private String id;
	
	@JsonProperty
	private YamBody body;
	
	@JsonProperty
	private String repliedToId;

	public String getId() {
		return id;
	}
	
	public YamBody getBody() {
		return body;
	}
	
	public String getRepliedToId() {
		return repliedToId;
	}
	
	public String toString(){
		//TODO: Replace this with a less arbitrary toString once design is settled
		return ToStringBuilder.reflectionToString(this);
	}
	
	
	public static class Builder{
		private String body;
		private String id;

		public Builder(){}
		
		public Builder body(String body){
			this.body=body;
			return this;
		}
		
		public Builder id(String id){
			this.id=id;
			return this;
		}
		
		public YammerMessage build(){
			YammerMessage yam = new YammerMessage();
			yam.id=id;
			yam.body=new YammerMessage.YamBody();
			yam.body.plain=body;
			return yam;
		}
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class YamBody {

		@JsonProperty
		public String plain;
		
		public String getPlain() {
			return plain;
		}
		
		@Override
		public String toString() {
			return plain;
		}	
	}
}
