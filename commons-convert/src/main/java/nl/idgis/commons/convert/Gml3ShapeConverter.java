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

/**
 * @author Rob
 * 
 */
public class Gml3ShapeConverter extends Gml2ShapeConverter {
	private static final Log log = LogFactory.getLog(Gml3ShapeConverter.class);
	
	@Override
	public long convert(InputStream is, OutputStream os, Item item) throws Exception {
		long count = 0;
		// create the parser with the gml 3.0 configuration
		try {
			Configuration parserConfig = new GMLConfiguration();
			count = generateShp(is, os, parserConfig);
			log.debug("features parsed: " + count);
		} finally {
			is.close();
			os.close();
		}
		return count;
	}

	public String getInputMimeType() {
		return ConverterMimeTypes.mimetypeGML31;
	}

}
