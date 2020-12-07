package com.learning.springreactive;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

//by default streams are cold
//a. if main thread is gone subscriber wont proceed
//b. if there is no subscriber publisher do not emit data
//c. if there is a sleep in between then subscriber gets data from live instance
	//, meaning old data will be lost
public class HotVsColdStreams extends BaseTest{
	Flux<Integer> strFlux = Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1));
	
	@Test
	public void coldStream1() {
		System.out.println("main thread starting cold stream with no subscription");
		strFlux.log();
		//with no subscription no emmirrions from pub nor consuming of data from subscriber happenbs
		//lazy and cold
		BaseTest.sleep(1000);
	}
	
	@Test
	public void coldStream2() {
		//until main thread reamins live till then subscriber/publisher works , once that is done both communcation is gone
		strFlux.log()
		.subscribe(BaseTest::findSuccessConsumer,
				BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer
				);
		BaseTest.sleep(2000);
	}
	
	@Test
	public void coldStreamSingleConsumers() {
		BaseTest.sleep(2000);
		//since iut is cold flux, publisher do not send data until subsrciver starts
		//and hence no data will be lost
        strFlux.subscribe(s -> System.out.println("subscriber1 found "+s),
				BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer
				);
        BaseTest.sleep(3000);
		
	}
	
	
	@Test
	public void coldStreamMultipleConsumers() {
		//until main thread reamins live till then subscriber/publisher works , once that is done both communcation is gone
		strFlux.subscribe(s -> System.out.println("subscriber1 found "+s),
				BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer
				);
		//we will recive items only until main is avaialbel and hence wont recive complete event at all
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if we attah another consumer , since it si cold it startes form begining as until subscriber starts data is not sent
		strFlux.subscribe(s -> System.out.println("subscriber2 found "+s),
				BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer
				);
		BaseTest.sleep(4000);
	}
	
	//a hot publisher do not wait for subsrciber to subscribe and so if after some time subscriber picks it, it looses data sent till now
	@Test
	public void hotStreamMultipleConsumers() {
		//creating hot publisher
		ConnectableFlux<Integer> connectableFlux = strFlux.publish();
		connectableFlux.connect();
		
		
		//until main thread reamins live till then subscriber/publisher works , once that is done both communcation is gone
		connectableFlux.subscribe(s -> System.out.println("subscriber1 found "+s),
				BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer
				);
		//we will recive items only until main is avaialbel and hence wont recive complete event at all
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//if we attah another consumer , since it si cold it startes form begining as until subscriber starts data is not sent
		connectableFlux.subscribe(s -> System.out.println("subscriber2 found "+s),
				BaseTest::findErrorConsumer,BaseTest::findCompletedConsumer
				);
		BaseTest.sleep(4000);
	}
}
