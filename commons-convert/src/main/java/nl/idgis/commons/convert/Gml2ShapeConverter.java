/**
 * 
 */
package nl.idgis.commons.convert;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import nl.idgis.commons.cache.Item;
import nl.idgis.commons.convert.utils.FeatureCollectionFactory;
import nl.idgis.commons.convert.utils.ShapeFileGenerator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.PullParser;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @author Rob
 * 
 */
public class Gml2ShapeConverter implements Convert {
	private static final Log log = LogFactory.getLog(Gml2ShapeConverter.class);

	private File shapeFile = null;
	private String shapeName = null;

	@Override
	public long convert(InputStream is, OutputStream os) throws Exception {
		return convert(is, os, null);
	}

	@Override
	public long convert(InputStream is, OutputStream os, Item item) throws Exception {
		long count = 0;
		// create the parser with the gml 2.0 configuration
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

	private void setShapefile(File file) {
		shapeFile = file;
	}

	private void setShapename(String name) {
		shapeName = name;
	}

	protected int generateShp(InputStream is, OutputStream os, Configuration parserConfig) throws Exception {
		/** 
		 * important: 		 
		 * the outputstream is not used because a zip file is created in this method for shp related files.
		 * not closing os will interfere with making a proper zipfile.
		 */		 
		os.close();
		log.debug("Parser xsd: " + parserConfig.getXSD());
		PullParser parser = new PullParser(parserConfig, is, SimpleFeature.class);
		log.debug("Parser: " + parser);

		FeatureCollectionFactory featureCollectionFactory = new FeatureCollectionFactory();
		SimpleFeatureCollection fc = featureCollectionFactory.createFeatureCollection(parser);

		ShapeFileGenerator sg = new ShapeFileGenerator();
		sg.createZippedShapeFile(fc, shapeFile, shapeName);
		return fc.size();

	}

	public String getInputMimeType() {
		return ConverterMimeTypes.mimetypeGML21;
	}

	public String getOutputMimeType() {
		return ConverterMimeTypes.mimetypeSHP;
	}

	@Override
	public void setProperty(String key, Object prop) {
		if (key.equalsIgnoreCase("FILEPATH")) {
			if (prop != null) {
				File file = new File(prop.toString());
				setShapefile(file);
			}
		}
		if (key.equalsIgnoreCase("FILENAME")) {
			if (prop != null) {
				setShapename(prop.toString());
			}
		}
	}

}
