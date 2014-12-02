package nl.idgis.commons.velocity.tools;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDateTool {

	private final DateTool tool = new DateTool ();
	private Locale oldLocale;
	private TimeZone oldTimeZone;
	
	@Before
	public void setLocaleAndTimezone () {
		oldLocale = Locale.getDefault ();
		oldTimeZone = TimeZone.getDefault ();
		
		// Set a known default locale and timezone to make the unit tests that use the defaults deterministic:
		Locale.setDefault (Locale.ENGLISH);
		TimeZone.setDefault (TimeZone.getTimeZone ("Europe/Amsterdam"));
	}
	
	@After
	public void resetLocaleAndTimezone () {
		Locale.setDefault (oldLocale);
		TimeZone.setDefault (oldTimeZone);
	}
	
	@Test
	public void testFormatObject () {
		assertNull (tool.format (null));
		assertEquals ("April 25, 2013", tool.format (makeDate (2013, 4, 25)));
		
		// Test other object types:
		assertEquals ("April 25, 2013", tool.format (makeDate (2013, 4, 25).getTime ()));
		
		final Calendar calendar = Calendar.getInstance ();
		calendar.setTimeInMillis (makeDate (2013, 4, 25).getTime ());
		assertEquals ("April 25, 2013", tool.format (calendar));
		
		assertNull (tool.format ("Hello, world!"));
	}

	@Test
	public void testFormatStringObject () {
		assertNull (tool.format (null));
		System.out.println("testFormatStringObject");
		assertEquals ("2013-04-25", tool.format ("yyyy-MM-dd", makeDate (2013, 4, 25)));
	}

	@Test
	public void testFormatStringObjectLocale () {
		assertNull (tool.format (null));
		System.out.println("testFormatStringObjectLocale");
		assertEquals ("2013-04-25", tool.format ("yyyy-MM-dd", makeDate (2013, 4, 25), Locale.ENGLISH));
	}

	@Test
	public void testGetYear () {
		assertNull (tool.getYear (null));
		assertEquals (2013, (int)tool.getYear (makeDate (2013, 4, 25)));
	}

	@Test
	public void testGetMonth () {
		assertNull (tool.getMonth (null));
		assertEquals (3, (int)tool.getMonth (makeDate (2013, 4, 25)));
	}

	@Test
	public void testGetDay () {
		assertNull (tool.getDay (null));
		assertEquals (25, (int)tool.getDay (makeDate (2013, 4, 25)));
	}

	@Test
	public void testGetHour() {
		assertNull (tool.getHour (null));
		assertEquals (13, (int)tool.getHour (makeDate (2013, 4, 25, 13, 14, 15)));
	}

	@Test
	public void testGetMinute() {
		assertNull (tool.getMinute (null));
		assertEquals (14, (int)tool.getMinute (makeDate (2013, 4, 25, 13, 14, 15)));
	}

	@Test
	public void testGetSecond() {
		assertNull (tool.getSecond (null));
		assertEquals (15, (int)tool.getSecond (makeDate (2013, 4, 25, 13, 14, 15)));
	}

	private static Date makeDate (int year, int month, int day) {
		return makeDate (year, month, day, 0, 0, 0);
	}
	
	private static Date makeDate (int year, int month, int day, int hour, int minute, int second) {
		final Calendar calendar = Calendar.getInstance ();
		
		calendar.set (Calendar.YEAR, year);
		calendar.set (Calendar.MONTH, month - 1);
		calendar.set (Calendar.DAY_OF_MONTH, day);
		calendar.set (Calendar.HOUR_OF_DAY, hour);
		calendar.set (Calendar.MINUTE, minute);
		calendar.set (Calendar.SECOND, second);
		
		return new Date (calendar.getTimeInMillis ());
	}
}
