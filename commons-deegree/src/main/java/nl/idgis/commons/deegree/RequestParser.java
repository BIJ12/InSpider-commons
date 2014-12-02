package nl.idgis.commons.deegree;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

class RequestParser {
	
	final HttpServletRequest request;
	final Map<String, String> params; 

	RequestParser(final HttpServletRequest request) {
		this.request = request;
		params = new HashMap<String, String>();
		
		for(final Object key : request.getParameterMap().keySet()) {
			final String paramName = key.toString().toLowerCase();
			final String paramValue = ((String[])request.getParameterMap().get(key))[0];
			
			params.put(paramName, paramValue);
		}
	}
	
	String getParameter(final String paramName) {
		return params.get(paramName);
	}
	
	boolean parameterExists(final String paramName) {
		return params.containsKey(paramName);
	}
	
	boolean parameterEquals(final String paramName, final String paramValue) {
		return paramValue.equals(params.get(paramName));
	} 
	
	boolean isGetMethod() {
		return "GET".equals(request.getMethod());
	}
	
	boolean headerContains(final String headerName, final String... headerValueFragments) {
		final String headerValue = request.getHeader(headerName);
		if(headerValue == null) {
			return false;
		}
		
		for(final String headerValueFragment : headerValueFragments) {
			if(headerValue.contains(headerValueFragment)) {
				return true;
			}
		}
		
		return false; 
	}
}
