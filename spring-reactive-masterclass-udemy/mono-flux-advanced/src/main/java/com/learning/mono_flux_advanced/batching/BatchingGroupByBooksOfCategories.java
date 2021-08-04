package com.learning.mono_flux_advanced.batching;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.github.javafaker.Book;
import com.learning.mono_flux_advanced.utils.MathUtils;
import com.learning.mono_flux_advanced.utils.MonoStreamsUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;

public class BatchingGroupByBooksOfCategories {
	private static final Set<String> GENRES_EXPECTED ;
	static {
		GENRES_EXPECTED = new HashSet<>(Arrays.asList("Fantasy", "Suspense/Thriller", "Science fiction"));
	}
	
	public static void main(String[] args) {
		
		CountDownLatch latch = new CountDownLatch(1);
		
		getBookOrders()
			.filter(bookOrder -> GENRES_EXPECTED.contains(bookOrder.getGenre())) // find only wanted genres
			.bufferTimeout(5, Duration.ofSeconds(5))
			.map(BatchingGroupByBooksOfCategories :: groupedRevenue)
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
	private static Flux<BookOrderCustom> getBookOrders() {
		return Flux.interval(Duration.ofMillis(100))
				   .map(i -> new  BookOrderCustom())
				   ;
	}
	
	private static RevenueBatchReportGrouped groupedRevenue(List<BookOrderCustom> bookOrders) {
		
		return  bookOrders.stream()
				.collect(Collectors.collectingAndThen(Collectors.groupingBy(bookOrder -> bookOrder.getGenre(),
						Collectors.summingDouble(BookOrderCustom :: getPrice)
						), RevenueBatchReportGrouped :: new))
		;
		
	}

}




@Data
class RevenueBatchReportGrouped{
	private final LocalDateTime localDateTime;
	
	private Map<String, Double> categoryRevenue;
	
	public RevenueBatchReportGrouped(Map<String, Double> categoryRevenue) {
		localDateTime = LocalDateTime.now();
		this.categoryRevenue=categoryRevenue;
	}
	
}

@Data
class BookOrderCustom{
	
	private String title;
	
	private String genre;
	
	private double price;
	
	public BookOrderCustom() {
		Book book = MonoStreamsUtils.FAKER.book();
		this.title=book.title();
		this.genre = book.genre();
		this.price = MathUtils.getRandomNumber(200, 400);
	}
}
