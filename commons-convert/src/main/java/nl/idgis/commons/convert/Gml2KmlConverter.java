/**
 * 
 */
package nl.idgis.commons.convert;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;

import nl.idgis.commons.cache.Item;
import nl.idgis.commons.utils.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.PullParser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

//import org.geotools.gml3.GML;

/**
 * @author Rob
 * 
 */
public class Gml2KmlConverter implements Convert {
	private static final Log log = LogFactory.getLog(Gml2KmlConverter.class);

	@Override
	public long convert(InputStream is, OutputStream os) throws Exception {
		return convert(is, os, null); 
	}

	@Override
	public long convert(InputStream is, OutputStream os, Item item) throws Exception {
		// create the parser with the gml 2.0 configuration
		Configuration parserConfig = new GMLConfiguration();
		long count = generateKml(is, os, parserConfig);
		log.debug("features parsed: " + count);
		return count;
	}

	protected int generateKml(InputStream is, OutputStream os, Configuration parserConfig) throws Exception
			 {
		log.debug("Parser xsd: " + parserConfig.getXSD());
		
		PullParser parser = null;
		parser = new PullParser(parserConfig, is, SimpleFeature.class);
		log.debug("Parser: " + parser);
		
		String[] geometryNames = {"SHAPE", "geom", "geometry"};
		SimpleFeature f = null;
		PrintStream ps = new PrintStream(os);
		int count = 0;
		try {
			log.debug("Start kml");
			ps.println("<?xml version='1.0' encoding='UTF-8'?>" );
			ps.println("<kml xmlns='http://www.opengis.net/kml/2.2'>");
			ps.println("<Document>");
			while ((f = (SimpleFeature) parser.parse()) != null) {
				log.debug("Feature # " + count + " ID:"+f.getID()+" = [" + StringUtils.maxLength(f, 64) + "]");
				
				ps.println("<Placemark>");
				ps.println("<name>"+(f.getAttribute("name")==null?f.getID():f.getAttribute("name"))+"</name>");
				// Attributen
				ps.println("<ExtendedData>");
				int i = 0;
				Collection<Property> c = f.getProperties();
				for (Iterator<Property> iterator = c.iterator(); iterator.hasNext();) {
					Property p = iterator.next();
					i++;
					boolean checkNames = checkString(p.getName().toString(),
							geometryNames);
					if (!checkNames && p.getValue() != null) {
						ps.println("<Data name=\"" + p.getName() + "\">");
						// trim newlines and spaces from the values
						ps.println("<value>" + URLEncoder.encode(p.getValue().toString().trim(), "UTF-8") + "</value>");
						ps.println("</Data>");
						log.debug(i + " + Attr: " + p.getName() + " = "
								+ StringUtils.maxLength(p.getValue(), 64) + "");
					} else {
						log.debug(i + " - Attr: " + p.getName() + " = "
								+ StringUtils.maxLength(p.getValue(), 64) + "");
					}
				}
				ps.println("</ExtendedData>");

				// Geometrie
				Geometry geom = (Geometry) f.getDefaultGeometry();
				generateGeometry(geom, ps);

				ps.println("</Placemark>");
				count++;
			}
		}catch(Exception e){
			log.debug("Exception, feature count sofar: " + count);
			throw e;
		} finally {
			ps.println("</Document>");
			ps.println("</kml>");
			is.close();
			ps.close();
		}
		return count;
	}

	private void generateGeometry(Geometry geom, PrintStream ps) {
		if (geom == null) {
			return;
		} else {
			String geometryStr = geom.toString();
			log.debug("Geometry: " + geometryStr.substring(0, Math.min(geometryStr.length()-1, 32)) + " ...");
			if (geom instanceof Point){
				ps.println("<Point>");
				ps.print("<coordinates>");
				for (int i = 0; i < +geom.getCoordinates().length; i++) {
					ps.print(geom.getCoordinates()[i].y);
					ps.print(",");
					ps.print(geom.getCoordinates()[i].x);
					ps.print(" ");
				}
				ps.println("</coordinates>");
				ps.println("</Point>");
			} else if (geom instanceof LineString){
				ps.println("<LinearRing>");
				ps.print("<coordinates>");
				for (int i = 0; i < +geom.getCoordinates().length; i++) {
					ps.print(geom.getCoordinates()[i].y);
					ps.print(",");
					ps.print(geom.getCoordinates()[i].x);
					ps.println(" ");
				}
				ps.println("</coordinates>");
				ps.println("</LinearRing>");

			} else if (geom instanceof MultiLineString){
				int n = geom.getNumGeometries();
				for (int j = 0; j < n; j++) {
					generateGeometry(geom.getGeometryN(j), ps);
				}				
			} else if (geom instanceof Polygon){
				Polygon poly = (Polygon)geom;
				ps.println("<Polygon>");
				ps.println("<outerBoundaryIs>");
				generateGeometry(poly.getExteriorRing(), ps);
				ps.println("</outerBoundaryIs>");
				
				for (int j = 1; j < poly.getNumInteriorRing(); j++) {
					ps.println("<innerBoundaryIs>");
					generateGeometry(poly.getInteriorRingN(j), ps);
					ps.println("</innerBoundaryIs>");
				}

				ps.println("</Polygon>");
				
			} else if (geom instanceof MultiPolygon){
				int n = geom.getNumGeometries();
				for (int j = 0; j < n; j++) {
					generateGeometry(geom.getGeometryN(j), ps);
				}
			}
		}
	}

	private boolean checkString(String stringToCheck, String... names){
		for (int i = 0; i < names.length; i++) {
			if (stringToCheck.equalsIgnoreCase(names[i])){
				return true;
			}
		}
		return false;
	}
	
	public String getInputMimeType() {
		return ConverterMimeTypes.mimetypeGML21;
	}

	public String getOutputMimeType() {
		return ConverterMimeTypes.mimetypeKML;
	}

	@Override
	public void setProperty(String key, Object prop) {
		// no properties to set
	}


}
