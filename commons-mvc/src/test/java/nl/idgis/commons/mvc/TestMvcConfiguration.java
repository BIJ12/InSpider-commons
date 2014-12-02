package nl.idgis.commons.mvc;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import nl.idgis.commons.mvc.config.annotation.EnableJSONViewResolver;
import nl.idgis.commons.mvc.config.annotation.EnableVelocityViewResolver;
import nl.idgis.commons.mvc.config.annotation.EnableViewResolvers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestMvcConfiguration.TestMvcConfigurationConfig.class)
public class TestMvcConfiguration {

	@Configuration
	@EnableViewResolvers
	@EnableVelocityViewResolver (velocityMacroLibrary = EnableVelocityViewResolver.NO_VELOCITY_MACRO_LIBRARY)
	@EnableJSONViewResolver
	public static class TestMvcConfigurationConfig {
		
		/**
		 * This bean is normally created by EnableWebMvc and is required by the view resolver configuration.
		 * We don't bootstrap Spring webmvc, therefore a content negotiation manager needs to be created.
		 */
		public @Bean ContentNegotiationManagerFactoryBean mvcContentNegotiationManager() {
			final ContentNegotiationManagerFactoryBean factoryBean = new ContentNegotiationManagerFactoryBean ();
			
			return factoryBean;
		}
	}
	
	@Inject
	private ContentNegotiatingViewResolver contentNegotiatingViewResolver;
	
	@Test
	public void test() {
		assertNotNull (contentNegotiatingViewResolver);
	}
}
