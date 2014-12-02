/**
 * 
 */
package nl.idgis.commons.convert.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import nl.idgis.commons.convert.Convert;
import nl.idgis.commons.convert.ConverterFactory;
import nl.idgis.commons.convert.ConverterMimeTypes;
import nl.idgis.commons.convert.FullCopyConverter;
import nl.idgis.commons.convert.Gml2KmlConverter;
import nl.idgis.commons.convert.Gml2ShapeConverter;
import nl.idgis.commons.convert.Gml3DxfConverter;
import nl.idgis.commons.convert.Gml3KmlConverter;
import nl.idgis.commons.convert.Gml3ShapeConverter;

/**
 * Standalone Converter utility.<br/>
 * usage:<br/>
 * <code> Converter &lt;file&gt; &lt;mimetype_in&gt; &lt;mimetype_out&gt;</code><br/>
 * Shows list of possible mimetypes when no arguments are given or no converter is found. 
 * @author Rob
 *
 */
public class Converter {
	private ConverterFactory converterFactory = new ConverterFactory ();
	
	public Converter(){
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML21,  ConverterMimeTypes.mimetypeKML, new Gml2KmlConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML21,  ConverterMimeTypes.mimetypeSHP, new Gml2ShapeConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML31,  ConverterMimeTypes.mimetypeKML, new Gml3KmlConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML31,  ConverterMimeTypes.mimetypeSHP, new Gml3ShapeConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML32,  ConverterMimeTypes.mimetypeKML, new Gml3KmlConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML32,  ConverterMimeTypes.mimetypeSHP, new Gml3ShapeConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML31,  ConverterMimeTypes.mimetypeDXF, new Gml3DxfConverter());
		converterFactory.addConverter(ConverterMimeTypes.mimetypeGML32,  ConverterMimeTypes.mimetypeDXF, new Gml3DxfConverter());
	}

	private String getMimetypes(){
		StringBuilder mimeCombinations = new StringBuilder();
		List<String> mimeTypes = converterFactory.getKeys();
		for (String mimeType : mimeTypes) {
			mimeCombinations.append("\n" + mimeType);
		}
		return mimeCombinations.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Converter converter = new Converter();
		try {
			if (args.length == 3) {
				File fileI, fileO;
				FileInputStream is = null;
				FileOutputStream os = null;
				fileI = new File(args[0]);
				if (!fileI.exists()){
					throw new IllegalArgumentException("Input file " + fileI.getAbsolutePath() + " does not exist!");
				}
				System.out.println("File In:  " + fileI.getAbsolutePath());
				try {
					is = new FileInputStream(fileI);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				String mimeTypeIn  = args[1];
				String mimeTypeOut = args[2];
				System.out.println("MIME type In:  " + mimeTypeIn);
				System.out.println("MIME type Out: " + mimeTypeOut);
				
				Convert convert = converter.converterFactory.getConverter(mimeTypeIn, mimeTypeOut);
				if (convert == null) {
					throw new IllegalArgumentException("not found: converter for conversion from "
									+ mimeTypeIn + " to " + mimeTypeOut 
									+ "\n Available combinations: " + converter.getMimetypes());
				} 
				
				String filename = fileI.getAbsolutePath();
				String extension;
				if (mimeTypeOut.indexOf("kml") >= 0){
					extension = ".kml";
				}else if (mimeTypeOut.indexOf("gml") >= 0){
					extension = ".gml";
				}else{
					extension = ".zip";
				}
				
				fileO = new File(filename.substring(0, filename.length() - 4)
						+ extension);
				if (fileO.getAbsolutePath().equals(fileI.getAbsolutePath())){
					fileO = new File(filename.substring(0, filename.length() - 4)
							+ extension+".converted");
				}
				System.out.println("File Out: " + fileO.getAbsolutePath());
				try {
					os = new FileOutputStream(fileO);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

				convert.setProperty("FILE", fileO.getAbsolutePath());
				long count = convert.convert(is, os);
				if ( convert instanceof FullCopyConverter)
					System.out.println("File size: " + count);
				else
					System.out.println("Nr of items parsed: " + count);

			} else {
				System.err.println("Wrong nr of arguments: " + args.length);
				System.err.println("Put arguments between \" to prevent errors with spaces in filenames or mimetypes");
				System.err.println("\nUsage: Converter \"<file.gml>\" \"<mimetype input>\" \"<mimetype output>\"");
				System.err.println("Available mime types: " + "(Input - Output)" + converter.getMimetypes());
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
		}

	}

}
