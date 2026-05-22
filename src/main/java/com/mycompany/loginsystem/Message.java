/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginsystem;

/**
 *
 * @author Student
 */
import java.util.Random;


public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
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
        messageID = String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    // Checks recipient cell formatting based on international rules
    public String checkRecipientCell() {
        if (recipient.startsWith("+") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public void createMessageHash() {
        String firstTwo;
        if (message.length() >= 2) {
            firstTwo = message.substring(0, 2).toUpperCase();
        } else {
            firstTwo = message.toUpperCase();
        }

        // Extracts the last word without external array dependencies
        String clean = message.trim();
        int lastSpace = clean.lastIndexOf(" ");
        String lastWord = (lastSpace == -1) ? clean : clean.substring(lastSpace + 1);

        messageHash = messageNumber + ":" + firstTwo + ":" + lastWord.toUpperCase();
    }

    public String storeMessage() {
        return "{\n" +
               "  \"messageId\": \"" + messageID + "\",\n" +
               "  \"phone\": \"" + recipient + "\",\n" +
               "  \"text\": \"" + message + "\",\n" +
               "  \"hash\": \"" + messageHash + "\"\n" +
               "}";
    }

    public void printMessage() {
        System.out.println("\n===== MESSAGE DETAILS =====");
        System.out.println("Message ID: " + messageID);
        System.out.println("Message Hash: " + messageHash);
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + message);
    }
}
