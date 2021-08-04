package com.learning.mono_flux_advanced.batching;

import java.util.function.Predicate;

import com.learning.mono_flux_advanced.utils.MathUtils;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;
import com.learning.mono_flux_advanced.utils.ThreadUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

public class GroupByChallenge {
	private static final String ELECTRONIC = "electronic";

	private static final String KIDS = "kids";

	private static Predicate<OrderItem> kidsOrder = orderItem -> orderItem.getCategory().toLowerCase() . contains(KIDS);
	
	private static Predicate<OrderItem> electronicOrder = orderItem -> orderItem.getCategory().toLowerCase() . contains(ELECTRONIC);

	public static void main(String[] args) {
		
		orderStream()
		  .filter(kidsOrder.or(electronicOrder))
		  .groupBy(orderItem -> kidsOrder.test(orderItem) ? KIDS: ELECTRONIC )
		  .take(11)
		 .subscribe(GroupByChallenge :: manageDepartmentCAtegory
				 );
		  
		
		ThreadUtils.sleep(40000);
		
		System.out.println("main diest");
	}
	
	
	private static void manageDepartmentCAtegory(GroupedFlux<String, OrderItem> groupedFlux) {
		
		groupedFlux.doOnComplete(() -> System.out.println("manageDepartmentCAtegory : all task done"))
		.doOnNext(orderItem -> {
			double currentPrice = orderItem.getPrice();
			double newPrice =currentPrice;
			if(kidsOrder.test(orderItem)) {
				//discount of 10 percent
				newPrice = currentPrice - (10 * currentPrice/100);
			}
			else if(electronicOrder.test(orderItem)) {
				newPrice = currentPrice + (10 * currentPrice/100);
			}
			System.out.println("updating item price from "+currentPrice+" to "+newPrice);
			orderItem.setPrice(newPrice);
		})
		
		.subscribe(orderItem -> System.out.println("manageDepartmentCAtegory: order recieved by key "+groupedFlux.key()+" and value "+orderItem));
	}
	
	
	private static Flux<OrderItem> orderStream() {
		
		return Flux.generate(() -> 1 , (currentStore , synchronousSink) -> {
			ThreadUtils.sleep(10);
			synchronousSink.next(new OrderItem("order-"+currentStore,
						MathUtils.getRandomNumber(100, 200)
					, MonoStreamsUtils.FAKER.commerce().department()));
			
			return currentStore+1;
		});
	}
}

@Data
@AllArgsConstructor
class OrderItem{
	private  String orderID;
	
	private  double price;
	
	private  String category;
	
}

