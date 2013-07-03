package com.example.rest.jaxrsutils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Utility class for manipulating date and time
 * 
 * @author DoDo <dopoljak@gmail.com>
 */
public class Hashs
{
    /**
    * Return SHA2-256 hash from input byte array
    */
    public static byte[] getSHA256(byte[] input) throws NoSuchAlgorithmException
    {
	MessageDigest md = MessageDigest.getInstance("SHA-256");
	md.update(input);
	return md.digest();
    }
}
