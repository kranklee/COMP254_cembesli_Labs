package comp254.lab2;

import java.util.Arrays;
public class Exercise3 {

    /** Returns true if there are no duplicate elements  O(n^2) */
    public static boolean unique1(int[] data) {
        int n = data.length;
        for (int j = 0; j < n - 1; j++)
            for (int k = j + 1; k < n; k++)
                if (data[j] == data[k])
                    return false;
        return true;
    }

    /** Returns true if there are no duplicate elements O(n log n) */
    public static boolean unique2(int[] data) {
        int n = data.length;
        int[] temp = Arrays.copyOf(data, n);
        Arrays.sort(temp);
        for (int j = 0; j < n - 1; j++)
            if (temp[j] == temp[j + 1])
                return false;
        return true;
    }

    /** Runs the algorithm and returns time in ms */
    public static long timeIt(int size, boolean useFirst) {
        int[] data = new int[size];
        for (int i = 0; i < size; i++) data[i] = i;

        long start = System.currentTimeMillis();
        if (useFirst) unique1(data);
        else unique2(data);
        return System.currentTimeMillis() - start;
    }

    /** Binary search for max n that runs in under 60 seconds */
    public static int findMaxN(boolean useFirst, String name) {
        long limit = 60000;
        int lo = 1000, hi = 0;


        System.out.println("Finding max n for " + name + "...");

        // step 1: double n until it goes over 60 seconds
        int n = lo;
        while (true) {
            long time = timeIt(n, useFirst);
            System.out.printf("  n = %,d -> %,d ms%n", n, time);
            if (time > limit) { hi = n; break; }
            lo = n;
            n *= 2;
        }

        // step 2: binary search between lo and hi
        while (hi - lo > 1000) {
            int mid = (lo + hi) / 2;
            long time = timeIt(mid, useFirst);
            System.out.printf("  n = %,d -> %,d ms%n", mid, time);
            if (time <= limit) lo = mid;
            else hi = mid;
        }
        return lo;
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 3: Max n in 60 seconds ===\n");

        int max1 = findMaxN(true, "unique1 (O(n^2))");
        System.out.println(">> unique1 max n: " + max1 + "\n");

        int max2 = findMaxN(false, "unique2 (O(n log n))");
        System.out.println(">> unique2 max n: " + max2 + "\n");

        System.out.println(" Results ");
        System.out.println("unique1 O(n^2):     max n ~ " + max1);
        System.out.println("unique2 O(n log n): max n ~ " + max2);
    }
}