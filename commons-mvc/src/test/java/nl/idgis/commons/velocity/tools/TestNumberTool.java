package nl.idgis.commons.velocity.tools;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestNumberTool {

	private final NumberTool tool = new NumberTool ();
	
	private Locale oldLocale;
	
	@Before
	public void setLocale () {
		oldLocale = Locale.getDefault ();
		
		// Set a known default locale and timezone to make the unit tests that use the defaults deterministic:
		Locale.setDefault (Locale.ENGLISH);
	}
	
	@After
	public void resetLocale () {
		Locale.setDefault (oldLocale);
	}
	
	@Test
	public void testFormatObject () {
		assertNull (tool.format (null));
		assertEquals ("42", tool.format (42));
		assertEquals ("42.5", tool.format (42.5));
		assertNull (tool.format ("Hello, world!"));
	}

	@Test
	public void testFormatStringObject () {
		assertNull (tool.format ("00.00", null));
		assertEquals ("42.00", tool.format ("00.00", 42));
		assertEquals ("42.50", tool.format ("00.00", 42.5));
	}

	@Test
	public void testFormatStringObjectLocale () {
		assertNull (tool.format ("00.00", null, Locale.getDefault ()));
		assertEquals ("42.00", tool.format ("00.00", 42, Locale.getDefault ()));
		assertEquals ("42,00", tool.format ("00.00", 42, Locale.FRANCE));
	}

	@Test
	public void testInteger () {
		assertNull (tool.integer (null));
		assertEquals ("42", tool.integer (42));
		assertEquals ("42", tool.integer (42.5));
	}

	@Test
	public void testNumber () {
		assertNull (tool.number (null));
		assertEquals ("42", tool.number (42));
		assertEquals ("42.5", tool.number (42.5));
	}
}
