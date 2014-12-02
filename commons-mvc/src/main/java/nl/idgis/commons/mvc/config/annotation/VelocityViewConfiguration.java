package nl.idgis.commons.mvc.config.annotation;

import java.util.Map;

/**
 * Additional configuration when enabling the velocity view resolver using @EnableVelocityViewResolver.
 * In order to use this configuration, the Configuration class should implement this interface.
 */
public interface VelocityViewConfiguration {

	/**
	 * Returns a map of default attributes that are added to the model of every
	 * Velocity view that is created by the velocity view resolver.
	 * 
	 * @return A map of default attributes, or null if no default attributes
	 * should be added to the model.
	 */
	Map<String, Object> getAttributes ();
}
