package com.tribel.NettyHttpServer.entity;

import java.util.Date;

public class Connection {
	
	private String ip;
	private String uri;
	private long receivedByte;
	private long sentByte;
	private int speed;
	private Date time;
	
	public Connection() {
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public long getReceivedByte() {
		return receivedByte;
	}

	public void setReceivedByte(long receivedByte) {
		this.receivedByte = receivedByte;
	}

	public long getSentByte() {
		return sentByte;
	}

	public void setSentByte(long sentByte) {
		this.sentByte = sentByte;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Connection [ip=" + ip + ", uri=" + uri + ", receivedByte="
				+ receivedByte + ", sentByte=" + sentByte + ", speedWrite="
				+ speed + time
				+ "]";
	}
	
	
}
