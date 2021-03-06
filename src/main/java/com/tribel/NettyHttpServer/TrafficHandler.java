package com.tribel.NettyHttpServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Date;

import com.tribel.NettyHttpServer.entity.Connection;
import com.tribel.NettyHttpServer.entity.ServerStatistic;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;

public class TrafficHandler extends ChannelTrafficShapingHandler {

	private long timeStamp;
	private Connection connectionInfo;

	public TrafficHandler(Connection connectionInfo, long writeLimit,
			long readLimit) {

		super(writeLimit, readLimit);
		this.connectionInfo = connectionInfo;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ServerStatistic.getInstance().increaseActiveConnections();
		super.channelActive(ctx);
	}
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        long currentReadBytes = trafficCounter.currentReadBytes();
        long currentWriteBytes = trafficCounter.currentWrittenBytes();
        trafficCounter.stop();

        if ("/favicon.ico".equals(connectionInfo.getUri())) {
            return;
        }
        
        connectionInfo.setTime(new Date(timeStamp));
       
		InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        InetAddress inetaddress = socketAddress.getAddress();
        
        //@SuppressWarnings("static-access")
      
		String ipAddress = inetaddress.getHostAddress();
        connectionInfo.setIp(ipAddress);

        connectionInfo.setReceivedByte(currentReadBytes);

        int writeSpeed = (int) trafficCounter.lastWriteThroughput();
        connectionInfo.setSpeed(writeSpeed);
        connectionInfo.setSentByte(currentWriteBytes);
        
        ServerStatistic.getInstance().addConnectionInfo(connectionInfo);

        super.channelReadComplete(ctx);
    }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        timeStamp = System.currentTimeMillis();
        trafficCounter.start();
        super.channelRead(ctx, msg);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ServerStatistic.getInstance().decreaseActiveConnections();
        super.channelInactive(ctx);
    }
}
