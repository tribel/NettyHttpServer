package com.tribel.NettyHttpServer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
	
	private final SslContext sslCtx; 
	
	public HttpServerInitializer(){
		this.sslCtx = null; 				/////////////// WARNING!
	}
	
	public HttpServerInitializer(SslContext  sslCtx) {
		this.sslCtx = sslCtx;
	}
	
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		if (sslCtx != null) { 
			pipeline.addLast(sslCtx.newHandler(ch.alloc()));
		}
		
		//ConnectionInfo
		
		ch.pipeline().addLast(new HttpServerHandler());
	}
}
