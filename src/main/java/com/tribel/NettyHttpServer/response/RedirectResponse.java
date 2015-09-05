package com.tribel.NettyHttpServer.response;

import java.util.List;
import java.util.Map;

import com.tribel.NettyHttpServer.entity.ServerStatistic;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class RedirectResponse extends AbstractResponse{

	public static final String GET_REQUEST_URL_KEY = "url";
	
	public RedirectResponse(HttpRequest request) {
		super(request);
	}
	
	@Override
	protected HttpResponse createResponseObject() {
		boolean keepAlive = HttpHeaders.isKeepAlive(request);
		String urlForRedirect = "";
		Map<String, List<String>> params = new QueryStringDecoder(request.getUri()).parameters();
		for (String key:params.keySet()){
            if(key.equalsIgnoreCase(GET_REQUEST_URL_KEY)){
                urlForRedirect = getUrlValueForRedirectFromUrlParams(params.get(key));
            }
        }
		
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, urlForRedirect);

        ServerStatistic.getInstance().addRedirectInfo(urlForRedirect);
        
        if(keepAlive) {
			response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
		} 
        
        return response;
 	}
	
	private String getUrlValueForRedirectFromUrlParams(List<String> values){
        for (String value:values){
            return value;
        }
        return "";
    }
}
