package com.sl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luhai
 * @Date 2020/12/24
 **/
public class CheckFormat {
    public static String EMAIL_REGEX="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static String PHONE_REGEX="^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public static boolean isEmail(String email){
        Pattern regex = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    public static boolean isPhone(String phone){
        Pattern regex = Pattern.compile(PHONE_REGEX);
        Matcher matcher = regex.matcher(phone);
        return matcher.matches();
    }
}