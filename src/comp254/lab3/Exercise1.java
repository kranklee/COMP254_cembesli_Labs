package comp254.lab3; // Package declaration for comp254.lab3

import java.util.Scanner;


public class Exercise1 {


    public static int multiply(int m, int n) {
        if (n == 0)
            return 0;
        else
            return m + multiply(m, n - 1);
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Recursive Multiplication (Using Addition & Subtraction) ===");
        System.out.println();


        System.out.println("--- Predefined Test Cases ---"); // Print section header
        System.out.println("multiply(3, 5) = " + multiply(3, 5));     // Test: 3*5 = 15
        System.out.println("multiply(7, 4) = " + multiply(7, 4));     // Test: 7*4 = 28
        System.out.println("multiply(6, 0) = " + multiply(6, 0));     // Test: 6*0 = 0
        System.out.println("multiply(0, 8) = " + multiply(0, 8));     // Test: 0*8 = 0
        System.out.println("multiply(1, 9) = " + multiply(1, 9));     // Test: 1*9 = 9
        System.out.println("multiply(12, 11) = " + multiply(12, 11)); // Test: 12*11 = 132
        System.out.println();


        System.out.println("--- User Input ---");
        System.out.print("Enter the first positive integer (m): ");
        int m = scanner.nextInt();
        System.out.print("Enter the second positive integer (n): ");
        int n = scanner.nextInt();

        if (m < 0 || n < 0)
            System.out.println("Error: Both numbers must be positive integers.");
        else
            System.out.println(m + " * " + n + " = " + multiply(m, n));

        scanner.close();
    }
}