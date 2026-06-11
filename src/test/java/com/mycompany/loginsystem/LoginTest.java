/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.loginsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class LoginTest {
    
    public LoginTest() {
    }
  

    @Test
    public void testCheckUserName() {
        // Test valid username (Exactly 5 chars with underscore)
        assertTrue(Login.checkUserName("kyl_1"));
        
        // Test invalid username (Too long)
        assertFalse(Login.checkUserName("kyle_smith"));
    }

    @Test
    public void testCheckPasswordComplexity() {
        // Test valid complex password
        assertTrue(Login.checkPasswordComplexity("Ch&&se@ke99!"));
        
        // Test invalid password (No special character)
        assertFalse(Login.checkPasswordComplexity("Password123"));
    }

    @Test
    public void testCheckCellPhoneNumber() {
        // Test valid SA phone number
        assertTrue(Login.checkCellPhoneNumber("+27838968976"));
        
        // Test invalid phone number (Incorrect country code)
        assertFalse(Login.checkCellPhoneNumber("0838968976"));
    }

    @Test
    public void testRegisterUser() {
        // Test successful registration message
        String expected = "User is successfully registered";
        String actual = Login.registerUser("kyl_1", "Ch&&se@ke99!", "+27838968976");
        assertEquals(expected, actual);
    }

    @Test
    public void testReturnLoginStatus() {
        // Prepare the class state by registering the user first
        Login.registerUser("kyl_1", "Ch&&se@ke99!", "+27838968976");
        
        // Test successful login message
        String expected = "A successful login";
        String actual = Login.returnLoginStatus("kyl_1", "Ch&&se@ke99!");
        assertEquals(expected, actual);
    }
}