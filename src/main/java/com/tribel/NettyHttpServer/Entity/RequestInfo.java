package com.tribel.NettyHttpServer.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RequestInfo {

	private String ip;
	private int totalRequest;
	private Date lastRequestTime;
	
	private Set<String> uniqueRequest;
	
	public RequestInfo(String ip) {
		this.ip = ip;
		uniqueRequest = new HashSet<>();
		lastRequestTime = new Date();
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTotalRequest() {
		return totalRequest;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}

	public void setLastRequestTime(long lastRequestTime) {
		this.lastRequestTime = new Date(lastRequestTime);
	}

	public Set<String> getUniqueRequest() {
		return uniqueRequest;
	}

	public void setUniqueRequest(Set<String> uniqueRequest) {
		this.uniqueRequest = uniqueRequest;
	}
	
	public void addRequest(String request) {
		totalRequest++;
		uniqueRequest.add(request);
		
	}
}
