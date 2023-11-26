package com.unigrab.util;

public class Common {
    public static boolean isValidString(String s){
        return s != null && s.trim().length() > 0;
    }
    public static boolean isValidEmail(String p){
        String regexPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return p.matches(regexPattern);
    }
    public static boolean isValidLong(String s){
        return isValidString(s) && Integer.parseInt(s) >= 0;
    }
    public static boolean isValidDouble(String s){
        return isValidString(s) && Double.parseDouble(s) > 0;
    }
    public static boolean isValidPassword(String s){
        return isValidString(s) && s.trim().length() > 5;
    }

}
