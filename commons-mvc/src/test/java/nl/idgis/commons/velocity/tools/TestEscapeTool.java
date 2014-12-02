package nl.idgis.commons.velocity.tools;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestEscapeTool {

	private EscapeTool tool = new EscapeTool ();
	
	@Test
	public void testHtml () {
		assertEquals ("", tool.html (null));
		assertEquals ("abc&lt;de&gt;fg", tool.html ("abc<de>fg"));
	}

	@Test
	public void testJavascript () {
		assertEquals ("", tool.javascript (null));
		assertEquals ("abc\\tde\\nfg", tool.javascript ("abc\tde\nfg"));
	}

	@Test
	public void testUrl () {
		assertEquals ("", tool.url (null));
		assertEquals ("abc+de%2Cfg", tool.url ("abc de,fg"));
	}

	@Test
	public void testXml () {
		assertEquals ("", tool.xml (null));
		assertEquals ("abc&lt;de&gt;fg", tool.xml ("abc<de>fg"));
	}

}
