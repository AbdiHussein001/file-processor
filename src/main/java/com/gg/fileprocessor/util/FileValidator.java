package com.gg.fileprocessor.util;

public class FileValidator {

    public static boolean isValid(String[] parts) {
        try {
            Double.parseDouble(parts[5]);
            Double.parseDouble(parts[6]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
