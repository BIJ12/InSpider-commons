package nl.idgis.commons.cli;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

public class CLIApplicationContext extends AnnotationConfigApplicationContext {

	private final String[] commandLineArguments;
	private final CommandLine commandLine;
	private final Options options;
	private final String commandLineSyntax;
	private final String header;
	private final String footer;
	
	@Configuration
	public static class Config {
		@Bean
		@Inject
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer (final Environment environment) {
			final PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer ();
			c.setEnvironment (environment);
			return c;
		}
	}
	
	public static class Arguments {
		public final String[] arguments;
		public final Options options;
		public final String commandLineSyntax;
		public final String header;
		public final String footer;
		public final CommandLine commandLine;
		
		public Arguments (final String[] arguments, final Options options, final String commandLineSyntax, final String header, final String footer, final CommandLine commandLine) {
			this.arguments = arguments;
			this.options = options;
			this.commandLineSyntax = commandLineSyntax;
			this.header = header;
			this.footer = footer;
			this.commandLine = commandLine;
		}
	}
	
	public static class CommandLineParseException extends Exception {
		private static final long serialVersionUID = -7778899478467278950L;
		
		public final Options options;
		public final String commandLineSyntax;
		public final String header;
		public final String footer;
		
		public CommandLineParseException (final Options options, final String commandLineSyntax, final String header, final String footer, final ParseException cause) {
			super (cause);
			
			this.options = options;
			this.commandLineSyntax = commandLineSyntax;
			this.header = header;
			this.footer = footer;
		}
		
		public void printHelp () {
			final HelpFormatter formatter = new HelpFormatter ();
			
			formatter.printHelp (commandLineSyntax, header, options, footer);
		}
	}
	
	public static class CommandLinePropertySource extends PropertySource<CommandLine> {
		
		public static final String defaultName = "cli";
		
		private final CommandLine commandLine;
		private final Options options;
		
		public CommandLinePropertySource (final String name, final CommandLine commandLine, final Options options) {
			super (name, commandLine);
			
			if (commandLine == null) {
				throw new NullPointerException ("commandLine cannot be null");
			}
			
			this.commandLine = commandLine;
			this.options = options;
		}
		
		public CommandLinePropertySource (final CommandLine commandLine, final Options options) {
			this (defaultName, commandLine, options);
		}
		
		@Override
		public Object getProperty (final String name) {
			if (!name.startsWith ("argument.")) {
				return null;
			}
			
			final String optionName = name.substring (9);
			final String value = commandLine.getOptionValue (optionName);
			
			if (value == null) {
				Option option = options.getOption (optionName);
				if (option != null && !option.hasArg()) {
					return commandLine.hasOption (optionName);
				} 
			}

			return value;
		}
	}
	
	public CLIApplicationContext (final String[] commandLine) throws CommandLineParseException {
		this (new Options (), commandLine);
	}
	
	public CLIApplicationContext (final Options options, final String[] commandLine) throws CommandLineParseException {
		this (options, null, commandLine);
	}
	
	public CLIApplicationContext (final Options options, final String commandLineSyntax, final String[] commandLine) throws CommandLineParseException {
		this (options, commandLineSyntax, null, commandLine);
	}
	
	public CLIApplicationContext (final Options options, final String commandLineSyntax, final String header, final String[] commandLine) throws CommandLineParseException {
		this (options, commandLineSyntax, header, null, commandLine, new Class<?>[0]);
	}
	
	public CLIApplicationContext (final String[] commandLine, final Class<?> ... configurationClasses) throws CommandLineParseException {
		this (new Options (), commandLine, configurationClasses);
	}
	
	public CLIApplicationContext (final Options options, final String[] commandLine, final Class<?> ... configurationClasses) throws CommandLineParseException {
		this (options, null, commandLine, configurationClasses);
	}
	
	public CLIApplicationContext (final Options options, final String commandLineSyntax, final String[] commandLine, final Class<?> ... configurationClasses) throws CommandLineParseException {
		this (options, commandLineSyntax, null, commandLine, configurationClasses);
	}
	
	public CLIApplicationContext (final Options options, final String commandLineSyntax, final String header, final String[] commandLine, final Class<?> ... configurationClasses) throws CommandLineParseException {
		this (options, commandLineSyntax, header, null, commandLine, configurationClasses);
	}
	
	public CLIApplicationContext (final Options options, final String commandLineSyntax, final String header, final String footer, final String[] commandLine, final Class<?> ... configurationClasses) throws CommandLineParseException {
		super ();
		
		if (commandLine == null) {
			throw new NullPointerException ("commandLine cannot be null");
		}
		
		if (options == null) {
			throw new NullPointerException ("options cannot be null");
		}
		
		final Class<?>[] expandedConfigurationClasses = concatenate (new Class<?>[] { Config.class }, configurationClasses);
		
		this.commandLineArguments = Arrays.copyOf (commandLine, commandLine.length);
		this.options = options;
		this.commandLineSyntax = commandLineSyntax;
		this.header = header;
		this.footer = footer;
		try {
			this.commandLine = new PosixParser ().parse (options, commandLineArguments);
		} catch (ParseException e) {
			throw new CommandLineParseException (options, this.commandLineSyntax, this.header, this.footer, e);
		}
		
		// Register the property source:
		getEnvironment ()
			.getPropertySources ()
			.addFirst (new CommandLinePropertySource (this.commandLine, options));
		
		// Register the annotated configuration classes:
		register (expandedConfigurationClasses);

		// Register the arguments as a bean definition:
		final BeanDefinition argumentsBeanDefinition = BeanDefinitionBuilder
				.rootBeanDefinition (Arguments.class)
				.addConstructorArgValue (commandLineArguments)
				.addConstructorArgValue (options)
				.addConstructorArgValue (commandLineSyntax)
				.addConstructorArgValue (header)
				.addConstructorArgValue (footer)
				.addConstructorArgValue (this.commandLine)
				.getBeanDefinition ();
		
		registerBeanDefinition ("arguments", argumentsBeanDefinition);
		
		// Refresh the context to process configuration classes:
		refresh ();
	}
	
	public int run () {
		return getBean (CLIRunnable.class).run ();
	}
	
	private static <T> T[] concatenate (final T[] a, final T[] b) {
		final T[] result = Arrays.copyOf (a, a.length + b.length);
		
		for (int i = 0; i < b.length; ++ i) {
			result[a.length + i] = b[i];
		}
		
		return result;
	}
	
	public Options getOptions () {
		return options;
	}
}
