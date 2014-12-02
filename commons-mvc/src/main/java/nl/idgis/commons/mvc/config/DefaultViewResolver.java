package nl.idgis.commons.mvc.config;

import org.springframework.util.Assert;
import org.springframework.web.servlet.ViewResolver;

/**
 * Default view resolvers that are added to the content negotiating view resolver created
 * using @EnableViewResolvers
 */
public class DefaultViewResolver {

	private final ViewResolver viewResolver;
	
	public DefaultViewResolver (final ViewResolver viewResolver) {
		Assert.notNull (viewResolver, "viewResolver cannot be null");
		
		this.viewResolver = viewResolver;
	}
	
	public ViewResolver getViewResolver () {
		return viewResolver;
	}
}
