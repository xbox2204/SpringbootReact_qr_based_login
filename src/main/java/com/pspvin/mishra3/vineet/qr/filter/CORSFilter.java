package com.pspvin.mishra3.vineet.qr.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Source - https://stackoverflow.com/a/43409061
//Posted by cнŝdk, modified by community. See post 'Timeline' for change history
//Retrieved 2026-09-25, License - CC BY-SA 3.0

public class CORSFilter implements Filter {

	 // This is to be replaced with a list of domains allowed to access the server
	//You can include more than one origin here
	 private final List<String> allowedOrigins = Arrays.asList("http://localhost:4200","http://localhost:3000","http://localhost:5173"); 
	
	 public void destroy() {
	
	 }

	 public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	     // Lets make sure that we are working with HTTP (that is, against HttpServletRequest and HttpServletResponse objects)
	     if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
	         HttpServletRequest request = (HttpServletRequest) req;
	         HttpServletResponse response = (HttpServletResponse) res;
	
	         // Access-Control-Allow-Origin
	         String origin = request.getHeader("Origin");
	         response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "");
	         response.setHeader("Vary", "Origin");
	
	         // Access-Control-Max-Age
	         response.setHeader("Access-Control-Max-Age", "3600");
	
	         // Access-Control-Allow-Credentials
	         response.setHeader("Access-Control-Allow-Credentials", "true");
	
	         // Access-Control-Allow-Methods
	         response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	
	         // Access-Control-Allow-Headers
	         response.setHeader("Access-Control-Allow-Headers",
	             "Origin, X-Requested-With, Content-Type, Accept, " + "X-CSRF-TOKEN");
	     }
	
	     chain.doFilter(req, res);
	 }

	 public void init(FilterConfig filterConfig) {
	 }
}


