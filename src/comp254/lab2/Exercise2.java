package comp254.lab2;

import javax.swing.*;
import java.awt.*;

public class Exercise2 extends JPanel {

    static int[] sizes;
    static long[] times1, times2;

    public static double[] prefixAverage1(double[] x) {
        int n = x.length;
        double[] a = new double[n];
        for (int j = 0; j < n; j++) {
            double s = 0;
            for (int i = 0; i <= j; i++) s += x[i];
            a[j] = s / (j + 1);
        }
        return a;
    }

    public static double[] prefixAverage2(double[] x) {
        int n = x.length;
        double[] a = new double[n];
        double s = 0;
        for (int j = 0; j < n; j++) {
            s += x[j];
            a[j] = s / (j + 1);
        }
        return a;
    }

    public static void runTests() {
        int trials = 8, n = 5000;
        sizes = new int[trials];
        times1 = new long[trials];
        times2 = new long[trials];

        System.out.println("=== PrefixAverage Timing ===\n");
        System.out.printf("%-10s %12s %12s%n", "n", "Avg1 (ms)", "Avg2 (ms)");
        System.out.println("------------------------------------");

        for (int t = 0; t < trials; t++) {
            double[] data = new double[n];
            for (int i = 0; i < n; i++) data[i] = i;

            long t1 = System.currentTimeMillis();
            prefixAverage1(data);
            times1[t] = System.currentTimeMillis() - t1;

            long t2 = System.currentTimeMillis();
            prefixAverage2(data);
            times2[t] = System.currentTimeMillis() - t2;

            sizes[t] = n;
            System.out.printf("%-10d %12d %12d%n", n, times1[t], times2[t]);
            n *= 2;
        }
        System.out.println("\nprefixAverage1 O(n^2): doubling n -> ~4x time");
        System.out.println("prefixAverage2 O(n):   doubling n -> ~2x time");
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight(), p = 55;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        // find log range
        double minX = Math.log10(sizes[0]), maxX = Math.log10(sizes[sizes.length - 1]);
        double maxY = Math.ceil(Math.log10(Math.max(1, times1[times1.length - 1]))) + 0.5;

        // axes
        g2.setColor(Color.BLACK);
        g2.drawLine(p, h - p, w - p, h - p);
        g2.drawLine(p, p, p, h - p);
        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.drawString("Log-Log: prefixAverage1 (red) vs prefixAverage2 (blue)", p, 20);
        g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2.drawString("log(n)", w / 2, h - 8);

        // draw both lines
        drawData(g2, times1, Color.RED, minX, maxX, maxY, w, h, p);
        drawData(g2, times2, Color.BLUE, minX, maxX, maxY, w, h, p);
    }

    void drawData(Graphics2D g2, long[] times, Color c,
                  double minX, double maxX, double maxY, int w, int h, int p) {
        g2.setColor(c);
        g2.setStroke(new BasicStroke(2));
        int px = -1, py = -1;
        for (int i = 0; i < sizes.length; i++) {
            if (times[i] <= 0) continue;
            int x = p + (int) ((Math.log10(sizes[i]) - minX) / (maxX - minX) * (w - 2 * p));
            int y = h - p - (int) (Math.log10(times[i]) / maxY * (h - 2 * p));
            g2.fillOval(x - 4, y - 4, 8, 8);
            if (px >= 0) g2.drawLine(px, py, x, y);
            px = x; py = y;
        }
    }

    public static void main(String[] args) {
        runTests();
        JFrame f = new JFrame("PrefixAverage Log-Log Chart");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(650, 420);
        f.add(new Exercise2());
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}