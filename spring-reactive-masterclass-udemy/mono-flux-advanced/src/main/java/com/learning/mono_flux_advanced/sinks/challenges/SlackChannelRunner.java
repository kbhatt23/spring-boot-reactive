package com.learning.mono_flux_advanced.sinks.challenges;

import com.learning.mono_flux_advanced.utils.ThreadUtils;

public class SlackChannelRunner {

	public static void main(String[] args) {
		SlackChannel<ChannelMessage> channel = new SlackChannel<>("act-17_device_landing");
		
		
		SlackUser<ChannelMessage> kanishk = new SlackUser<>("kanishk");
		
		SlackUser<ChannelMessage> nitish = new SlackUser<>("nitish");
		
	
		
		SlackUser<ChannelMessage> anuja = new SlackUser<>("anuja");
		
		channel.subscribe(kanishk);
		channel.subscribe(nitish);
		channel.subscribe(anuja);
		
		ChannelMessage channelMessage1 = new ChannelMessage("jai shree ganes", "kanishk");

		channel.sendMessage(channelMessage1, kanishk);
		
		ThreadUtils.sleep(2000);
		
		
		SlackUser<ChannelMessage> alex = new SlackUser<>("alex");
		
		channel.subscribe(alex);
		ThreadUtils.sleep(1000);
		
		
		ChannelMessage channelMessage2 = new ChannelMessage("\"hail lord\"", "alex");
		channel.sendMessage(channelMessage2, alex);
		
		
		
	//	ThreadUtils.sleep(10000);
	}
}
