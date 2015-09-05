package com.tribel.NettyHttpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class HttpServer {

	static final boolean SSL = System.getProperty("ssl") != null;

	
	static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
	
	public HttpServer(int port) {
	}

	public static void main(String[] args) throws Exception {
		int port;

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}

		new HttpServer(port).start();
	}

	public void start() throws Exception {

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup worckerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, worckerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new HttpServerInitializer())
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(PORT).sync();
			f.channel().closeFuture().sync();

		} finally {
			worckerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
