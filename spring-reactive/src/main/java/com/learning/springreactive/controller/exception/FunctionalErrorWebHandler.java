package com.learning.springreactive.controller.exception;

import java.util.Date;
import java.util.Map;

import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;
@Component
public class FunctionalErrorWebHandler extends AbstractErrorWebExceptionHandler{

	public FunctionalErrorWebHandler(ErrorAttributes errorAttributes, ServerCodecConfigurer serverCodecConfigurer,
			ApplicationContext applicationContext) {
		super(errorAttributes, new ResourceProperties(), applicationContext);
		super.setMessageWriters(serverCodecConfigurer.getWriters());
		super.setMessageReaders(serverCodecConfigurer.getReaders());
	}

	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::handleErrorRespnse);
	}

	private Mono<ServerResponse> handleErrorRespnse(ServerRequest serverRequest){
		Map<String, Object>  errorAttributeMap= getErrorAttributes(serverRequest, false);
		System.out.println("handleErrorRespnse() error attribute map: "+errorAttributeMap);
		CustomExceptionResponse res = new CustomExceptionResponse(new Date(),
				(String)errorAttributeMap.get("message"), HttpStatus.INTERNAL_SERVER_ERROR.toString());
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.contentType(MediaType.APPLICATION_STREAM_JSON)
						.body(BodyInserters.fromObject(res));
	}
}
