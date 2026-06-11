/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginsystem;

/**
 *
 * @author Student
 */
public class Login {

    // Internal fields to store credentials upon successful registration
    private static String registeredUsername = "";
    private static String registeredPassword = "";

    public static String getRegisteredUsername() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Login() {
    }

    // Ensures the username contains an underscore and is exactly 5 characters long
    public static boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() == 5;
    }

    // Uses Regular Expressions (Regex) to validate length and multi-character requirements
    public static boolean checkPasswordComplexity(String password) {
        if (password == null) return false;
        
        String capital = ".*[A-Z].*";
        String small = ".*[a-z].*";
        String special = ".*[!@#$%^&*(),.?\":{}|<>].*";
        String digit = ".*\\d.*";
        
        return password.length() >= 8 
               && password.matches(capital)
               && password.matches(small) 
               && password.matches(digit)
               && password.matches(special);
    }

    // Validates SA phone format conditions (+27 prefix, length, and 4th digit rules)
    public static boolean checkCellPhoneNumber(String phone) {
        if (phone == null || phone.length() < 4) return false;
        
        String saCode = "+27";
        String firstThreeChars = phone.substring(0, 3);
        int fourthDigit = Character.getNumericValue(phone.charAt(3));
        
        return phone.length() <= 12 
               && firstThreeChars.equals(saCode)
               && fourthDigit >= 6 
               && fourthDigit <= 8;
    }

    // Registers user and securely stores their credentials if all conditions pass
    public static String registerUser(String username, String password, String phone) {
        if (checkCellPhoneNumber(phone) && checkUserName(username) && checkPasswordComplexity(password)) {
            registeredUsername = username;
            registeredPassword = password;
            return "User is successfully registered";
        } else {
            return "User registration failed!!!!!";
        }
    }

    // Verifies if login inputs match the saved credentials from registration
    public static String returnLoginStatus(String username, String password) {
        if (username != null && username.equals(registeredUsername) 
            && password != null && password.equals(registeredPassword)) {
            return "A successful login";
        } else {
            return "A failed login";
        }
    }
}
