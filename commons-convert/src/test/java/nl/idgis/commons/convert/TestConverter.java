package nl.idgis.commons.convert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */

/**
 * @author Rob
 *
 */
public class TestConverter {
	String builder ;
	
	@Before
	public void buildInputString(){
		builder = "<wfs:FeatureCollection xsi:schemaLocation=\"http://atlas.brabant.nl/arcgis/services/pgr_m01_milieu/MapServer/WFSServer http://atlas.brabant.nl/ArcGIS/services/pgr_m01_milieu/MapServer/WFSServer?request=DescribeFeatureType%26version=1.0.0%26typename=Breuken http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\" "
				+ " xmlns:pgr_m01_milieu=\"http://atlas.brabant.nl/arcgis/services/pgr_m01_milieu/MapServer/WFSServer\""
				+ " xmlns:gml=\"http://www.opengis.net/gml\" "
				+ " xmlns:wfs=\"http://www.opengis.net/wfs\" "
				+ " xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
				+ " <gml:boundedBy>"
				+ " <gml:Box srsName=\"EPSG:28992\">"
				+ " <gml:coordinates>110706.344,358479.65600000002"
				+ " 202179.57800000001,444919.25</gml:coordinates>"
				+ " </gml:Box>"
				+ " </gml:boundedBy>"
				+ " <gml:featureMember>"
				+ " <pgr_m01_milieu:Breuken fid=\"F3030__31\">"
				+ " <pgr_m01_milieu:OBJECTID>31</pgr_m01_milieu:OBJECTID>"
				+ " <pgr_m01_milieu:FORMATIE>st</pgr_m01_milieu:FORMATIE>"
				+ " <pgr_m01_milieu:Shape>"
				+ " <gml:MultiLineString>"
				+ " <gml:lineStringMember>"
				+ " <gml:LineString>"
				+ " <gml:coordinates>"
				+ " 157131.76500000001,436387.31200000003"
				+ " 157212.46900000001,436205.71900000004"
				+ " 158132.23499999999,433844.06200000003"
				+ " 158868.46900000001,432157.71900000004"
				+ " 159880.46900000001,430547.71900000004"
				+ " 161927.92199999999,427807.28200000001"
				+ " 163142.78100000002,426180.06200000003"
				+ " 163871.34399999998,424887.375"
				+ " 166083.14000000001,421031.65600000002"
				+ " </gml:coordinates>"
				+ " </gml:LineString>"
				+ " </gml:lineStringMember>"
				+ " </gml:MultiLineString>"
				+ " </pgr_m01_milieu:Shape>"
				+ " <pgr_m01_milieu:LENGTE>"
				+ " 17855.305562950001</pgr_m01_milieu:LENGTE>"
				+ " <pgr_m01_milieu:SHAPE.LEN>0</pgr_m01_milieu:SHAPE.LEN>"
				+ " </pgr_m01_milieu:Breuken>"
				+ " </gml:featureMember>"
				+ " </wfs:FeatureCollection>";
	}
	
	@Test
	public void testGml2Kml() {
		Convert converter = new Gml2KmlConverter();
		
		String osStr = convert(converter);
		
		Assert.assertTrue("Expected: <Data name=\"LENGTE\">", osStr.indexOf("<Data name=\"LENGTE\">")>=0);
		Assert.assertTrue("Expected: <value>17855.30556295</value>", osStr.indexOf("<value>17855.30556295</value>")>=0);
		
	}

	@Test
	public void testFull() {
		Convert converter = new FullCopyConverter(null, null);
		
		String osStr = convert(converter);
		
		Assert.assertTrue("Expected: equal input / output", osStr.equals(builder));
	}

	@Test
	public void testGml2Shape() {
		Gml2ShapeConverter converter = new Gml2ShapeConverter();
		
		File file = new File(System.getProperty("user.home")+ File.separator + "TestGml2ShapeConverter.zip");
		file.deleteOnExit();
		Assert.assertFalse("Not expected: " + file.getAbsolutePath(), file.exists());
		converter.setProperty("FILEPATH",file.getAbsolutePath());
		converter.setProperty("FILENAME","test_test");

		String osStr = convert(converter);
		
		Assert.assertTrue("Expected: " + file.getAbsolutePath(), file.exists());
	}

	private String convert(Convert converter) {
		InputStream is = new ByteArrayInputStream( builder.getBytes() );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream os = baos;
		
		try {
			converter.convert(is, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String osStr = baos.toString();
//		System.err.println(osStr);
		return osStr;
	}
	

}