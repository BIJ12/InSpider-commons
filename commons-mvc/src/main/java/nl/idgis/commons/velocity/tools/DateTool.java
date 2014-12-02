package nl.idgis.commons.velocity.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTool {

	public String format (final Object date) {
		final Locale locale = getDefaultLocale ();
		return format (getDefaultFormat (locale), date, locale, getDefaultTimezone ());
	}
	
	public String format (final String format, final Object date) {
		return format (format, date, getDefaultLocale ());
	}
	
	public String format (final String format, final Object date, final Locale locale) {
		return format (getFormat (format, locale), date, locale, getDefaultTimezone ());
	}
	
	public Integer getYear (final Object date) {
		final Calendar calendar = convertToCalendar (date);
		if (calendar == null) {
			return null;
		}
		
		return calendar.get (Calendar.YEAR);
	}
	
	public Integer getMonth (final Object date) {
		final Calendar calendar = convertToCalendar (date);
		if (calendar == null) {
			return null;
		}
		
		return calendar.get (Calendar.MONTH);
	}
	
	public Integer getDay (final Object date) {
		final Calendar calendar = convertToCalendar (date);
		if (calendar == null) {
			return null;
		}
		
		return calendar.get (Calendar.DAY_OF_MONTH);
	}
	
	public Integer getHour (final Object date) {
		final Calendar calendar = convertToCalendar (date);
		if (calendar == null) {
			return null;
		}
		
		return calendar.get (Calendar.HOUR_OF_DAY);
	}
	
	public Integer getMinute (final Object date) {
		final Calendar calendar = convertToCalendar (date);
		if (calendar == null) {
			return null;
		}
		
		return calendar.get (Calendar.MINUTE);
	}
	
	public Integer getSecond (final Object date) {
		final Calendar calendar = convertToCalendar (date);
		if (calendar == null) {
			return null;
		}
		
		return calendar.get (Calendar.SECOND);
	}
	
	private String format (final DateFormat format, final Object obj, final Locale locale, final TimeZone timezone) {
		// Check input:
		if (format == null || obj == null || locale == null || timezone == null) {
			return null;
		}
		
		final Date date = convertToDate (obj);
		if (date == null) {
			return null;
		}

		format.setTimeZone (timezone);
		
		return format.format (date);
	}
	
	private Date convertToDate (final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Date) {
			return (Date)obj;
		} else if (obj instanceof Calendar) {
			return ((Calendar)obj).getTime ();
		} else if (obj instanceof Number) {
			return new Date (((Number)obj).longValue ());
		}
		
		return null;
	}
	
	private Calendar convertToCalendar (final Object obj) {
		if (obj == null) {
			return null;
		} else if (obj instanceof Calendar) {
			return (Calendar)obj;
		}
		
		final Date date = convertToDate (obj);
		if (date == null) {
			return null;
			
		}
		
		final Calendar calendar = Calendar.getInstance (getDefaultTimezone (), getDefaultLocale ());
		
		calendar.setTimeInMillis (date.getTime ());
		
		return calendar;
	}
	
	private DateFormat getDefaultFormat (final Locale locale) {
		if (locale == null) {
			return null;
		} 
		
		try {
			return SimpleDateFormat.getDateInstance (SimpleDateFormat.LONG, locale);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	private DateFormat getFormat (final String format, final Locale locale) {
		if (format == null || locale == null) {
			return null;
		}
		
		try {
			return new SimpleDateFormat (format, locale);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	private Locale getDefaultLocale () {
		return Locale.getDefault ();
	}
	
	private TimeZone getDefaultTimezone () {
		return TimeZone.getDefault ();
	}
}
