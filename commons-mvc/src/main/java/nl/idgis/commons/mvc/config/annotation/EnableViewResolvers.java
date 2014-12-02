package nl.idgis.commons.mvc.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nl.idgis.commons.mvc.config.ViewResolversConfiguration;

import org.springframework.context.annotation.Import;

/**
 * Use this annotation on a @Configuration class to enable view resolver functionality.
 * A content negotiating view resolver is created that can be combined with a velocity
 * view resolver, a JSON view resolver or both (@EnableVelocityViewResolver, @EnableJSONViewResolver).
 * 
 * This annotation only needs to be present if neither the velocity or JSON annotations are 
 * used, it is used when custom view resolvers should be added.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (ElementType.TYPE)
@Documented
@Import (ViewResolversConfiguration.class)
public @interface EnableViewResolvers {

}
