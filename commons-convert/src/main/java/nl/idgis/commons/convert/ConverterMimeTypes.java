/**
 * 
 */
package nl.idgis.commons.convert;

/**
 * List of mimetypes to be used in setting up converterfactory.<br/>
 * Make sure these are lower case and enter lowercase 
 * in method converterFactory.getConverter(mimeType, mimeType).
 * @author Rob
 *
 */
public class ConverterMimeTypes {
	public final static String mimetypeGML21 = "application/gml+xml; version=2.1";
	public final static String mimetypeGML311 = "text/xml; subtype=gml/3.1.1";
	public final static String mimetypeGML311p = "text/xml; subtype=gml/3.1.1/profiles/gmlsf/1.0.0/0";
	public final static String mimetypeGML31 = "application/gml+xml; version=3.1";
	public final static String mimetypeGML32 = "application/gml+xml; version=3.2";
	public final static String mimetypeKML = "application/vnd.google-earth.kml+xml";
	public final static String mimetypeSHP = "application/octet-stream";
	public final static String mimetypeDXF = "application/dxf";

	public final static String mimetypeBINARY = "application/octet-stream";
	public final static String mimetypeTEXT = "text/plain";
}
