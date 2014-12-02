package nl.idgis.commons.mvc.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.idgis.commons.mvc.config.JSONViewResolverConfiguration;

import org.springframework.context.annotation.Import;

/**
 * Add this annotation to a configuration class to create a default JSON view. The
 * view has the content type "application/json" and uses the Jackson mapper to
 * serialize the model. Can be used in combination with @ResponseBody to serialize
 * specific beans.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.TYPE)
@Documented
@Import (JSONViewResolverConfiguration.class)
public @interface EnableJSONViewResolver {

	/**
	 * When set to true, the generated JSON is prefixed with "{} && ".
	 */
	boolean prefixJson () default false;
}
