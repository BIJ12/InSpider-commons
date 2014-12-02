/**
 * 
 */
package nl.idgis.commons.convert.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import nl.idgis.commons.utils.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.xml.PullParser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * @author eshuism 16 mei 2012
 */
public class FeatureCollectionFactory {
	private static final int MAX_ATTR_NAME_LEN = 10;
	private static final String GEOMETRY_ATTR_NAME = "geom";
	private static final Log log = LogFactory.getLog(FeatureCollectionFactory.class);
	String[] geometryNames = { "SHAPE", "geom", "geometry" };

	/**
	 * Create featureCollection based on geometry-errors of given job
	 * 
	 * @param job
	 * @return
	 */
	public SimpleFeatureCollection createFeatureCollection(PullParser parser) {
		log.debug("createFeatureCollection");
		SimpleFeature f = null;
		SimpleFeatureBuilder builder = null;
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
		/*
		 * A list to collect features as we create them.
		 */
		List<SimpleFeature> features = new ArrayList<SimpleFeature>();

		/*
		 * GeometryFactory will be used to create the geometry attribute of each
		 * feature (a Point object for the location)
		 */
		GeometryFactory geometryFactory = JTSFactoryFinder
				.getGeometryFactory(null);
		

		try {
			while ((f = (SimpleFeature) parser.parse()) != null) {
				// build a featuretype from the first feature
				if (builder == null) {
					SimpleFeatureType sft = createFeatureType(f);
					log.debug("SimpleFeatureType: " + StringUtils.maxLength(sft, 128));
					builder = new SimpleFeatureBuilder(sft);
				}

				// add geometry
//				Geometry geometry = geometryFactory.createGeometry((Geometry) f
//						.getDefaultGeometry());
				Geometry geometry = (Geometry) f.getDefaultGeometry();
							
				log.debug("SimpleFeature geometry: " + StringUtils.maxLength(geometry, 64) + " ...");
				builder.add(geometry);

				// add attributes
				int i = 0;
				Collection<Property> c = f.getProperties();
				for (Iterator<Property> iterator = c.iterator(); iterator.hasNext();) {
					Property p = iterator.next();

					String name = p.getName().toString();
					boolean checkNames = checkString(name, geometryNames);
					if (!checkNames && p.getValue() != null) {
						builder.add(URLEncoder.encode(StringUtils.maxLength(p.getValue(), 255).trim(), "UTF-8"));
						log.debug(i++ + " + Attr: " + p.getName() + " = "
								+ StringUtils.maxLength(p.getValue(), 64) + "");
					}
				}
				
				
				SimpleFeature sf = builder.buildFeature(f.getID());
				log.debug("SimpleFeature org  : " + StringUtils.maxLength(f, 128));
				log.debug("SimpleFeature built: " + StringUtils.maxLength(sf, 128));
				featureCollection.add(sf);
//				features.add(sf);
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return featureCollection;
//		return new ListFeatureCollection(builder.getFeatureType(), features);
	}

	/**
	 * Create the schema for your FeatureType cq shapefile
	 */
	private SimpleFeatureType createFeatureType(SimpleFeature f) {
		log.debug("createFeatureType from " + f);
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName(f.getName());

		// TODO remove hardcoded CRS 
		try {
			CRSAuthorityFactory authorityFactory = ReferencingFactoryFinder
					.getCRSAuthorityFactory("epsg", null);
			builder.setCRS(authorityFactory
					.createCoordinateReferenceSystem("28992")); // <- Coordinate
																// reference
																// system
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Geometrie
		Object geom = f.getDefaultGeometry();
		if (geom instanceof MultiPolygon){
			log.debug("GEOMETRY TYPE: MultiPolygon");
			builder.add(GEOMETRY_ATTR_NAME, MultiPolygon.class);
		} else if (geom instanceof Polygon){
			log.debug("GEOMETRY TYPE: Polygon");
			builder.add(GEOMETRY_ATTR_NAME, Polygon.class);
		} else if (geom instanceof MultiLineString){
			log.debug("GEOMETRY TYPE: MultiLineString");
			builder.add(GEOMETRY_ATTR_NAME, MultiLineString.class);
		} else if (geom instanceof LineString){
			log.debug("GEOMETRY TYPE: LineString");
			builder.add(GEOMETRY_ATTR_NAME, LineString.class);
		} else if (geom instanceof MultiPoint){
			log.debug("GEOMETRY TYPE: MultiPoint");
			builder.add(GEOMETRY_ATTR_NAME, MultiPoint.class);
		} else if (geom instanceof Point){
			log.debug("GEOMETRY TYPE: Point");
			builder.add(GEOMETRY_ATTR_NAME, Point.class);
		} else {
			log.error("NO TYPE FOR GEOMETRY " + geom);
		}
		builder.setDefaultGeometry(GEOMETRY_ATTR_NAME);
		
		// add attributes
		int i = 0;
		Collection<Property> c = f.getProperties();
		for (Iterator<Property> iterator = c.iterator(); iterator.hasNext();) {
			Property p = iterator.next();

			String name = p.getName().toString();
			boolean checkNames = checkString(name, geometryNames);
			if (!checkNames && p.getValue() != null) {
				/* fout in ShapefileDataStore.createDbaseHeader(SimpleFeatureType)
				 * waardoor attribuutnamen langer dan 10 chars 
				 * dezelfde kolomnaam kunnen krijgen.
				 * De inhoud blijft dan leeg!
				 */
				if (name.length() > MAX_ATTR_NAME_LEN){ 
					// restrict names to 8 or 9 chars + number
					name = name.substring(0, ((i < 10) ? (MAX_ATTR_NAME_LEN-1) : (MAX_ATTR_NAME_LEN-2))) + i;
				}
				builder.add(name, String.class);
				log.debug(i + "   Attr: " + p.getName() + " ["+name+"] = "
						+ p.getValue() + "");
				i++;
			}
		}
		return builder.buildFeatureType();
	}

	private boolean checkString(String stringToCheck, String... names) {
		for (int i = 0; i < names.length; i++) {
			if (stringToCheck.equalsIgnoreCase(names[i])) {
				return true;
			}
		}
		return false;
	}

}
