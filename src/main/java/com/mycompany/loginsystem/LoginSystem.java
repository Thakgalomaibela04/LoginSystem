/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loginsystem;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class LoginSystem {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("--- USER REGISTRATION ---");
        System.out.print("Enter Username: ");
        String username = input.nextLine();
        
        System.out.print("Enter Password: ");
        String password = input.nextLine();
        
        System.out.print("Enter Phone Number: ");
        String phone = input.nextLine();

        System.out.println("\n--- VALIDATION RESULTS ---");
        
        if (Login.checkUserName(username)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted.");
        }

        if (Login.checkPasswordComplexity(password)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted.");
        }

        if (Login.checkCellPhoneNumber(phone)) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted.");
        }

        // Attempt registration and output status
        String regResult = Login.registerUser(username, password, phone);
        System.out.println("\nFinal Status: " + regResult);

        // If registration passes, prompt for login credentials
        if (regResult.equals("User is successfully registered")) {
            System.out.println("\n--- LOGIN ---");
            System.out.print("Username: ");
            String loginUser = input.nextLine();
            System.out.print("Password: ");
            String loginPass = input.nextLine();
            
            System.out.println(Login.returnLoginStatus(loginUser, loginPass));
        }

        input.close();
    }
    
}