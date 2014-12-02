/**
 * 
 */
package nl.idgis.commons.convert;

// TODO make proper convert method from GML to DXF
/**
 * @author Rob
 *
 */
public class Gml3DxfConverter extends FullCopyConverter {

	public Gml3DxfConverter() {
		super(null, null);
	}
	
	public String getInputMimeType() {
		return ConverterMimeTypes.mimetypeGML31;
	}

	public String getOutputMimeType() {
		return ConverterMimeTypes.mimetypeDXF;
	}

}
