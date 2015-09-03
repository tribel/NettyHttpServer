package com.tribel.NettyHttpServer.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerStatistic {

	private static final int CONNECTIONS_INFO_NUMBER_TO_STORE = 16;

	private int totalRequest;
	private int activeConnections;

	private List<Connection> connectionInfo;
	private Map<String, Integer> redirectsInfo;
	private List<ClientInfo> clientsInfo;

	private static final ServerStatistic serverStatistic = new ServerStatistic();

	private ServerStatistic() {
		this.totalRequest = 0;
		this.activeConnections = 0;
		connectionInfo = new ArrayList<>();
		redirectsInfo = new HashMap<>();
		clientsInfo = new ArrayList<>();
	}

	public static ServerStatistic getInstance() {
		return serverStatistic;
	}

	public synchronized void addConnectionInfo(Connection conIn) {
		if (connectionInfo.size() >= CONNECTIONS_INFO_NUMBER_TO_STORE) {
			connectionInfo.remove(0);
		}

		connectionInfo.add(conIn);
		totalRequest++;
		addRequest(conIn);
	}

	private synchronized void addRequest(Connection conIn) {
		String ip = conIn.getIp();

		for (ClientInfo clientInfo : clientsInfo) {
			if (clientInfo.getIp().equals(ip)) {
				clientInfo.addRequest(conIn.getUri(), conIn.getTime());
				return;
			}
		}

		ClientInfo clientInfo = new ClientInfo(ip);
		clientInfo.addRequest(conIn.getUri(), conIn.getTime());
		clientsInfo.add(clientInfo);
	}

	public synchronized void addRedirectInfo(String url) {
		Integer redirectsCountForUrl = redirectsInfo.get(url);
		redirectsCountForUrl = (redirectsCountForUrl != null) ? ++redirectsCountForUrl
				: 1;
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

	public List<ClientInfo> getClientsInfo() {
		return clientsInfo;
	}

	@Override
	public String toString() {
		return "totalRequests = " + totalRequest + ", activeConnections = "
				+ activeConnections;
	}

	public class ClientInfo {

		private String ip;
		private int requestsCount;
		private Date lastRequestTime;

		private Set<String> uniqueRequests;

		public ClientInfo(String ip) {
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
			return ((ClientInfo) obj).ip.equals(ip);
		}

		@Override
		public String toString() {
			return String
					.format("ClientInfo[ip = %s,  requestsCount = %s, lastRequestTime = %s, uniqueRequests = %s]",
							ip, requestsCount, lastRequestTime,
							uniqueRequests.toString());
		}

	}

}
