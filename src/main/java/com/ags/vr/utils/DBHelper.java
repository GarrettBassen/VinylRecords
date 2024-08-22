package com.ags.vr.utils;

/**
 * Class for database helper utilities.
 */
public class DBHelper
{
    /**
     * Takes string and returns unique integer hash code.
     * @param str String to hash
     * @return Integer hash code
     */
    public static int StringHash(String str)
    {
        // Removes all punctuation, white space, and converts string to lowercase
        str = str.toLowerCase().strip().replaceAll("[^a-zA-Z ]","");
        return str.hashCode();
    }
}
