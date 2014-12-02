/**
 * 
 */
package nl.idgis.commons.convert;

import java.io.InputStream;
import java.io.OutputStream;

import nl.idgis.commons.cache.Item;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.Configuration;

//import org.geotools.gml3.GML;

/**
 * @author Rob
 * 
 */
public class Gml3KmlConverter extends Gml2KmlConverter {
	private static final Log log = LogFactory.getLog(Gml3KmlConverter.class);

	@Override
	public long convert(InputStream is, OutputStream os, Item item) throws Exception {
		// create the parser with the gml 3.0 configuration
		Configuration parserConfig = new GMLConfiguration();
		long count = generateKml(is, os, parserConfig);
		log.debug("features parsed: " + count);
		return count;
	}

	
	public String getInputMimeType() {
		return ConverterMimeTypes.mimetypeGML31;
	}

}
