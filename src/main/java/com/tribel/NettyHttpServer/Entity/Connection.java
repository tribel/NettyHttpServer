package com.tribel.NettyHttpServer.Entity;

import java.util.Date;

public class Connection {
	
	private String ip;
	private String uri;
	private long receivedByte;
	private long sentByte;
	private int speedWrite;
	private int speedRead;
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

	public int getSpeedWrite() {
		return speedWrite;
	}

	public void setSpeedWrite(int speedWrite) {
		this.speedWrite = speedWrite;
	}

	public int getSpeedRead() {
		return speedRead;
	}

	public void setSpeedRead(int speedRead) {
		this.speedRead = speedRead;
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
				+ speedWrite + ", speedRead=" + speedRead + ", time=" + time
				+ "]";
	}
	
	
}
