package com.tribel.NettyHttpServer.response;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class ResponseFactory {

	private static final String HELLO_RESPONSE = "hello";
	private static final String STATISTIC_RESPONSE = "status";
	private static final String REDIRECT_RESPONSE = "redirect";

	private QueryStringDecoder queryStringDecoder;
	private HttpRequest httpRequest;
	private String action;
	private String requestUri;

	public ResponseFactory(HttpRequest request) {
		this.httpRequest = request;
		this.requestUri = request.getUri();
		this.queryStringDecoder = new QueryStringDecoder(requestUri);
		this.action = requestParse();
	}

	private String requestParse() {
		String[] segments = queryStringDecoder.path().split("/");
		if (segments.length > 0) {
			return segments[segments.length - 1];
		}

		return "";
	}

	private AbstractResponse determineResponse(String row) {
		switch (row) {
			case HELLO_RESPONSE:
				return new HelloWorldResponse(httpRequest);
			case STATISTIC_RESPONSE:
				 return new StatusResponse(httpRequest);
			case REDIRECT_RESPONSE:
				return new RedirectResponse(httpRequest);

		}
		return null;//new DefaultResponse(httpRequest);
	}
	
	public AbstractResponse geResponset() {
		return determineResponse(action);
	}
}
