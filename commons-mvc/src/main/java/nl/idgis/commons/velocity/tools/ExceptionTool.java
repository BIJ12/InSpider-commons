package nl.idgis.commons.velocity.tools;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ExceptionTool {

	public String getFullStackTrace (final Throwable throwable) {
		if (throwable == null) {
			return null;
		}
		
		return ExceptionUtils.getFullStackTrace (throwable);
	}
}
