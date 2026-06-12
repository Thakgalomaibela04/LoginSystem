/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.loginsystem;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class MessageApp {
    private static final Scanner input = new Scanner(System.in);

    // Required arrays
    private static final ArrayList<Message> sentMessagesList = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessagesList = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIds = new ArrayList<>();

    private static final String JSON_FILE = "messages.json";
    private static String loggedInFirstName = "";
    private static String loggedInLastName = "";

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("     WELCOME TO QUICKCHAT");
        System.out.println("==========================================\n");
        
        // ========== REGISTRATION PHASE ==========
        System.out.println("--- USER REGISTRATION ---");
        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();
        
        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();
        
        System.out.print("Enter Username (must contain '_' and max 5 characters): ");
        String username = input.nextLine();
        
        System.out.print("Enter Password (min 8 chars, 1 capital, 1 number, 1 special): ");
        String password = input.nextLine();
        
        System.out.print("Confirm Password: ");
        String confirmPassword = input.nextLine();
        
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match! Exiting application.");
            return;
        }
        
        System.out.print("Enter Phone Number (+27XXXXXXXXX): ");
        String phone = input.nextLine();

        System.out.println("\n--- VALIDATION RESULTS ---");
        
        if (Login.checkUserName(username)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        if (Login.checkPasswordComplexity(password)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }

        if (Login.checkCellPhoneNumber(phone)) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        String regResult = Login.registerUser(username, password, phone, firstName, lastName);
        System.out.println("\n" + regResult);
        
        if (!regResult.equals("User is successfully registered")) {
            System.out.println("Registration failed. Exiting application.");
            return;
        }

        // ========== LOGIN PHASE ==========
        System.out.println("\n--- LOGIN ---");
        int loginAttempts = 0;
        boolean loggedIn = false;
        
        while (loginAttempts < 3 && !loggedIn) {
            System.out.print("Username: ");
            String loginUser = input.nextLine();
            System.out.print("Password: ");
            String loginPass = input.nextLine();
            
                      Login tempLogin = new Login();
            if (tempLogin.loginUser(loginUser, loginPass)) {
                String loginStatus = Login.returnLoginStatus(loginUser, loginPass);
                System.out.println(loginStatus);
                if (loginStatus.equals("A successful login")) {
                    loggedIn = true;
                    loggedInFirstName = firstName;
                    loggedInLastName = lastName;
                    System.out.println("Welcome " + firstName + " " + lastName + ", it is great to see you again.");
                }
            } else {
                System.out.println("Username or password incorrect, please try again.");
                loginAttempts++;
            }
        }
        
        if (!loggedIn) {
            System.out.println("Too many failed login attempts. Exiting application.");
            return;
        }

        // ========== WELCOME MESSAGE ==========
        System.out.println("\n==========================================");
        System.out.println("Welcome to QuickChat, " + loggedInFirstName + " " + loggedInLastName + "!");
        System.out.println("==========================================");

        loadStoredMessagesFromJson();

        System.out.print("\nHow many messages would you like to send? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(input.nextLine().trim());
            if (numMessages <= 0) {
                System.out.println("Number must be positive. Setting to 1.");
                numMessages = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Setting to 1 message.");
            numMessages = 1;
        }

        // ========== SEND MESSAGES USING FOR LOOP ==========
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- MESSAGE " + i + " of " + numMessages + " ---");
            sendSingleMessage();
        }

        System.out.println("\n==========================================");
        System.out.println("Total number of messages sent: " + Message.totalMessages);
        System.out.println("==========================================");

        // ========== MAIN MENU ==========
        int option;
        do {
            System.out.println("\n===== QUICKCHAT MAIN MENU =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages (Coming Soon)");
            System.out.println("3. Stored Messages (Reports & Management)");
            System.out.println("4. Quit");
            System.out.print("Choice: ");
            try {
                option = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1 -> {
                    System.out.print("How many messages to send? ");
                    int more = Integer.parseInt(input.nextLine().trim());
                    for (int i = 1; i <= more; i++) {
                        System.out.println("\n--- Additional Message " + i + " ---");
                        sendSingleMessage();
                    }
                }
                case 2 -> System.out.println("\nComing Soon.");
                case 3 -> storedMessagesMenu();
                case 4 -> System.out.println("\nThank you for using QuickChat. Goodbye!");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (option != 4);
    }

    private static void sendSingleMessage() {
        System.out.print("Recipient phone number (+27XXXXXXXXX): ");
        String recipient = input.nextLine();
        System.out.print("Message text (max 250 characters): ");
        String messageText = input.nextLine();

        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            System.out.println("Message exceeds 250 characters by " + excess + "; please reduce the size.");
            System.out.println("Message disregarded.");
            disregardedMessages.add(messageText);
            return;
        }
        
        System.out.println("Message ready to send.");

        Message msg = new Message(recipient, messageText);
        
        String recipientCheck = msg.checkRecipientCell();
        System.out.println(recipientCheck);
        
        if (!recipientCheck.contains("successfully")) {
            System.out.println("Message disregarded due to invalid recipient.");
            disregardedMessages.add(messageText);
            return;
        }

        System.out.println("\nOptions:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message to send later");
        System.out.print("Choose an option: ");
        
        int option;
        try {
            option = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            option = 0;
        }

        switch (option) {
            case 1:
                System.out.println("Message successfully sent");
                sentMessagesList.add(msg);
                messageHashes.add(msg.getMessageHash());
                messageIds.add(msg.getMessageID());
                storedMessagesList.add(msg);
                rewriteJsonFile();
                msg.printMessage(loggedInFirstName + " " + loggedInLastName);
                break;
                
            case 2:
                System.out.println("Press 0 to delete the message");
                System.out.println("Message disregarded.");
                disregardedMessages.add(messageText);
                break;
                
            case 3:
                System.out.println("Message successfully stored");
                storedMessagesList.add(msg);
                messageHashes.add(msg.getMessageHash());
                messageIds.add(msg.getMessageID());
                rewriteJsonFile();
                break;
                
            default:
                System.out.println("Invalid option. Message disregarded.");
                disregardedMessages.add(messageText);
                break;
        }
    }

    private static void storedMessagesMenu() {
        String choice;
        do {
            System.out.println("\n=== STORED MESSAGES MENU ===");
            System.out.println("a. Display sender and recipient of all stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message ID and display recipient + message");
            System.out.println("d. Search for all messages stored for a particular recipient");
            System.out.println("e. Delete a message using the message hash");
            System.out.println("f. Display full report of all stored messages");
            System.out.println("g. Back to Main Menu");
            System.out.print("Choice: ");
            choice = input.nextLine().toLowerCase();

            switch (choice) {
                case "a" -> displaySendersAndRecipients();
                case "b" -> displayLongestMessage();
                case "c" -> searchByMessageId();
                case "d" -> searchByRecipient();
                case "e" -> deleteByMessageHash();
                case "f" -> displayFullReport();
                case "g" -> System.out.println("Returning to main menu.");
                default -> System.out.println("Invalid option.");
            }
        } while (!choice.equals("g"));
    }

    private static void displaySendersAndRecipients() {
        System.out.println("\n--- SENDERS AND RECIPIENTS (stored messages) ---");
        if (storedMessagesList.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        for (int i = 0; i < storedMessagesList.size(); i++) {
            Message msg = storedMessagesList.get(i);
            System.out.println("Message " + (i+1) + ": Sender = " + loggedInFirstName + " " + loggedInLastName + 
                             ", Recipient = " + msg.getRecipient());
        }
    }

    private static void displayLongestMessage() {
        System.out.println("\n--- LONGEST STORED MESSAGE ---");
        if (storedMessagesList.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        Message longest = storedMessagesList.get(0);
        for (Message msg : storedMessagesList) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }
        System.out.println("Longest message: " + longest.getMessageText());
        System.out.println("Length: " + longest.getMessageText().length() + " characters");
        System.out.println("Recipient: " + longest.getRecipient());
    }

    private static void searchByMessageId() {
        System.out.print("\nEnter Message ID: ");
        String searchId = input.nextLine();
        for (int i = 0; i < storedMessagesList.size(); i++) {
            if (storedMessagesList.get(i).getMessageID().equals(searchId)) {
                System.out.println("Message found!");
                System.out.println("Recipient: " + storedMessagesList.get(i).getRecipient());
                System.out.println("Message: " + storedMessagesList.get(i).getMessageText());
                return;
            }
        }
        System.out.println("Message ID not found.");
    }

    private static void searchByRecipient() {
        System.out.print("\nEnter recipient phone number: ");
        String searchPhone = input.nextLine();
        boolean found = false;
        System.out.println("\n--- Messages for " + searchPhone + " ---");
        for (Message msg : storedMessagesList) {
            if (msg.getRecipient().equals(searchPhone)) {
                System.out.println("Message: " + msg.getMessageText());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for that recipient.");
        }
    }

    private static void deleteByMessageHash() {
        System.out.print("\nEnter Message Hash: ");
        String hash = input.nextLine().toUpperCase();
        
        for (int i = 0; i < storedMessagesList.size(); i++) {
            if (storedMessagesList.get(i).getMessageHash().equals(hash)) {
                Message deleted = storedMessagesList.remove(i);
                messageHashes.remove(i);
                messageIds.remove(i);
                rewriteJsonFile();
                System.out.println("Message: \"" + deleted.getMessageText() + "\" successfully deleted.");
                return;
            }
        }
        System.out.println("Message hash not found.");
    }

    private static void displayFullReport() {
        System.out.println("\n=== FULL STORED MESSAGES REPORT ===");
        System.out.println("=================================");
        if (storedMessagesList.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        for (int i = 0; i < storedMessagesList.size(); i++) {
            Message msg = storedMessagesList.get(i);
            System.out.println("\n--- Message " + (i+1) + " ---");
            System.out.println("Message Hash: " + msg.getMessageHash());
            System.out.println("Recipient: " + msg.getRecipient());
            System.out.println("Message: " + msg.getMessageText());
            System.out.println("---------------------------------");
        }
    }

    private static void loadStoredMessagesFromJson() {
        File file = new File(JSON_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String content = sb.toString().trim();
            
            if (content.startsWith("[") && content.endsWith("]") && content.length() > 2) {
                System.out.println("Loaded existing messages from JSON.");
            }
        } catch (IOException e) {
            System.out.println("Note: No existing messages file found.");
        }
    }

    private static void rewriteJsonFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE))) {
            writer.write("[");
            for (int i = 0; i < storedMessagesList.size(); i++) {
                writer.write(storedMessagesList.get(i).storeMessage());
                if (i < storedMessagesList.size() - 1) {
                    writer.write(",");
                }
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }
}