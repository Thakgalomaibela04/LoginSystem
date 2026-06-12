/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.loginsystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Thakgalo Maibela
 */
public class MessageTest {
    
    public MessageTest() {
    }
    
    @Test
    public void testMessageIDValidation() {
        Message msg = new Message("+27838968976", "Testing ID generation length rules");
        
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testRecipientCellValid() {
        Message msg = new Message("+27838968976", "Valid cell phone formatting check");
        String expected = "Cell phone number successfully captured.";
        
        assertEquals(expected, msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCellInvalid() {
        Message msg = new Message("0838968976", "Invalid cell phone format check");
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        
        assertEquals(expected, msg.checkRecipientCell());
    }

    @Test
    public void testHashCompilationStructure() {
        Message msg = new Message("+27838968976", "java application verification workflow");
        
        String layoutContent = msg.storeMessage();
        
        assertTrue(layoutContent.contains("WORKFLOW"));
    }
    
}
