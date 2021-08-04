package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.github.javafaker.Book;
import com.learning.mono_flux_advanced.utils.MathUtils;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

public class BatchingBooksOfCategories {
	private static final Set<String> GENRES_EXPECTED ;
	static {
		GENRES_EXPECTED = new HashSet<>(Arrays.asList("Fantasy", "Suspense/Thriller", "Science fiction"));
	}
	
	public static void main(String[] args) {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		getBookOrders()
			.filter(bookOrder -> GENRES_EXPECTED.contains(bookOrder.getBook().genre())) // find only wanted genres
			.bufferTimeout(5, Duration.ofSeconds(5))
			.map(bookOrders -> bookOrders.stream().mapToDouble(b -> b.getPrice()).sum()) //for each batch
			.map(RevenueBatchReport :: new)
			.subscribe(
					batchPrice -> System.out.println("bookorder revenue report "+batchPrice)
					, error -> System.out.println("error occurred")
					, () -> {latch.countDown(); System.out.println("all tasks completed");}
					);
		
			;
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("main dies");
	}

	// after every 100 ms it passes one book
//consider it like every 100 ms one order of book is placed	
	// assume books of different genres are placed by customer
	private static Flux<BookOrder> getBookOrders() {
		return Flux.interval(Duration.ofMillis(100))
				   .map(i -> MonoStreamsUtils.FAKER.book())
				   .map(book -> new BookOrder(book, MathUtils.getRandomNumber(200, 400)))
				   ;
	}

}

@Data
@AllArgsConstructor
class BookOrder{
	
	private Book book;
	
	private double price;
}

@Data
class RevenueBatchReport{
	private final LocalDateTime localDateTime;
	
	private double revenue;
	
	public RevenueBatchReport(double revenue) {
		this.revenue=revenue;
		this.localDateTime = LocalDateTime.now();
	}
}
