/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginsystem;

import java.util.Scanner;

/**
 *
 * @author Student
 */
public class LoginSystem {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();  // Note: some methods are static, but keeping instance for flexibility

        System.out.println("=== Login System ===");

        while (true) {
            System.out.println("\n1. Register User");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 1) {
                // Registration
                System.out.print("Enter username (e.g. kyl_1): ");
                String username = scanner.nextLine();

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                System.out.print("Enter phone number (e.g. +27838968976): ");
                String phone = scanner.nextLine();

                System.out.print("Enter first name: ");
                String firstName = scanner.nextLine();

                System.out.print("Enter last name: ");
                String lastName = scanner.nextLine();

                String result = Login.registerUser(username, password, phone, firstName, lastName);
                System.out.println(result);

            } else if (choice == 2) {
                // Login
                System.out.print("Enter username: ");
                String username = scanner.nextLine();

                System.out.print("Enter password: ");
                String password = scanner.nextLine();

                String status = Login.returnLoginStatus(username, password);
                System.out.println(status);

            } else if (choice == 3) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Invalid option!");
            }
        }

        scanner.close();
    }
}

