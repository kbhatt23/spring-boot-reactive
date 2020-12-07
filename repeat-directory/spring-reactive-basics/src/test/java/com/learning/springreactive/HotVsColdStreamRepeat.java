package com.learning.springreactive;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

public class HotVsColdStreamRepeat extends BaseTest{
	
	
	//if main method goes both hot and cold stream cna not commuicate with their subscriber
	@Test
	public void createBasicColdStream() {
		Flux<Integer> strFlux = Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
		strFlux.subscribe(BaseTest::findSuccessConsumer);
		//sine there is no sllep to wait main thread no communciation happens between pub and sub
		
		//simulating only to show pub
		BaseTest.sleep(2000);
	}
	//if main method goes both hot and cold stream cna not commuicate with their subscriber
	@Test
	public void createBasicHotStream() {
		Flux<Integer> strFlux = Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
		ConnectableFlux<Integer> connectableFlux = strFlux.publish();
		connectableFlux.connect();
		//sine there is no sllep to wait main thread no communciation happens between pub and sub
		
		connectableFlux.subscribe(BaseTest::findSuccessConsumer);
		//simulating only to show pub
		BaseTest.sleep(2000);
	}
	
	//in case of cold streams , pub starts sending data only when sub subscries to it
	//hence if another subscriber is attached after some time, it takesdata from beginnig
	
	@Test
	public void multiplesubscriberColdStream() {
		Flux<Integer> strFlux = Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
		//sine there is no sllep to wait main thread no communciation happens between pub and sub
		
		strFlux.subscribe(s -> System.out.println("subscriber1 found element "+s),BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer);
		//simulating only to show pub
		BaseTest.sleep(2000);
		
		strFlux.subscribe(s -> System.out.println("subscriber2 found element "+s));
		//simulating only to show pub
		BaseTest.sleep(3000);
	}
	
	//a hot strea starts sending data as soon as it is created
	//irrespective of subscriber attached
	//hence if there is aleep and subscriber fetched it late earlier sent data will be lost
	@Test
	public void singleSsubscriberHotStream() {
		Flux<Integer> strFlux = Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
		ConnectableFlux<Integer> connectableFlux = strFlux.publish();
		connectableFlux.connect();
		BaseTest.sleep(2000);
		
		//since subscriber came late and it is hot strema , datra will be lost
		connectableFlux.subscribe(s -> System.out.println("subscriber1 found element "+s),BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer);
		//simulating only to show pub
		BaseTest.sleep(2000);
		
	}
	
	
	@Test
	public void multiplesubscriberHotStream() {
		Flux<Integer> strFlux = Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
		//sine there is no sllep to wait main thread no communciation happens between pub and sub
		ConnectableFlux<Integer> connectableFlux = strFlux.publish();
		connectableFlux.connect();
		
		connectableFlux.subscribe(s -> System.out.println("subscriber1 found element "+s),BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer);
		//simulating only to show pub
		BaseTest.sleep(2000);
		
		connectableFlux.subscribe(s -> System.out.println("subscriber2 found element "+s));
		//simulating only to show pub
		BaseTest.sleep(3000);
	}
}
