package nl.idgis.commons.velocity.tools;

import java.util.ArrayList;
import java.util.List;

public class StringTool {

	public List<String> singleQuoatanizeStringList (final List<String> entries) {
		if (entries == null) {
			return null;
		}
		
		final List<String> result = new ArrayList<String> ();
		for (final String s: entries) {
			result.add ("'" + s + "'");
		}
		
		return result;
	}
}
