/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginsystem;
import java.util.Random;

/**
 *
 * @author Thakgalo Maibela
 */
public final class Message {

    private String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String message;
    private String messageHash;

    // Static tracker for total number of created messages
    public static int totalMessages = 0;

    // Constructor to instantiate a new message object
    public Message(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;

        totalMessages++;
        this.messageNumber = totalMessages;

        generateMessageID();
        createMessageHash();
    }

    // Generates a random 10-digit number for the ID attribute
    private void generateMessageID() {
        Random random = new Random();
        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        messageID = String.valueOf(number).substring(0, 10);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    // Checks recipient cell formatting based on international rules
    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+27") && recipient.length() >= 12 && recipient.length() <= 13) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Creates Message Hash in format: first2ofID:messageNumber:firstWord:lastWord (ALL CAPS)
    public void createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        
        String trimmedMessage = message.trim();
        String[] words = trimmedMessage.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        
        String hash = firstTwo + ":" + messageNumber + ":" + firstWord + ":" + lastWord;
        messageHash = hash.toUpperCase();
    }
    
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public String getMessageText() {
        return message;
    }
    
    // FIXED: Returns the actual message, not an exception
    public String getMessage() {
        return message;
    }
    
    public int getMessageNumber() {
        return messageNumber;
    }

    // Returns JSON representation of the message for file storage
    public String storeMessage() {
        return "{\n" +
               "  \"messageId\": \"" + messageID + "\",\n" +
               "  \"messageNumber\": " + messageNumber + ",\n" +
               "  \"phone\": \"" + recipient + "\",\n" +
               "  \"text\": \"" + escapeJson(message) + "\",\n" +
               "  \"hash\": \"" + messageHash + "\"\n" +
               "}";
    }
    
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }

    public void printMessage(String loggedInUsername) {
        System.out.println("\n===== MESSAGE DETAILS =====");
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);
    }
    
    public void displayFullDetails() {
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Number: " + messageNumber);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message Text: " + message);
    }
}