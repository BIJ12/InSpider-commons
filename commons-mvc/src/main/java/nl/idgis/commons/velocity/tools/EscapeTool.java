package nl.idgis.commons.velocity.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.web.util.HtmlUtils;

public class EscapeTool {

	public String html (final Object input) {
		if (input == null) {
			return "";
		}
		return HtmlUtils.htmlEscape (input.toString ());
	}
	
	public String javascript (final Object input) {
		if (input == null) {
			return "";
		}
		
		return StringEscapeUtils.escapeJavaScript (input.toString ());
	}

	public String url (final Object input) {
		if (input == null) {
			return "";
		}
		
		try {
			return URLEncoder.encode (input.toString (), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public String xml (final Object input) {
		if (input == null) {
			return "";
		}
		
		return StringEscapeUtils.escapeXml (input.toString ());
	}
}
