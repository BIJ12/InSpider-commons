/**
 * 
 */
package nl.idgis.commons.convert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Rob
 * 
 */
public class ConverterFactory {

	private Map<String, Convert> converters;

	public ConverterFactory() {
		converters = new HashMap<String, Convert>();
	}

	public void setConverters(Map<String, Convert> converters) {
		this.converters = converters;
	}

	public void addConverter(String fromMimeType, String toMimeType, Convert converter) {
		this.converters.put(makeKey(fromMimeType, toMimeType), converter);
	}

	public void removeConverter(String fromMimeType, String toMimeType) {
		this.converters.remove(makeKey(fromMimeType, toMimeType));
	}

	public List<String> getKeys(){
		return new ArrayList<String>(converters.keySet());
	}
	
	/**
	 * Get a Converter between mimetypes.<br>
	 * If the mimetypes are equal, or toMimeType is null or both are null, a
	 * direct copy converter is returned
	 * 
	 * @param fromMimeType
	 *            e.g. "application/xml", null signifies "don't care"
	 * @param toMimeType
	 *            e.g. "application/xml", null signifies "don't care"
	 */
	public Convert getConverter(String fromMimeType, String toMimeType){
		if (fromMimeType == null || toMimeType == null
				|| fromMimeType.equalsIgnoreCase(toMimeType)) {
			// special case: direct copy
			return new FullCopyConverter(fromMimeType, toMimeType);
		} else {
			Convert converter = converters.get(makeKey(fromMimeType, toMimeType));
			return converter;
		}
	}

	private String makeKey(String fromMimeType, String toMimeType){
		return fromMimeType + " -> " + toMimeType;
	}
	
}
