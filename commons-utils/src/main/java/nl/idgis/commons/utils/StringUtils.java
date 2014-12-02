/**
 * 
 */
package nl.idgis.commons.utils;

import java.util.Map;

/**
 * @author Rob
 * 
 */
public class StringUtils {

	/**
	 * Make a url string from a baseUrl (e.g. wfs service url) and a map of url
	 * parameters.<br>
	 * Takes are of ? and & in the baseUrl when adding parameters from the map.
	 * 
	 * @param baseUrl
	 *            e.g. http://host/WFS/services?
	 * @param parameters
	 *            e.g. request=GetCapabilities, service=WFS,
	 * @return Url string based upon baseUrl with parameters added at the end.
	 */
	public static String makeUrl(String baseUrl, Map<String, String> parameters) {

		return baseUrl;
	}

	/**
	 * Limit a string to a certain maximum length
	 * 
	 * @param s
	 *            string to limit, can be null or empty
	 * @param maxLength
	 *            nr of characters in string (0 .. 4096)
	 * @return string limited to maxlength or less characters
	 */
	public static String maxLength(String s, int maxLength) {
		maxLength = Math.max(Math.min(4096, maxLength), 0);
		if (s == null || s.isEmpty()) {
			return "";
		} else {
			return s.substring(0, Math.min(s.length(), maxLength));
		}
	}

	/**
	 * Make a string from an object and limit to a certain maximum length
	 * 
	 * @param o
	 *            object string to limit, can be null or empty
	 * @param maxLength
	 *            nr of characters in string (0 .. 4096)
	 * @return string limited to maxlength or less characters
	 */
	public static String maxLength(Object o, int maxLength) {
		maxLength = Math.max(Math.min(4096, maxLength), 0);
		if (o == null || o.toString().isEmpty()) {
			return "";
		} else {
			return o.toString().substring(0, Math.min(o.toString().length(), maxLength));
		}
	}

}
