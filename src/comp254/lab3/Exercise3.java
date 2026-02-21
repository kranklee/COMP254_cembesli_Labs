package comp254.lab3; // Package declaration for comp254.lab3

import java.util.Scanner;
import java.io.File;


public class Exercise3 {


    public static void find(File root, String filename) {
        if (root.getName().equals(filename))
            System.out.println("Found: " + root.getAbsolutePath());
        if (root.isDirectory()) {
            String[] children = root.list();
            if (children != null) {
                for (String childname : children) {
                    File child = new File(root, childname);
                    find(child, filename);
                }
            }
        }

    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Recursive File Finder ===");
        System.out.println();


        String userHome = System.getProperty("user.home");
        System.out.println("--- Predefined Test ---");
        System.out.println("Searching for '.classpath' in: " + userHome);
        System.out.println("Results:");
        find(new File(userHome), ".classpath");
        System.out.println();

        // Interactive user input section
        System.out.println("--- User Input ---");
        System.out.print("Enter the directory path to search in: ");
        String searchPath = scanner.nextLine().trim();

        System.out.print("Enter the filename to search for: ");
        String targetFilename = scanner.nextLine().trim();
        File searchDir = new File(searchPath);
        if (!searchDir.exists())
            System.out.println("Error: The path '" + searchPath + "' does not exist.");
        else {
            System.out.println("\nSearching for '" + targetFilename + "' in: " + searchPath);
            System.out.println("Results:");
            find(searchDir, targetFilename);
            System.out.println("\nSearch complete.");
        }

        scanner.close();
    }
}