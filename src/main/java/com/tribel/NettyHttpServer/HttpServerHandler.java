package com.tribel.NettyHttpServer;

import com.tribel.NettyHttpServer.entity.Connection;
import com.tribel.NettyHttpServer.response.AbstractResponse;
import com.tribel.NettyHttpServer.response.ResponseFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;



public class HttpServerHandler extends ChannelInboundHandlerAdapter {

	private Connection connectionInfo;

	public HttpServerHandler(Connection connection) {
		this.connectionInfo = connection;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {

		if (msg instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) msg;

			if ("/favicon.ico".equals(request.getUri())) {
				FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1,
						HttpResponseStatus.NOT_FOUND);
				sendHttpResponse(ctx, request, res);
				return;
			}

			if (HttpHeaders.is100ContinueExpected(request)) {
				send100Continue(ctx);
			}

			connectionInfo.setUri(request.getUri());
			ResponseFactory responseFactory = new ResponseFactory(request);
			AbstractResponse response = responseFactory.geResponset();
			response.response(ctx);
		}
	}

	private void send100Continue(ChannelHandlerContext ctx) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				CONTINUE);
		ctx.write(response);
	}

	private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req,
			FullHttpResponse res) {
		// Generate an error page if response getStatus code is not OK (200).
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			HttpHeaders.setContentLength(res, res.content().readableBytes());
		}

		// Send the response and close the connection if necessary.
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
}
