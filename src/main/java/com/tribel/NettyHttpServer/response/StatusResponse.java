package com.tribel.NettyHttpServer.response;

import java.util.Map;

import com.tribel.NettyHttpServer.entity.Connection;
import com.tribel.NettyHttpServer.entity.RequestInfo;
import com.tribel.NettyHttpServer.entity.ServerStatistic;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;



public class StatusResponse extends AbstractResponse {

	protected final StringBuffer buf = new StringBuffer();

	public StatusResponse(HttpRequest request) {
		super(request);
	}

	@Override
	protected HttpResponse createResponseObject() {
		
		StringBuilder buf = generateStatistic();
		boolean keepAlive = HttpHeaders.isKeepAlive(request);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8));

		response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");

		if (keepAlive) {
			response.headers().set(CONTENT_LENGTH,
					response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}

		return response;

	}

	private StringBuilder generateStatistic() {
		ServerStatistic serverStatistic = ServerStatistic.getInstance();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<center><h3>Общее количество запросов: "
				+ serverStatistic.getTotalRequest() + "</h3>");
		stringBuilder.append("<h3><center>Открытые соединения: "
				+ serverStatistic.getActiveConnections() + "</h3></center><br>");
		stringBuilder.append(generateRequestsForEachIpTable());
		stringBuilder.append(generateRedirectsInfoTable());
		stringBuilder.append(generate16LastConnectionsTable());
		return stringBuilder;
	}

	private StringBuilder generateRequestsForEachIpTable() {
		StringBuilder stringBuilder = new StringBuilder();
		ServerStatistic serverStatistic = ServerStatistic.getInstance();

		stringBuilder.append("<center><table border=1><tr>" + "<th>ip</th>"
				+ "<th>количество уникальных запросов</th>"
				+ "<th>количество запросов</th>"
				+ "<th>время последнего запроса</th></tr>");
		for (RequestInfo reqtInfo : serverStatistic
				.getClientsInfo()) {
			stringBuilder.append("<tr>" + "<td>" + reqtInfo.getIp() + "</td>"
					+ "<td>" + reqtInfo.getUniqueRequests().size() + "</td>"
					+ "<td>" + reqtInfo.getRequestsCount() + "</td>" + "<td>"
					+ reqtInfo.getLastRequestTime() + "</td>" + "</tr></center>");
		}
		stringBuilder.append("</table>");

		return stringBuilder;
	}

	private StringBuilder generateRedirectsInfoTable() {
		StringBuilder stringBuilder = new StringBuilder();
		ServerStatistic serverStatistic = ServerStatistic.getInstance();

		stringBuilder.append("<center><br><h3>Информация о переадресациях:</h3>");
		stringBuilder.append("<table border=1><tr>" + "<th>url</th>"
				+ "<th>Количество переадресаций</th></tr>");
		Map<String, Integer> redirectsInfo = serverStatistic.getRedirectsInfo();
		for (String uri : redirectsInfo.keySet()) {
			stringBuilder.append("<tr>" + "<td>" + uri + "</td>" + "<td>"
					+ redirectsInfo.get(uri) + "</td></tr>");
		}

		stringBuilder.append("</table>");

		return stringBuilder;
	}

	private StringBuilder generate16LastConnectionsTable() {
		StringBuilder stringBuilder = new StringBuilder();
		ServerStatistic serverStatistic = ServerStatistic.getInstance();

		stringBuilder.append("<br><h3>Последние 16 соединений:</h3>");
		stringBuilder.append("<table border=1><tr>" + "<th>ip</th>"
				+ "<th>URI</th>" + "<th>время</th>" + "<th>отправленые байты</th>"
				+ "<th>полученые байты</th>" + "<th>скорость KB/s</th>"
				+ "</tr>");
		for (Connection connectionInfo : serverStatistic.getConnectionInfo()) {
			stringBuilder.append("<tr>" + "<td>" + connectionInfo.getIp()
					+ "</td>" + "<td>" + connectionInfo.getUri() + "</td>"
					+ "<td>" + connectionInfo.getTime() + "</td>" + "<td>"
					+ connectionInfo.getSentByte() + "</td>" + "<td>"
					+ connectionInfo.getReceivedByte() + "</td>" + "<td>"
					+ connectionInfo.getSpeed() + "</td></center>" );
		}

		stringBuilder.append("</table>");

		return stringBuilder;
	}

}
