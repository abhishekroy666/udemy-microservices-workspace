package com.udemy.microservices.netflixzuulapigatewayserver;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class ZuulLoggingFilter extends ZuulFilter {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Object run() throws ZuulException {
		// business logic implementation
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
		logger.info("request -> {} request uri -> {}", request, request.getRequestURI());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// should we filter this request or not?
		return true;
	}

	@Override
	public int filterOrder() {
		// set a priority between multiple filters
		return 1;
	}

	@Override
	public String filterType() {
		// type of requests we want to filter
		// eg : pre(all), error, ... etc 
		return "pre";
	}

}
