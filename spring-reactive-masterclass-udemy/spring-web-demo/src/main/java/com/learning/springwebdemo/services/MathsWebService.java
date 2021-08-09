package com.learning.springwebdemo.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.learning.springwebdemo.dtos.MathResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MathsWebService {

	public MathResponseDTO squareRoot(int number) {
		return new MathResponseDTO( Math.sqrt(number));
	}
	
	public List<MathResponseDTO> numberTable(int number) {
		
		return IntStream.rangeClosed(1, 10)
						.peek(i -> ThreadUtils.sleep(1000))
						.peek(i -> log.info("numberTable: Generating table for number "+number))// peek is a consumer and terminal oeprator
						.map(i -> i *number)
						.mapToObj(MathResponseDTO :: new)
						.collect(Collectors.toList());
	}
}
