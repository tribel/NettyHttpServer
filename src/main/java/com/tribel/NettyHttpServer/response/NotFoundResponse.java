package com.tribel.NettyHttpServer.response;


import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;


public class NotFoundResponse extends AbstractResponse{
	
	public static final String MESSAGE = "<center><h3>Page not found  404 !</h3></center>"; 
	
	public NotFoundResponse(HttpRequest request) {
		super(request);
	}

	@Override
	protected HttpResponse createResponseObject() {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.copiedBuffer(MESSAGE, CharsetUtil.UTF_8));
		
		response.headers().set(CONTENT_TYPE , "text/html; charset=UTF-8");
		
		return response;
	}

	
}
