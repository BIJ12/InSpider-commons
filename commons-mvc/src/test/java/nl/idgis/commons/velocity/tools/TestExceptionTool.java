package nl.idgis.commons.velocity.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestExceptionTool {

	private final ExceptionTool tool = new ExceptionTool ();
	
	@Test
	public void testGetFullStackTrace () {
		assertNull (tool.getFullStackTrace (null));
		assertNotNull (tool.getFullStackTrace (new Exception ()));
	}
}
