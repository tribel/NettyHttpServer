package com.tribel.NettyHttpServer.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ServerStatistic {

	private static final int CONNECTIONS_SAVE_NUMBER = 16;

	private int totalRequest;
	private int activeConnections;

	private List<Connection> connectionInfo;
	private Map<String, Integer> redirectsInfo;
	private List<RequestInfo> requestsInfo;

	private static final ServerStatistic serverStatistic = new ServerStatistic();

	private ServerStatistic() {
		this.totalRequest = 0;
		this.activeConnections = 0;
		connectionInfo = new ArrayList<>();
		redirectsInfo = new HashMap<>();
		requestsInfo = new ArrayList<>();
	}

	public static ServerStatistic getInstance() {
		return serverStatistic;
	}

	public synchronized void addConnectionInfo(Connection conIn) {
		if (connectionInfo.size() >= CONNECTIONS_SAVE_NUMBER) {
			connectionInfo.remove(0);
		}
		Iterator<Connection> iterator = connectionInfo.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().getUri() == null) iterator.remove();
		}
		
		connectionInfo.add(conIn);
		totalRequest++;
		addRequest(conIn);
	}

	private synchronized void addRequest(Connection conIn) {
		String ip = conIn.getIp();

		for (RequestInfo requesInfo : requestsInfo) {
			if (requesInfo.getIp().equals(ip)) {
				requesInfo.addRequest(conIn.getUri(), conIn.getTime());
				return;
			}
		}

		RequestInfo reqInfo = new RequestInfo(ip);
		reqInfo.addRequest(conIn.getUri(), conIn.getTime());
		requestsInfo.add(reqInfo);
	}

	public synchronized void addRedirectInfo(String url) {
		Integer redirectsCountForUrl = redirectsInfo.get(url);
		redirectsCountForUrl = (redirectsCountForUrl != null) ? ++redirectsCountForUrl: 1;
		redirectsInfo.put(url, redirectsCountForUrl);
	}

	public synchronized void increaseTotalRequests() {
		totalRequest++;
	}

	public void increaseActiveConnections() {
		changeActiveConnectionsValue(1);
	}

	public void decreaseActiveConnections() {
		changeActiveConnectionsValue(-1);
	}

	private synchronized void changeActiveConnectionsValue(int delta) {
		activeConnections += delta;
	}

	public synchronized int getActiveConnections() {
		return activeConnections;
	}

	public int getTotalRequest() {
		return totalRequest;
	}

	public List<Connection> getConnectionInfo() {
		return connectionInfo;
	}

	public Map<String, Integer> getRedirectsInfo() {
		return redirectsInfo;
	}

	public List<RequestInfo> getClientsInfo() {
		return requestsInfo;
	}

	@Override
	public String toString() {
		return "totalRequests = " + totalRequest + ", activeConnections = "
				+ activeConnections;
	}

}
