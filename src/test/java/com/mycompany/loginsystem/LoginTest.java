/*
 * Login System Tests
 */
package com.mycompany.loginsystem;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Thakgalo Maibela
 */
public class LoginTest {

    @Test
    public void testCheckUserName() {
        assertTrue(Login.checkUserName("kyl_1"));
        assertFalse(Login.checkUserName("kyle_smith"));
    }

    @Test
    public void testCheckPasswordComplexity() {
        assertTrue(Login.checkPasswordComplexity("Ch&&se@ke99!"));
        assertFalse(Login.checkPasswordComplexity("Password123"));
    }

    @Test
    public void testCheckCellPhoneNumber() {
        assertTrue(Login.checkCellPhoneNumber("+27838968976"));
        assertFalse(Login.checkCellPhoneNumber("0838968976"));
    }

    @Test
    public void testRegisterUser() {
        String expected = "User is successfully registered";
        // Now passing all 5 parameters
        String actual = Login.registerUser("kyl_1", "Ch&&se@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals(expected, actual);
    }

    @Test
    public void testReturnLoginStatus() {
        // Register user first (with all parameters)
        Login.registerUser("kyl_1", "Ch&&se@ke99!", "+27838968976", "Kyle", "Smith");
        
        String expected = "A successful login";
        String actual = Login.returnLoginStatus("kyl_1", "Ch&&se@ke99!");
        assertEquals(expected, actual);
    }
}