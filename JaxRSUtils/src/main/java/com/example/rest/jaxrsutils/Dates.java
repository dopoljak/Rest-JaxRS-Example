package com.example.rest.jaxrsutils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for manipulating date and time
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class Dates
{
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String SHORT_DATE_FORMAT = "HH:mm:ss:SSS";

    /**
     * Get formated date
     */
    public static String formatDate(String format, long date)
    {
	SimpleDateFormat sdf = new SimpleDateFormat(format);
	return sdf.format(date);
    }

    /**
     * Get formated date
     */
    public static String formatDate(String format, Date date)
    {
	SimpleDateFormat sdf = new SimpleDateFormat(format);
	return sdf.format(date);
    }

    /**
     * Get current date
     */
    public static String now()
    {
	SimpleDateFormat sdf = new SimpleDateFormat(LONG_DATE_FORMAT);
	return sdf.format(System.currentTimeMillis());
    }

    /**
     * Format input milliseconds to: milliseconds, seconds, minutes, hours and
     * days
     */
    public static String formatElapsed(long milliseconds)
    {
	long milisec = milliseconds % 1000;
	long seconds = (milliseconds / 1000) % 60;
	long minutes = (milliseconds / 60000) % 60;
	long hours = (milliseconds / 3600000) % 24;
	long days = milliseconds / 86400000;

	final StringBuilder sb = new StringBuilder("[");
	if (days > 0)
	    sb.append(days).append("d ");
	if (hours > 0)
	    sb.append(hours).append("h ");
	if (minutes > 0)
	    sb.append(minutes).append("m ");
	if (seconds > 0)
	    sb.append(seconds).append("s ");
	if (milisec >= 0)
	    sb.append(milisec).append("ms");

	sb.append("]");

	return sb.toString();
    }
}
