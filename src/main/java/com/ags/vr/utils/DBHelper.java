package com.ags.vr.utils;

/**
 * Class for database helper utilities.
 */
public class DBHelper
{
    /**
     * Takes n many strings, concatenates them, strips of special characters, then returns hash from inputs.
     * @param string 1-n String inputs
     * @return Integer unique hash code
     */
    public static int StringHash(String... string)
    {
        // Concatenate all strings
        StringBuilder raw = new StringBuilder();
        for (String s : string)
        {
            raw.append(s);
        }

        // Removes all punctuation, white space, and converts string to lowercase then returns hash
        return raw.toString().toLowerCase().strip().replaceAll("[^a-zA-Z ]","").hashCode();
    }
}
