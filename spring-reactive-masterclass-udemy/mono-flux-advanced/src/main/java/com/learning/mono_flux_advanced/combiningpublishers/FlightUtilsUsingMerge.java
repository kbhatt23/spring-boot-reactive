package com.learning.mono_flux_advanced.combiningpublishers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

import com.learning.mono_flux_advanced.utils.CountDownSubscriber;
import com.learning.mono_flux_advanced.utils.DefaultSubscriber;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

// we have different publisher for different vendor prices
//as soon as we recieve first cheap fligh we book it and reject others
public class FlightUtilsUsingMerge {
	

	private static final String VENDOR_QATAR = "Qatar";
	
	private static final String VENDOR_INDIAN_AIRLINE= "Indian-Airline";
	
	private static final String VENDOR_EMIRATES = "Emirates";

	public static void main(String[] args) {
		
		CountDownLatch latch = new CountDownLatch(1);
		//whatever comes first
		//can not use concatmap or starts with as those are ordered and will delay the process
		Flux<FlightInstance> merge = Flux.merge(qatarTickets(),indianAirways(),emiratesTickets());
		
		merge
		   .doOnNext(flightInstance -> System.out.println("Instance to check "+flightInstance))
		   .timeout(Duration.ofSeconds(10))//gives error once time out
		   .filter(flightInstance -> flightInstance.getPrice() < 60)//we book the ticket only if it is less than 40
		   .take(1) // take only one of such -> take completes the subscriber and calls cancel for emitter(publisher)
		   .subscribe(new CountDownSubscriber<>(true, "FlightUtilsUsingMerge",latch));
		   
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("main dies");
	}
	
	//we have 3 publisher
	//each one of them are searching over internet for different vendors
	//each flux is taking data related to flighcost based on different vendors
	//if we do one by one than concatmap is not good here
	//we need as soon as whatever flight data comes we chcek for filter if subscriber get its data

	
	private static Flux<FlightInstance> qatarTickets(){
		return Flux.interval(Duration.ofSeconds(1))
				  .map(n -> new FlightInstance(VENDOR_QATAR, MonoStreamsUtils.FAKER.random().nextInt(50, 150)));
	}
	
	private static Flux<FlightInstance> indianAirways(){
		return Flux.interval(Duration.ofSeconds(1))
				  .map(n -> new FlightInstance(VENDOR_INDIAN_AIRLINE, MonoStreamsUtils.FAKER.random().nextInt(50, 150)));
	}
	
	private static Flux<FlightInstance> emiratesTickets(){
		return Flux.interval(Duration.ofSeconds(1))
				  .map(n -> new FlightInstance(VENDOR_EMIRATES, MonoStreamsUtils.FAKER.random().nextInt(50, 150)));
	}
}




@Data
@AllArgsConstructor
class FlightInstance{
	
	private String vendor;
	
	private int price;
	
}
