package com.tribel.NettyHttpServer;

import com.tribel.NettyHttpServer.entity.Connection;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
	
	 public HttpServerInitializer() {}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		Connection connectionInfo = new Connection();
		
		pipeline.addLast(new TrafficHandler(connectionInfo, 0, 0));
		pipeline.addLast(new HttpRequestDecoder());
		pipeline.addLast(new HttpResponseEncoder());
		pipeline.addLast(new HttpServerHandler(connectionInfo));
	}
}
