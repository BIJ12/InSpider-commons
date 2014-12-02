package nl.idgis.commons.mvc.config;

import org.springframework.web.servlet.View;

/**
 * Instances of this bean are added as a "default" in the content negotiating
 * view resolver created using @EnableViewResolvers. Views are selected based
 * on the requested content type.
 */
public class DefaultView {

	private final View view;
	
	public DefaultView (final View view) {
		if (view == null) {
			throw new NullPointerException ("view cannot be null");
		}
		this.view = view;
	}
	
	public View getView () {
		return view;
	}
}
