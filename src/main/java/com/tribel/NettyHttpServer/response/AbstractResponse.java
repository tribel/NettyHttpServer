package com.tribel.NettyHttpServer.response;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public abstract class AbstractResponse {
	
	protected HttpResponse response;
	protected HttpRequest request;
	
	
	public AbstractResponse(HttpRequest request) {
		this.request = request;
		this.response = createResponseObject();
	}
	
	protected abstract HttpResponse createResponseObject();
	
	public void response(ChannelHandlerContext ctx) {
		ctx.write(response).addListener(ChannelFutureListener.CLOSE);
	}
}
