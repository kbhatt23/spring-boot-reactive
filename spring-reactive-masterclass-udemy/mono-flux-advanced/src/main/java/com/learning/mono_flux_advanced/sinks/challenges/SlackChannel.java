package com.learning.mono_flux_advanced.sinks.challenges;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SlackChannel<T extends ChannelMessage> {

	// many message swill come
	//may subscriber will be there
	//new subscriber should see even old message -> cold 
	private Sinks.Many<T> sinkChannel;
	
	private Flux<T> channelFlux;
	
	private String channelName;
	
	public SlackChannel(String channelName) {
		this.channelName = channelName;
		
		//flux
		//multiple subscriber
		//cold publisher
		sinkChannel = Sinks.many().replay().all();
	}
	
	public void subscribe(SlackUser<T> user) {
		//lazyness
		if(this.channelFlux == null)
			this.channelFlux = sinkChannel.asFlux();
		this.channelFlux.subscribe(user);
		System.out.println("Adding user "+user.getUserName() + " to channel "+channelName);
	}
	
	public void sendMessage(T message , SlackUser<T> sender) {
		System.out.println("User "+sender.getUserName()+" sending message "+message);
		this.channelFlux = this.channelFlux.filter(internalMessage -> isSender(internalMessage, sender));
		sinkChannel.tryEmitNext(message);
	}
	
	private  boolean isSender(T internalMessage , SlackUser<T> sender) {
	boolean isSender= !internalMessage.getSender().equals(sender.getUserName());
	return isSender;
	}
}
