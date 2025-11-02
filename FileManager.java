package financetracker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static void exportToCSV(List<Transaction> list, String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("description,amount,type,category,date");
            for (Transaction t: list) {
                pw.printf("%s,%f,%s,%s,%s\n", escape(t.getDescription()), t.getAmount(), t.getType(), escape(t.getCategory()), t.getDate());
            }
            System.out.println("Exported to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting: " + e.getMessage());
        }
    }

    public static List<Transaction> readFromCSV(String filename) {
        List<Transaction> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // simple split (assumes no commas in fields)
                if (parts.length >= 5) {
                    String desc = parts[0];
                    double amt = Double.parseDouble(parts[1]);
                    String type = parts[2];
                    String cat = parts[3];
                    String date = parts[4];
                    list.add(new Transaction(desc, amt, type, cat, date));
                }
            }
            System.out.println("Imported " + list.size() + " transactions from " + filename);
        } catch (Exception e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
        return list;
    }

    private static String escape(String s) {
        return s == null ? "" : s.replace(",", " "); // simple escape: remove commas
    }
}
