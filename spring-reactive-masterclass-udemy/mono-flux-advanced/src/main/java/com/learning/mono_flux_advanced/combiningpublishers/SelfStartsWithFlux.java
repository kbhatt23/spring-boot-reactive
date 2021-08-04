package com.learning.mono_flux_advanced.combiningpublishers;

import com.learning.mono_flux_advanced.utils.DefaultSubscriber;

import reactor.core.publisher.Flux;

// FluxA.startsWith(FluxB) -> data always comes fro flux b until it vanieshed or filter condition filters it
	// once all data is vanished and consumer did not recieve it comes from FluxA
//in a way it is reverse of Flux.concat
public class SelfStartsWithFlux {

	public static void main(String[] args) {
		Flux<Integer> fluxA = Flux.range(1, 5);
		
		Flux<Integer> fluxB = Flux.range(10, 5);
		
		//all data will come from fluxB
		//data from fluxA will only be called if consumer unable to get what it wants from fluxB
		
		fluxA.startWith(fluxB)
		//.take(1)
			 //.take(6) //fluxA wont even start generating untill consumer still needs it
			 //can act like cache
		     .subscribe(new DefaultSubscriber<>(true, "SelfStartsWithFlux"));
	}
}
