package com.ags.vr.utils;

public class Hash
{
    /**
     * Takes n many strings, concatenates them, strips of special characters, then returns hash from inputs.
     * @param string 1-n String inputs
     * @return Integer unique hash code
     */
    public static int StringHash(String... string)
    {
        // concatenate all strings
        String raw = String.join("",string);

        // Removes all punctuation, white space, and converts string to lowercase then returns hash
        return raw.toLowerCase().strip().replaceAll("[^a-zA-Z0-9]","").hashCode();
    }
}
