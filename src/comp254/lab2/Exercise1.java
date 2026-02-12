package comp254.lab2;

public class Exercise1 {


    public static int example1(int[] arr) {
        int n = arr.length, total = 0;
        for (int j=0; j < n; j++)       // loop from 0 to n-1
            total += arr[j];
        return total;
    }


    public static int example2(int[] arr) {
        int n = arr.length, total = 0;
        for (int j=0; j < n; j += 2)    // note the increment of 2
            total += arr[j];
        return total;
    }


    public static int example3(int[] arr) {
        int n = arr.length, total = 0;
        for (int j=0; j < n; j++)       // loop from 0 to n-1
            for (int k=0; k <= j; k++)    // loop from 0 to j oldu
                total += arr[j];
        return total;
    }


    public static int example4(int[] arr) {
        int n = arr.length, prefix = 0, total = 0;
        for (int j=0; j < n; j++) {     // loop from 0 to n-1 onemli
            prefix += arr[j];
            total += prefix;
        }
        return total;
    }


    public static int example5(int[] first, int[] second) {
        int n = first.length, count = 0;
        for (int i=0; i < n; i++) {     // loop from 0 to n-1
            int total = 0;
            for (int j=0; j < n; j++)     // loop from 0 to n-1
                for (int k=0; k <= j; k++)  // loop from 0 to j
                    total += first[k];
            if (second[i] == total) count++;
        }
        return count;
    }

    public static void main(String[] args) {
        int[] sample = {1, 2, 3, 4, 5};

        System.out.println("=== Exercise 1: Big-O Analysis ===\n");

        System.out.println("example1 result: " + example1(sample));
        System.out.println("  -> O(n)\n");

        System.out.println("example2 result: " + example2(sample));
        System.out.println("  -> O(n)\n");

        System.out.println("example3 result: " + example3(sample));
        System.out.println("  -> O(n^2)\n");

        System.out.println("example4 result: " + example4(sample));
        System.out.println("  -> O(n)\n");

        int[] sample2 = {1, 2, 3, 4, 5};
        System.out.println("example5 result: " + example5(sample, sample2));
        System.out.println("  -> O(n^3)\n");

        System.out.println("=== Summary ===");
        System.out.println("example1 -> O(n)");
        System.out.println("example2 -> O(n)");
        System.out.println("example3 -> O(n^2)");
        System.out.println("example4 -> O(n)");
        System.out.println("example5 -> O(n^3)");
    }
}