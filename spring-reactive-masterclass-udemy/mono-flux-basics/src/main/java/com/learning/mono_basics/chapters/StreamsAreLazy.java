package com.learning.mono_basics.chapters;

import java.util.stream.Stream;

import com.learning.mono_basics.utils.MathUtils;
import com.learning.mono_basics.utils.ThreadUtils;

public class StreamsAreLazy {

	public static void main(String[] args) {
		Stream<Integer> data = Stream.of(1,2,3,4);
		
		data.filter(MathUtils :: isEven)
			.map(i ->{
				ThreadUtils.sleep(2000);
				return i * 2;
			})
			.forEach(System.out::println);
			;
	}
}
