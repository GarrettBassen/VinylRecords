package com.ags.vr.utils;

public class GeneralUtil {
    /**
     * Uses each character in a string to generate a unique integer representation of that string.
     * @param str The string that will be hashed.
     * @return An integer representation of that string.
     */
    public static int hash(String str)
    {
        //int to be returned
        int hash = 0;

        for(int i = 0; i < str.length(); i++){
            //append hash with character Unicode multiplied by 7
            hash += (str.charAt(i) * 7);
        }

        //return the hash value
        return hash;
    }
}
