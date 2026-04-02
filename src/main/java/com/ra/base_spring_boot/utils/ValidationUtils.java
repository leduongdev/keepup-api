package com.ra.base_spring_boot.utils;

import com.ra.base_spring_boot.validation.RegexConstants;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(RegexConstants.EMAIL_REGEX);

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
