package com.example.funfood.util;

import android.util.Patterns;

public class ValidationUtil {

    /**
     * Email validation
     */
    public static boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Password validation (min 6 characters)
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Phone validation (Vietnam format)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }

        // Remove spaces and dashes
        String cleanPhone = phone.replaceAll("[\\s-]", "");

        // Vietnam phone: 10-11 digits, starts with 0
        return cleanPhone.matches("^0[0-9]{9,10}$");
    }

    /**
     * Name validation
     */
    public static boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2;
    }

    /**
     * Check if string is empty
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Check if string is not empty
     */
    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    /**
     * Get error message for email
     */
    public static String getEmailError(String email) {
        if (isEmpty(email)) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        return null;
    }

    /**
     * Get error message for password
     */
    public static String getPasswordError(String password) {
        if (isEmpty(password)) {
            return "Password is required";
        }
        if (!isValidPassword(password)) {
            return "Password must be at least 6 characters";
        }
        return null;
    }

    /**
     * Get error message for phone
     */
    public static String getPhoneError(String phone) {
        if (isEmpty(phone)) {
            return "Phone number is required";
        }
        if (!isValidPhone(phone)) {
            return "Invalid phone number format";
        }
        return null;
    }

    /**
     * Get error message for name
     */
    public static String getNameError(String name) {
        if (isEmpty(name)) {
            return "Name is required";
        }
        if (!isValidName(name)) {
            return "Name must be at least 2 characters";
        }
        return null;
    }
}