package com.mycompany.loginsystem;   // adjust to your package

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class LoginSystem2 {
    private static final Scanner input = new Scanner(System.in);

    // Required arrays
    private static final ArrayList<String> sentMessages = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIds = new ArrayList<>();

    private static final String JSON_FILE = "messages.json";
    private static String loggedInUsername = "";  // store after login

    public static void main(String[] args) {
        // Registration
        System.out.println("--- USER REGISTRATION ---");
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = input.nextLine();

        System.out.println("\n--- VALIDATION RESULTS ---");
        if (Login.checkUserName(username))
            System.out.println("Username successfully captured.");
        else
            System.out.println("Username is not correctly formatted.");

        if (Login.checkPasswordComplexity(password))
            System.out.println("Password successfully captured.");
        else
            System.out.println("Password is not correctly formatted.");

        if (Login.checkCellPhoneNumber(phone))
            System.out.println("Cell phone number successfully added.");
        else
            System.out.println("Cell phone number incorrectly formatted.");

        String regResult = Login.registerUser(username, password, phone);
        System.out.println("\nFinal Status: " + regResult);
        if (!regResult.equals("User is successfully registered")) return;

        // Login
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String loginUser = input.nextLine();
        System.out.print("Password: ");
        String loginPass = input.nextLine();
        String loginStatus = Login.returnLoginStatus(loginUser, loginPass);
        System.out.println(loginStatus);
        if (!loginStatus.equals("A successful login")) return;
        loggedInUsername = loginUser;   // store for later display

        // Load existing messages
        loadStoredMessagesFromJson();

        // Main menu
        int option;
        do {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Send message(s)");
            System.out.println("2. View disregarded messages");
            System.out.println("3. Stored Messages (Reports & Management)");
            System.out.println("4. Exit");
            System.out.print("Choice: ");
            try {
                option = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1 -> sendMessagesPrompt();
                case 2 -> viewDisregardedMessages();
                case 3 -> storedMessagesMenu();
                case 4 -> System.out.println("Exiting.");
                default -> System.out.println("Invalid option.");
            }
        } while (option != 4);
    }

    // Ask how many messages to send
    private static void sendMessagesPrompt() {
        System.out.print("\nHow many messages do you want to send? ");
        int count;
        try {
            count = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Sending 1 message.");
            count = 1;
        }
        if (count <= 0) {
            System.out.println("Number must be positive.");
            return;
        }
        for (int i = 1; i <= count; i++) {
            System.out.println("\n--- Message " + i + " of " + count + " ---");
            sendSingleMessage();
        }
    }

    // Send one message, populate arrays, save to JSON
    private static void sendSingleMessage() {
        System.out.print("Recipient phone number (+...): ");
        String recipient = input.nextLine();
        System.out.print("Message text: ");
        String messageText = input.nextLine();

        Message msg = new Message(recipient, messageText);

        boolean recipientValid = msg.checkRecipientCell().contains("successfully");
        boolean messageValid = validateMessageContent(messageText);

        if (recipientValid && messageValid) {
            // Valid message
            sentMessages.add(messageText);
            messageHashes.add(msg.getMessageHash());
            messageIds.add(msg.getMessageID());
            // Add to stored messages and persist
            String json = msg.storeMessage();
            storedMessages.add(json);
            rewriteJsonFile();

            // Display the required details: Username, MessageID, MessageHash, Phone number
            msg.printMessage(loggedInUsername);
        } else {
            // Invalid message
            disregardedMessages.add(messageText);
            System.out.println("\n❌ Message disregarded.");
            if (!recipientValid) System.out.println("Reason: Invalid recipient number.");
            if (!messageValid) System.out.println("Reason: Message invalid (empty, too long, or spam).");
        }
    }

    private static boolean validateMessageContent(String text) {
        if (text == null || text.trim().isEmpty()) return false;
        if (text.length() > 160) return false;
        String lower = text.toLowerCase();
        return !(lower.contains("spam") || lower.contains("lottery") || lower.contains("free money"));
    }

    private static void viewDisregardedMessages() {
        System.out.println("\n--- DISREGARDED MESSAGES ---");
        if (disregardedMessages.isEmpty())
            System.out.println("No disregarded messages.");
        else
            for (int i = 0; i < disregardedMessages.size(); i++)
                System.out.println((i+1) + ". " + disregardedMessages.get(i));
    }

    // ---------- Stored Messages Menu (features a-f) ----------
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

    // a. Display sender and recipient
    private static void displaySendersAndRecipients() {
        System.out.println("\n--- SENDERS AND RECIPIENTS (stored messages) ---");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            String recipient = extractJsonValue(storedMessages.get(i), "phone");
            System.out.println("Message " + (i+1) + ": Sender = " + loggedInUsername + ", Recipient = " + recipient);
        }
    }

    // b. Display longest stored message
    private static void displayLongestMessage() {
        System.out.println("\n--- LONGEST STORED MESSAGE ---");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        String longest = "";
        for (String json : storedMessages) {
            String text = extractJsonValue(json, "text");
            if (text.length() > longest.length()) longest = text;
        }
        System.out.println("Longest message: " + longest);
        System.out.println("Length: " + longest.length() + " characters");
    }

    // c. Search by message ID
    private static void searchByMessageId() {
        System.out.print("\nEnter Message ID: ");
        String searchId = input.nextLine();
        for (int i = 0; i < messageIds.size(); i++) {
            if (messageIds.get(i).equals(searchId)) {
                System.out.println("Found!");
                System.out.println("Recipient: " + extractJsonValue(storedMessages.get(i), "phone"));
                System.out.println("Message: " + extractJsonValue(storedMessages.get(i), "text"));
                return;
            }
        }
        System.out.println("Message ID not found.");
    }

    // d. Search by recipient
    private static void searchByRecipient() {
        System.out.print("\nEnter recipient phone: ");
        String searchPhone = input.nextLine();
        boolean found = false;
        for (String json : storedMessages) {
            if (extractJsonValue(json, "phone").equals(searchPhone)) {
                System.out.println("Message: " + extractJsonValue(json, "text"));
                found = true;
            }
        }
        if (!found) System.out.println("No messages for that recipient.");
    }

    // e. Delete by message hash
    private static void deleteByMessageHash() {
        System.out.print("\nEnter Message Hash: ");
        String hash = input.nextLine();
        int index = messageHashes.indexOf(hash);
        if (index != -1) {
            messageHashes.remove(index);
            messageIds.remove(index);
            sentMessages.remove(index);
            storedMessages.remove(index);
            rewriteJsonFile();
            System.out.println("Message deleted successfully.");
        } else {
            System.out.println("Hash not found.");
        }
    }

    // f. Display full report
    private static void displayFullReport() {
        System.out.println("\n=== FULL STORED MESSAGES REPORT ===");
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages.");
            return;
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            System.out.println("--- Message " + (i+1) + " ---");
            System.out.println("ID: " + messageIds.get(i));
            System.out.println("Hash: " + messageHashes.get(i));
            System.out.println("Recipient: " + extractJsonValue(storedMessages.get(i), "phone"));
            System.out.println("Message: " + extractJsonValue(storedMessages.get(i), "text"));
            System.out.println();
        }
    }

    // ---------- JSON Helpers ----------
    private static void loadStoredMessagesFromJson() {
        File file = new File(JSON_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            String content = sb.toString().trim();

            if (content.startsWith("[") && content.endsWith("]")) {
                content = content.substring(1, content.length() - 1).trim();
                if (!content.isEmpty()) {
                    String[] objects = content.split("\\},\\{");
                    for (String obj : objects) {
                        if (!obj.startsWith("{")) obj = "{" + obj;
                        if (!obj.endsWith("}")) obj = obj + "}";
                        storedMessages.add(obj);
                        messageIds.add(extractJsonValue(obj, "messageId"));
                        messageHashes.add(extractJsonValue(obj, "hash"));
                        sentMessages.add(extractJsonValue(obj, "text"));
                    }
                }
            }
            System.out.println("Loaded " + storedMessages.size() + " messages from JSON.");
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    private static void rewriteJsonFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE))) {
            writer.write("[");
            for (int i = 0; i < storedMessages.size(); i++) {
                writer.write(storedMessages.get(i));
                if (i < storedMessages.size() - 1) writer.write(",");
            }
            writer.write("]");
        } catch (IOException e) {
            System.out.println("Error writing JSON file: " + e.getMessage());
        }
    }

    private static String extractJsonValue(String json, String key) {
        if (json == null || key == null) return "";
        String search = "\"" + key + "\": \"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return "";
        return json.substring(start, end);
    }

    // ---------- Public getters for unit testing (optional) ----------
    public static ArrayList<String> getSentMessagesForTest() { return new ArrayList<>(sentMessages); }
    public static ArrayList<String> getStoredMessagesForTest() { return new ArrayList<>(storedMessages); }
    public static ArrayList<String> getMessageIdsForTest() { return new ArrayList<>(messageIds); }
    public static ArrayList<String> getMessageHashesForTest() { return new ArrayList<>(messageHashes); }

    public static boolean deleteMessageByHash(String targetHash) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static ArrayList<String> getFullReportLines() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static String getLongestStoredMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static String getLongestStoredMessage() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static ArrayList<String> getMessagesByRecipient(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static String getRecipientAndMessageById(String targetId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static String getRecipientAndMessageById(String targetId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static String getRecipientAndMessageById(String targetId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static Object getSentMessages() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static ArrayList<String> getMessageHashes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static ArrayList<String> getSentMessages() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static Object getSentMessages() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static Object getMessageHashes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
   