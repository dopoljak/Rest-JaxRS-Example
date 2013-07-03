package com.example.rest.jaxrsutils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * String manipulation utils
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class Strings
{
    public static final String NEWLINE = System.getProperty("line.separator");

    private static char[] HEX = "0123456789ABCDEF".toCharArray();

    /**
     * Byte array to HEX String
     */
    public static String toHEX(byte[] input)
    {
	if (input == null)
	{
	    return null;
	}

	int length = input.length;
	char[] output = new char[2 * length];
	for (int i = 0; i < length; ++i)
	{
	    output[2 * i] = HEX[(input[i] & 0xF0) >>> 4];
	    output[2 * i + 1] = HEX[input[i] & 0x0F];
	}
	return new String(output);
    }

    /**
     * Byte arrays to HEX
     */
    public static String toHEX(byte[]... array)
    {
	StringBuilder sb = new StringBuilder();
	for (byte[] bs : array)
	{
	    sb.append(toHEX(bs));
	}
	return sb.toString();
    }

    /**
     * Byte to Hex String
     */
    public static String toHEX(byte input)
    {
	char[] output = new char[2];
	output[0] = HEX[(input & 0xF0) >>> 4];
	output[1] = HEX[input & 0x0F];
	return new String(output);
    }

    /**
     * Hex String to Byte array
     */
    public static byte[] fromHEX(String hexInput)
    {
	if (hexInput == null)
	{
	    return null;
	}

	int length = hexInput.length();
	byte[] output = new byte[length / 2];
	for (int i = 0; i < length; i += 2)
	{
	    output[i / 2] = (byte) ((Character.digit(hexInput.charAt(i), 16) << 4) + Character.digit(hexInput.charAt(i + 1), 16));
	}
	return output;
    }

    /**
     * Check is string empty
     */
    public static boolean isEmpty(String s)
    {
	if (s == null || s.length() == 0)
	    return true;

	return false;
    }

    /**
     * Check i string blank eg: return true if string is - NULL - empty -> "" -
     * blank -> " "
     */
    public static boolean isBlank(String s)
    {
	if (s == null || s.length() == 0 || s.trim().length() == 0)
	    return true;

	return false;
    }

    /**
     * Check if string contains [YES;TRUE;1]
     */
    public static boolean stringToBool(String s)
    {
	if (s == null)
	    return false;

	if (s.equalsIgnoreCase("YES") || s.equalsIgnoreCase("TRUE") || s.equalsIgnoreCase("1"))
	    return true;

	return false;
    }

    /**
     * Append character
     */
    public static String append(String data, char append, int num)
    {
	final int currLen = data.length();
	final StringBuilder sb = new StringBuilder(num);

	for (int i = currLen; i < num; i++)
	{
	    sb.append(append);
	}

	sb.append(data);

	return sb.toString();
    }

    /**
     * Format String using SLF4J
     */
    public static String format(String format, Object obj1, Object obj2, Object obj3)
    {
	FormattingTuple t = MessageFormatter.format(format, new Object[] { obj1, obj2, obj3 });
	return t.getMessage();
    }

    /**
     * Format String using SLF4J
     */
    public static String format(String format, Object obj1, Object obj2)
    {
	FormattingTuple t = MessageFormatter.format(format, obj1, obj2);
	return t.getMessage();
    }

    /**
     * Format String using SLF4J
     */
    public static String format(String format, Object obj1)
    {
	FormattingTuple t = MessageFormatter.format(format, obj1);
	return t.getMessage();
    }

    /**
     * Format String using SLF4J
     */
    public static String format(String format, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5)
    {
	FormattingTuple t = MessageFormatter.format(format, new Object[] { obj1, obj2, obj3, obj4, obj5 });
	return t.getMessage();
    }

    /**
     * Format String using SLF4J
     */
    public static String format(String format, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6)
    {
	FormattingTuple t = MessageFormatter.format(format, new Object[] { obj1, obj2, obj3, obj4, obj5, obj6 });
	return t.getMessage();
    }
}
