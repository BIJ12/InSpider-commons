package nl.idgis.commons.mvc.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import nl.idgis.commons.mvc.ToolLayoutVelocityView;
import nl.idgis.commons.mvc.ToolVelocityLayoutViewResolver;
import nl.idgis.commons.mvc.config.annotation.EnableVelocityViewResolver;
import nl.idgis.commons.mvc.config.annotation.EnableViewResolvers;
import nl.idgis.commons.mvc.config.annotation.VelocityViewConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;

@Configuration
@EnableViewResolvers
public class VelocityViewResolverConfiguration implements ImportAware {

	private AnnotationAttributes velocityAttributes;
	
	@Autowired (required = false)
	private List<VelocityViewConfiguration> configuration;

	@Bean
	@Autowired
	public DefaultViewResolver defaultVelocityViewResolver (final @Named ("velocityViewResolver") ToolVelocityLayoutViewResolver viewResolver) {
		return new DefaultViewResolver (viewResolver);
	}
	
	@Bean
	public ToolVelocityLayoutViewResolver velocityViewResolver () {
		final ToolVelocityLayoutViewResolver viewResolver = new ToolVelocityLayoutViewResolver ();
		
		// Set view properties:
		viewResolver.setLayoutKey ("layout");
		viewResolver.setLayoutUrl (velocityAttributes.getString ("layoutUrl"));
		viewResolver.setScreenContentKey ("screenContent");
		viewResolver.setCache (velocityAttributes.getBoolean ("cache"));
		viewResolver.setPrefix ("");
		viewResolver.setSuffix (".vm");
		viewResolver.setViewClass (ToolLayoutVelocityView.class);
		
		// Set default model properties:
		if (configuration != null && configuration.size () > 0) {
			final Map<String, Object> attributes = new HashMap<String, Object> ();
			
			for (final VelocityViewConfiguration conf: configuration) {
				final Map<String, Object> attrs = conf.getAttributes ();
				if (attrs != null) {
					attributes.putAll (attrs);
				}
			}
			
			viewResolver.setAttributesMap (attributes);
		}
		
		return viewResolver;
	}

	@Bean
	public VelocityConfigurer velocityConfig () {
		final VelocityConfigurer config = new VelocityConfigurer ();
		final Map<String, Object> properties = new HashMap<String, Object> ();
		
		config.setResourceLoaderPath (velocityAttributes.getString ("resourceLoaderPath"));
		
		if (!velocityAttributes.getString ("velocityMacroLibrary").isEmpty()) {
			properties.put ("velocimacro.library", velocityAttributes.getString ("velocityMacroLibrary"));
		}
		properties.put ("velocimacro.library.autoreload", velocityAttributes.getBoolean ("reloadVelocityMacroLibrary"));
		
		config.setVelocityPropertiesMap (properties);
		
		return config;
	}
	
	@Override
	public void setImportMetadata(AnnotationMetadata importMetadata) {
		this.velocityAttributes = AnnotationAttributes.fromMap (
				importMetadata.getAnnotationAttributes (EnableVelocityViewResolver.class.getName(), false));
			
		Assert.notNull(this.velocityAttributes,
				"@EnableVelocityViewResolver is not present on importing class " +
				importMetadata.getClassName());
	}
}
