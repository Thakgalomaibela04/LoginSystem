/*
 * Login System
 */
package com.mycompany.loginsystem;

/**
 *
 * @author Thakgalo Maibela
 */
public class Login {

    // Internal fields to store credentials upon successful registration
    private static String registeredUsername = "";
    private static String registeredPassword = "";
    private static String registeredFirstName = "";
    private static String registeredLastName = "";
    private static String registeredPhone = "";

    // Getter methods
    public static String getRegisteredUsername() {
        return registeredUsername;
    }

    public static String getRegisteredFirstName() {
        return registeredFirstName;
    }

    public static String getRegisteredLastName() {
        return registeredLastName;
    }

    // Username validation: must contain '_' and be <= 5 characters
    public static boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Password validation: >=8 chars, 1 uppercase, 1 digit, 1 special char
    public static boolean checkPasswordComplexity(String password) {
        if (password == null) return false;

        String capital = ".*[A-Z].*";
        String digit = ".*\\d.*";
        String special = ".*[!@#$%^&*(),.?\":{}|<>].*";

        return password.length() >= 8
                && password.matches(capital)
                && password.matches(digit)
                && password.matches(special);
    }

    // South African phone number validation
    public static boolean checkCellPhoneNumber(String phone) {
        if (phone == null || phone.length() < 4) return false;
        return phone.startsWith("+27") && phone.length() >= 12 && phone.length() <= 13;
    }

    // Main registration method
    public static String registerUser(String username, String password, String phone, String firstName, String lastName) {
        if (checkUserName(username) && checkPasswordComplexity(password) && checkCellPhoneNumber(phone)) {
            registeredUsername = username;
            registeredPassword = password;
            registeredPhone = phone;
            registeredFirstName = firstName;
            registeredLastName = lastName;
            return "User is successfully registered";
        } else {
            String errorMsg = "User registration failed!!!!!\n";
            if (!checkUserName(username)) {
                errorMsg += "- Username must contain '_' and be no more than 5 characters.\n";
            }
            if (!checkPasswordComplexity(password)) {
                errorMsg += "- Password must be at least 8 characters, contain a capital letter, a number, and a special character.\n";
            }
            if (!checkCellPhoneNumber(phone)) {
                errorMsg += "- Cell phone number must start with +27 and be valid.\n";
            }
            return errorMsg;
        }
    }

    // Login status method (used by tests)
    public static String returnLoginStatus(String username, String password) {
        if (username != null && username.equals(registeredUsername)
                && password != null && password.equals(registeredPassword)) {
            return "A successful login";
        } else {
            return "A failed login";
        }
    }

    // Alternative login method (returns boolean)
    public boolean loginUser(String username, String password) {
        if (username != null && username.equals(registeredUsername)
                && password != null && password.equals(registeredPassword)) {
            return true;
        }
        return false;
    }
}