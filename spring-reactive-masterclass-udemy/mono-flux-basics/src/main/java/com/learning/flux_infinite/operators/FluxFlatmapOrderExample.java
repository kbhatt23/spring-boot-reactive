package com.learning.flux_infinite.operators;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.ThreadUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
//we get the order ids form batch and then find its respective commeritems
//each order has one to many relatonship making it flux of flux
public class FluxFlatmapOrderExample {
	
	private static final String ORDER_PREFIX="order-";
	
	private static final String ITEM_PREFIX="item-";

	public static void main(String[] args) {
		findOrders()
		  //.flatMap(FluxFlatmapOrderExample :: findCommerceItems)
		.concatMap(FluxFlatmapOrderExample :: findCommerceItems)
		  .subscribe(new DefaultSubscriber<>(true, "FluxFlatmapOrderExample"));
		
		ThreadUtils.sleep(20000);
	}
	
	//assume we are getting order ids from d.b batch data
	//take the order ids one by one
	public static Flux<OrderItem> findOrders(){
		//ORDER_PREFIX +i
		return Flux.interval(Duration.ofSeconds(1))
		    .take(5) // 10 orders ids
		    .map(i -> new OrderItem(ORDER_PREFIX +i));
	}
	
	//for each order call the fulfillment system
	//but we make it async
	private static Flux<CommerceItem> findCommerceItems(OrderItem order){
		
		 Flux<CommerceItem> data = Flux.create(sink -> {
			String orderID = order.getOrderID();
			int id = Integer.valueOf(orderID.split("-")[1]);
			
			for(int i = 0 ; i < id ; i++) {
				if(sink.isCancelled()) 
					break;
				CommerceItem commerceItem = new CommerceItem(ITEM_PREFIX + id+ "-" + (i+1));
				sink.next(commerceItem);
			}
			sink.complete();
		});
		
		 return data.delayElements(Duration.ofSeconds(1));
	}
}

@Data
@RequiredArgsConstructor
class OrderItem{
	private final String orderID;
	private List<CommerceItem> items;
	
}
@Data
@AllArgsConstructor
class CommerceItem{
	private String itemID;
}