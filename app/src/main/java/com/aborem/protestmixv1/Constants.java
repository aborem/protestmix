package com.aborem.protestmixv1;

public class Constants {
    // todo eventually change to authenticated user
    public static final String DB_NAME = "protest_mix_db";
    public static final String FORWARD_INDICATOR = "<<FORWARD_MESSAGE>>";
    public static final int FORWARD_INDICATOR_LENGTH = FORWARD_INDICATOR.length();
    public static final String REGEX_PHONE_NUMBER = "^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$";
    public static final String REGEX_CLEAN_NUMBER = "\\+?\\d*";
}
