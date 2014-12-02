package nl.idgis.commons.mvc.config;

import javax.inject.Inject;
import javax.inject.Named;

import nl.idgis.commons.mvc.config.annotation.EnableJSONViewResolver;
import nl.idgis.commons.mvc.config.annotation.EnableViewResolvers;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

@Configuration
@EnableViewResolvers
public class JSONViewResolverConfiguration implements ImportAware {

	private AnnotationAttributes jsonAttributes;

	@Bean
	@Inject
	public DefaultView jsonView (final @Named ("mappingJacksonJsonView") MappingJacksonJsonView view) {
		return new DefaultView (view);
	}
	
	@Bean
	public MappingJacksonJsonView mappingJacksonJsonView () {
		final MappingJacksonJsonView jsonView = new MappingJacksonJsonView ();
		
		jsonView.setPrefixJson (jsonAttributes.getBoolean ("prefixJson"));
		
		return jsonView;
	}

	@Override
	public void setImportMetadata (final AnnotationMetadata importMetadata) {
		this.jsonAttributes = AnnotationAttributes.fromMap (
			importMetadata.getAnnotationAttributes (EnableJSONViewResolver.class.getName(), false));
		
		Assert.notNull(this.jsonAttributes,
				"@EnableJSONViewResolver is not present on importing class " +
				importMetadata.getClassName());
	}
}
