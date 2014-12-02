package nl.idgis.commons.cli;

import org.apache.commons.cli.Options;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.Assert.assertEquals;

public class CLIApplicationContextTest {
	
	static class ArgumentBean<T> {
		
		final T argument;
		
		ArgumentBean(T argument) {
			this.argument = argument;
		}
		
		T getArgument() {
			return argument;
		}
	}
	
	@Configuration
	static class Config {
		
		@Bean
		public ArgumentBean<String> stringArgument(final @Value("${argument.stringTest:default}") String stringArgument) {
			return new ArgumentBean<String>(stringArgument);
		}
		
		@Bean
		public ArgumentBean<Boolean> booleanArgument(final @Value("${argument.booleanTest}") Boolean booleanArgument) {
			return new ArgumentBean<Boolean>(booleanArgument);
		}
	}
	
	Options options = new Options()
		.addOption("st", "stringTest", true, "string test description")
		.addOption("bt", "booleanTest", false, "boolean test description");
	

	@Test
	public void testDefault() throws Exception {		
		CLIApplicationContext context = new CLIApplicationContext(options, new String[0], Config.class);		
		assertEquals("default", context.getBean("stringArgument", ArgumentBean.class).getArgument());
		context.close();
	}
	
	@Test
	public void testString() throws Exception {		
		CLIApplicationContext context = new CLIApplicationContext(options, new String[]{"-st", "myArgument"}, Config.class);		
		assertEquals("myArgument", context.getBean("stringArgument", ArgumentBean.class).getArgument());
		context.close();
	}
	
	@Test
	public void testBoolean() throws Exception {
		CLIApplicationContext context = new CLIApplicationContext(options, new String[0], Config.class);
		assertEquals(false, context.getBean("booleanArgument", ArgumentBean.class).getArgument());		
		context.close();
		
		context = new CLIApplicationContext(options, new String[]{"-bt"}, Config.class);
		assertEquals(true, context.getBean("booleanArgument", ArgumentBean.class).getArgument());
		context.close();
	}
}
