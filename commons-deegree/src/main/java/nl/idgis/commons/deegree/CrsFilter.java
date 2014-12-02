package nl.idgis.commons.deegree;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;

public class CrsFilter implements Filter {
	
    private static final Logger LOG = getLogger(CrsFilter.class);

	private final class ParameterOverrideWrapper extends HttpServletRequestWrapper {
		
		private final Map<String, Object[]> parameterOverrides;

		private ParameterOverrideWrapper(HttpServletRequest request, Map<String, Object[]> parameterOverrides) {
			super(request);
			
			this.parameterOverrides = parameterOverrides;
		}

		@Override
		public String getQueryString() {
			
			String paramSeparator = "";
			final StringBuilder queryStringBuilder = new StringBuilder();
			
			@SuppressWarnings("rawtypes")
			final Map parameters = getParameterMap();
			for(final Object param : parameters.keySet()) {
				final String paramLowerCase = param.toString().toLowerCase();
				
				Object[] paramValues;
				if(parameterOverrides.containsKey(paramLowerCase)) {
					paramValues = parameterOverrides.get(paramLowerCase);
				} else {
					paramValues = (Object[])parameters.get(param); 
				}
				
				queryStringBuilder.append(paramSeparator);
				queryStringBuilder.append(param.toString());
				queryStringBuilder.append("=");
				
				String valueSeparator = "";
				for(Object paramValue : paramValues) {
					queryStringBuilder.append(valueSeparator);
					try {
						queryStringBuilder.append(URLEncoder.encode(paramValue.toString(), "utf-8"));
					} catch(UnsupportedEncodingException e) {}
					
					valueSeparator = ",";
				}
				
				paramSeparator = "&";
			}
			
			return queryStringBuilder.toString();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
		final Map<String, Object[]> parameterOverrides = new HashMap<String, Object[]>();
		
		try {
			String service = null, request = null, version = null, crs = null, srs = null;
			
			@SuppressWarnings("rawtypes")
			final Map parameters = servletRequest.getParameterMap();
			for(Object param : parameters.keySet()) {
				String paramKey = param.toString().toLowerCase();
				
				if(paramKey.equals("request")) {
					request = (String)((Object [])parameters.get(param))[0];
				} else if(paramKey.equals("service")) {
					service = (String)((Object [])parameters.get(param))[0];
				} else if(paramKey.equals("version")) {
					version = (String)((Object [])parameters.get(param))[0];
				} else if(paramKey.equals("crs")) {
					crs = (String)((Object [])parameters.get(param))[0];
				} else if(paramKey.equals("srs")) {
					srs = (String)((Object [])parameters.get(param))[0];
				}
			}			
			
			if("WMS".equals(service)
				|| "GetMap".equals(request)
				|| "GetFeatureInfo".equals(request)) {
				
				LOG.debug("WMS request detected");
				
				if("1.3.0".equals(version) && crs != null && crs.startsWith("EPSG:")) {
					LOG.debug("1.3.0 with 'short' crs detectected");
					
					parameterOverrides.put("crs", new Object[]{"urn:ogc:def:crs:EPSG::" + crs.split(":")[1]});
				} else if("1.1.1".equals(version) && srs != null && srs.startsWith("urn:ogc:def:crs:EPSG::")) {
					LOG.debug("1.1.1 with 'long' srs detectected");
					
					parameterOverrides.put("srs", new Object[]{"EPSG:" + srs.split("EPSG::")[1]});
				}
			}
			
			
		} catch(Throwable t) {
			LOG.error("Exception thrown", t);
		}
		
		if(parameterOverrides.isEmpty()) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			chain.doFilter(new ParameterOverrideWrapper(httpServletRequest, parameterOverrides), servletResponse);
		}
	}

	@Override
	public void destroy() {
		
	}
}
