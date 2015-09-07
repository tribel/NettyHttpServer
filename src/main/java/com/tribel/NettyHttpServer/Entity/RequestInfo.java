package com.tribel.NettyHttpServer.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RequestInfo {

	private String ip;
	private int requestsCount;
	private Date lastRequestTime;

	private Set<String> uniqueRequests;

	public RequestInfo(String ip) {
		this.ip = ip;
		requestsCount = 0;
		lastRequestTime = new Date(System.currentTimeMillis());
		uniqueRequests = new HashSet<>();
	}

	public void addRequest(String request, Date timestamp) {
		lastRequestTime = (timestamp != null) ? timestamp : lastRequestTime;
		uniqueRequests.add(request);
		requestsCount++;
	}

	public String getIp() {
		return ip;
	}

	public Date getLastRequestTime() {
		return lastRequestTime;
	}

	public Set<String> getUniqueRequests() {
		return uniqueRequests;
	}

	public int getRequestsCount() {
		return requestsCount;
	}

	@Override
	public int hashCode() {
		return ip.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return ((RequestInfo) obj).ip.equals(ip);
	}

	@Override
	public String toString() {
		return String
				.format("ClientInfo[ip = %s,  requestsCount = %s, lastRequestTime = %s, uniqueRequests = %s]",
						ip, requestsCount, lastRequestTime,
						uniqueRequests.toString());
	}


}
