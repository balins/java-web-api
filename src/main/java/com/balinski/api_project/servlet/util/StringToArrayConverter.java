package com.balinski.api_project.servlet.util;

public class StringToArrayConverter {
    public static int[] toIntArray(String arr) {
        String[] params = arr.split(",");
        int[] converted = new int[params.length];

        for(int i = 0; i < params.length; i++)
            converted[i] = Integer.parseInt(params[i]);

        return converted;
    }
    public static String[] toStringArray(String arr) {
        return arr.split(",");
    }

}
