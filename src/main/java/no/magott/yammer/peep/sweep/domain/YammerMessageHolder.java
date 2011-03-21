package no.magott.yammer.peep.sweep.domain;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Yammer wraps its messages in a messages object instead of passing it as an
 * array, so unfortunately, this class needs to exist.
 * 
 * @author morten.andersen-gott
 * 
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class YammerMessageHolder {

	public YammerMessageHolder(){}
	
	@JsonProperty("messages")
	private YammerMessage[] yams;
	
	public List<YammerMessage> getYammerMessages() {
		return Arrays.asList( yams);
	}
}