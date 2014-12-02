package nl.idgis.commons.mvc.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.idgis.commons.mvc.config.VelocityViewResolverConfiguration;

import org.springframework.context.annotation.Import;

/**
 * Add this annotation to a configuration class to instantiate a default
 * velocity view resolver. Creates a velocity view resolver and a velocity configurer bean.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.TYPE)
@Documented
@Import (VelocityViewResolverConfiguration.class)
public @interface EnableVelocityViewResolver {

	public final static String NO_VELOCITY_MACRO_LIBRARY = "";
	
	/**
	 * URL to the default layout, can be overridden by setting the "layout" property on the model.
	 */
	public String layoutUrl () default "layouts/layout.vm";
	
	/**
	 * Enables or disables caching of views.
	 */
	public boolean cache () default true;

	/**
	 * The base path where views are loaded. Default is "/WEB-INF/views/".
	 */
	public String resourceLoaderPath () default "/WEB-INF/views/";
	
	/**
	 * Location of a library of velocity macros. Relative to the resource loader path.
	 */
	public String velocityMacroLibrary () default "velocity-macros.vm";
	
	/**
	 * When true, the velocity macro is reloaded each time a view is (re-)loaded.
	 */
	public boolean reloadVelocityMacroLibrary () default true; 
}
