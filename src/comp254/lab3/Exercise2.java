package comp254.lab3; // Package declaration for comp254.lab3

import java.util.Scanner;

public class Exercise2 {


    public static boolean isPalindrome(String s) {
        if (s.length() <= 1)
            return true;
        else if (s.charAt(0) != s.charAt(s.length() - 1))
            return false;
        else
            return isPalindrome(s.substring(1, s.length() - 1));
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Recursive Palindrome Checker ===");
        System.out.println();


        System.out.println("--- Predefined Test Cases ---");
        String[] testCases = {"racecar", "gohangasalamiimalasagnahog", "hello", "abba", "a", "madam", "java"};
        for (String test : testCases) {
            System.out.println("\"" + test + "\" -> " + (isPalindrome(test) ? "Palindrome" : "Not a palindrome"));
        }
        System.out.println();

        // Interactive user input loop
        System.out.println("--- User Input (type 'quit' to exit) ---");
        while (true) {
            System.out.print("Enter a string to check: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }

            String processed = input.toLowerCase();
            if (isPalindrome(processed))
                System.out.println("\"" + input + "\" is a palindrome.");
            else
                System.out.println("\"" + input + "\" is NOT a palindrome.");
            System.out.println();
        }

        scanner.close();
    }
}