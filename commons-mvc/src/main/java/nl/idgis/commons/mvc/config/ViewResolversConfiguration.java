package nl.idgis.commons.mvc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@Configuration
public class ViewResolversConfiguration extends WebMvcConfigurerAdapter {
	
	@Autowired(required=false)
	private List<DefaultView> defaultViews;
	
	@Autowired(required=false)
	private List<DefaultViewResolver> defaultViewResolvers;

	@Bean
	@Inject
	public ContentNegotiatingViewResolver contentNegotiatingViewResolver (
			final ContentNegotiationManager contentNegotiationManager) {
		final ContentNegotiatingViewResolver viewResolver = new ContentNegotiatingViewResolver ();

		// Set content types:
		viewResolver.setContentNegotiationManager (contentNegotiationManager);

		// Set view resolvers:
		if (defaultViewResolvers != null) {
			final List<ViewResolver> viewResolvers = new ArrayList<ViewResolver> ();		
			for (final DefaultViewResolver defaultResolver: defaultViewResolvers) {
				viewResolvers.add (defaultResolver.getViewResolver ());
			}
			viewResolver.setViewResolvers (viewResolvers);
		}
		
		// Set default views:
		if (defaultViews != null) {
			final List<View> views = new ArrayList<View> ();
			for (final DefaultView defaultView: defaultViews) {
				views.add (defaultView.getView ());
			}
			viewResolver.setDefaultViews (views);
		}

		return viewResolver;
	}
	
	/**
	 * Instantiates a content negotiation manager factory that maps path extensions to concrete content
	 * types. Adding content types here will allow the user to specify the requested content type
	 * by adding an extension (e.g. ".json" or ".html") to the URL).
	 * 
	 * View managers for these content types should be available if the specific content types are
	 * to be requested.
	 * 
	 * @return A factory bean that is used to construct the content negotiation manager.
	 */
	@Override
	public void configureContentNegotiation (final ContentNegotiationConfigurer configurer) {
		configurer
			.favorParameter (false)
			.favorPathExtension (true)
			.ignoreAcceptHeader (false)
			.useJaf (false)
			.mediaTypes (new HashMap<String, MediaType> () {
				private static final long serialVersionUID = 3864854172610771630L;
				{
					put ("html", MediaType.TEXT_HTML);
					put ("json", MediaType.APPLICATION_JSON);
					put ("xml", MediaType.TEXT_XML);
				}
			});
	}
}
