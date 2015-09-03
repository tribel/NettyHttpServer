package com.tribel.NettyHttpServer.response;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class DefaultResponse extends AbstractResponse {

	public DefaultResponse(HttpRequest request) {
		super(request);
	}

	@Override
	protected HttpResponse createHttpResponseObject() {
		StringBuffer buf = prepareResponseData();
		boolean keepAlive = HttpHeaders.isKeepAlive(request);

		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK,
				Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8));
		;

		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

		if (keepAlive) {
			response.headers().set(CONTENT_LENGTH,
					response.content().readableBytes());
			response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		}
		return response;
	}

	private StringBuffer prepareResponseData() {
		StringBuffer buf = new StringBuffer();
		buf.setLength(0);
		buf.append("WELCOME TO THE WEB SERVER\r\n");
		buf.append("===================================\r\n");

		buf.append("VERSION: ").append(request.getProtocolVersion())
				.append("\r\n");
		buf.append("HOSTNAME: ").append(request.headers().get(HOST))
				.append("\r\n");
		buf.append("REQUEST_URI: ").append(request.getUri()).append("\r\n\r\n");

		HttpHeaders headers = request.headers();
		if (!headers.isEmpty()) {
			for (Map.Entry<String, String> h : headers) {
				String key = h.getKey();
				String value = h.getValue();
				buf.append("HEADER: ").append(key).append(" = ").append(value)
						.append("\r\n");
			}

			buf.append("\r\n");
		}

		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
				request.getUri());
		Map<String, List<String>> parameters = queryStringDecoder.parameters();

		if (!parameters.isEmpty()) {
			for (Entry<String, List<String>> p : parameters.entrySet()) {
				String key = p.getKey();
				List<String> values = p.getValue();
				for (String v : values) {
					buf.append("PARAM: ").append(key).append(" = ").append(v)
							.append("\r\n");
				}
			}
			buf.append("\r\n");
		}

		appendDecoderResult(buf, request);
		return buf;
	}

	private void appendDecoderResult(StringBuffer buf, HttpObject ob) {
		DecoderResult result = ob.getDecoderResult();

		if (result.isSuccess())
			return;

		buf.append(".. WITH DECODER FAILURE: ");
		buf.append(result.cause());
		buf.append("\r\n");
	}

}
