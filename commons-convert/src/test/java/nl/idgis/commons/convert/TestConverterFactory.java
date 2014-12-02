package nl.idgis.commons.convert;

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
public class TestConverterFactory {
	ConverterFactory factory;
	Gml2KmlConverter gml2kmlConverter;
	Gml3KmlConverter gml3kmlConverter;

	Gml2ShapeConverter gml2shpConverter;
	Gml3ShapeConverter gml3shpConverter;

	@Before
	public void before() {
		factory = new ConverterFactory();
		gml2kmlConverter = new Gml2KmlConverter();
		factory.addConverter(gml2kmlConverter.getInputMimeType(),
				gml2kmlConverter.getOutputMimeType(), gml2kmlConverter);
		gml3kmlConverter = new Gml3KmlConverter();
		factory.addConverter(gml3kmlConverter.getInputMimeType(),
				gml3kmlConverter.getOutputMimeType(), gml3kmlConverter);

		gml2shpConverter = new Gml2ShapeConverter();
		factory.addConverter(gml2shpConverter.getInputMimeType(),
				gml2shpConverter.getOutputMimeType(), gml2shpConverter);
		gml3shpConverter = new Gml3ShapeConverter();
		factory.addConverter(gml3shpConverter.getInputMimeType(),
				gml3shpConverter.getOutputMimeType(), gml3shpConverter);
	}

	@Test
	public void testgml2kml() {

		Convert converter = factory.getConverter(
				gml2kmlConverter.getInputMimeType(),
				gml2kmlConverter.getOutputMimeType());

		Assert.assertFalse("Found no converter", converter == null);
		Assert.assertTrue("Expected gml2->kml converter",
				converter instanceof Gml2KmlConverter);
		Assert.assertFalse("Not expected a gml3->kml converter",
				converter instanceof Gml3KmlConverter);
		Assert.assertFalse("Not expected a shape converter",
				converter instanceof Gml2ShapeConverter);

	}

	@Test
	public void testgml3kml() {

		Convert converter = factory.getConverter(
				gml3kmlConverter.getInputMimeType(),
				gml3kmlConverter.getOutputMimeType());

		Assert.assertFalse("Found no converter", converter == null);
		Assert.assertTrue("Expected gml3->kml converter",
				converter instanceof Gml3KmlConverter);
		Assert.assertTrue("Expected gml2->kml converter",
				converter instanceof Gml2KmlConverter);
		Assert.assertFalse("Not expected a shape converter",
				converter instanceof Gml2ShapeConverter);

	}

	@Test
	public void testgml3shp() {

		Convert converter = factory.getConverter(
				gml3shpConverter.getInputMimeType(),
				gml3shpConverter.getOutputMimeType());

		Assert.assertFalse("Found no converter", converter == null);
		Assert.assertTrue("Expected gml3->shp converter",
				converter instanceof Gml3ShapeConverter);
		Assert.assertTrue("Expected gml2->shp converter",
				converter instanceof Gml2ShapeConverter);
		Assert.assertFalse("Not expected a kml converter",
				converter instanceof Gml2KmlConverter);

	}

	@Test
	public void testnone() {

		Convert converter = factory.getConverter("mimetype1", "mimetype2");

		Assert.assertTrue("Found a converter", converter == null);
	}

}
