package com.learning.mono_flux_advanced.sinks.challenges;

import lombok.Data;

@Data
public class ChannelMessage {

	private String message;
	
	private String sender;

	public ChannelMessage(String message, String sender) {
		super();
		this.message = message;
		this.sender = sender;
	}
	
	
	
}
