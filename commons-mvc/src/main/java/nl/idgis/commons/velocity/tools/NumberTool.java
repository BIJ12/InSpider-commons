package nl.idgis.commons.velocity.tools;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberTool {

	public String format (final Object number) {
		return format (getDefaultNumberFormat (), number, Locale.getDefault ());
	}

	public String format (final String format, final Object number) {
		return format (getNumberFormat (format), number, Locale.getDefault ());
	}
	
	public String format (final String format, final Object number, final Locale locale) {
		return format (getNumberFormat (format, locale), number, locale);
	}

	public String integer (final Object number) {
		return format (getIntegerFormat (), number, Locale.getDefault ());
	}
	
	public String number (final Object number) {
		return format (getNumberNumberFormat (), number, Locale.getDefault ());
	}
	
	private String format (final NumberFormat numberFormat, final Object number, final Locale locale) {
		if (number == null || numberFormat == null || locale == null) {
			return null;
		}
		
		final Number num = convertToNumber (number);
		if (num == null) {
			return null;
		}
		
		try {
			return numberFormat.format (num);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	private Number convertToNumber (final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Number) {
			return ((Number)obj);
		}
		
		return null;
	}
	
	private NumberFormat getDefaultNumberFormat () {
		return NumberFormat.getNumberInstance ();
	}
	
	private NumberFormat getIntegerFormat () {
		return NumberFormat.getIntegerInstance ();
	}
	
	private NumberFormat getNumberNumberFormat () {
		return NumberFormat.getNumberInstance ();
	}
	
	private NumberFormat getNumberFormat (final String format) {
		return getNumberFormat (format, Locale.getDefault ());
	}
	
	private NumberFormat getNumberFormat (final String format, final Locale locale) {
		if (format == null || locale == null) {
			return null;
		}
		
		try {
			return new DecimalFormat (format, new DecimalFormatSymbols (locale));
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
}
