package nl.idgis.commons.velocity.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestStringTool {

	private final StringTool tool = new StringTool ();
	
	@Test
	public void testSingleQuoatanizeStringList() {
		assertNull (tool.singleQuoatanizeStringList (null));
		assertEquals (0, tool.singleQuoatanizeStringList (new ArrayList<String> ()).size ());
		
		final List<String> result = tool.singleQuoatanizeStringList (Arrays.asList(new String[] { "a", "b", "c" }));
		
		assertEquals (3, result.size ());
		assertEquals ("'a'", result.get (0));
		assertEquals ("'b'", result.get (1));
		assertEquals ("'c'", result.get (2));
	}

}
