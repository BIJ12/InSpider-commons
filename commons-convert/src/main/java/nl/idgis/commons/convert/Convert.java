/**
 * 
 */
package nl.idgis.commons.convert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;


import nl.idgis.commons.cache.Item;


/**
 * Format conversion between two streams, based upon their mime types.
 * @author Rob
 *
 */
public interface Convert {
	
	/**
	 * Perform a specific format conversion between two streams.
	 * @param is input stream
	 * @param os output stream
	 * @return nr of converted parts (e.g. features)
	 * @throws IOException
	 */
	public long convert(InputStream is, OutputStream os) throws Exception;

	/**
	 * Perform a specific format conversion between two streams.
	 * @param is input stream
	 * @param os output stream
	 * @param item of a Cache, in case the conversion produces more than one file.
	 * @return nr of converted parts (e.g. features)
	 * @throws IOException
	 */
	public long convert(InputStream is, OutputStream os, Item item) throws Exception;
	
	/**
	 * The mimetype of the inputstream
	 * @return mimetype like "application/gml+xml; version=3.2"
	 */
	public String getInputMimeType();
	
	/**
	 * The mimetype of the outputstream
	 * @return mimetype like "application/gml+xml; version=3.2"
	 */
	public String getOutputMimeType();
	
	/**
	 * Set properties that a certain converter may need, like File path.
	 * @param prop properties to set
	 */
	public void setProperty(String key, Object prop);
}
