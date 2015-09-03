package com.tribel.NettyHttpServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class HttpServer {

	// private final int port;
	static final boolean SSL = System.getProperty("ssl") != null;
	/*static final int PORT = Integer.parseInt(System.getProperty("port",
			SSL ? "8443" : "8080"));  

	*/
	
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
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(),
					ssc.privateKey());
		} else {
			sslCtx = null;
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup worckerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, worckerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new HttpServerInitializer(sslCtx))
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
