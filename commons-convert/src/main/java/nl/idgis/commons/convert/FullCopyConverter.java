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
 * @author Rob
 *
 */
public class FullCopyConverter implements Convert {
	private String fromMimeType = ConverterMimeTypes.mimetypeBINARY;
	private String toMimeType   = ConverterMimeTypes.mimetypeBINARY;
	
	private static final int IO_BUFFER_SIZE = 8192;

	public FullCopyConverter(String fromMimeType, String toMimeType) {
		this.fromMimeType = fromMimeType ;
		this.toMimeType   = toMimeType;
	}

	@Override
	public long convert(InputStream is, OutputStream os, Item item)
			throws Exception {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		long total = 0;
		while ((read = is.read(b)) != -1) {
			os.write(b, 0, read);
			total += read;
		}
		is.close();
		os.close();
		return total;
	}
	
	@Override
	public long convert(InputStream is, OutputStream os) throws Exception {
		return convert(is, os, null); 
	}

	public String getInputMimeType() {
		return fromMimeType;
	}

	public String getOutputMimeType() {
		return toMimeType;
	}

	@Override
	public void setProperty(String key, Object prop) {
		// no properties to set
	}

}
