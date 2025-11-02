package financetracker;

import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DatabaseManager.initializeDatabase();
        FinanceTracker tracker = new FinanceTracker();

        int choice = -1;
        while (choice != 14) {
            displayMenu();
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input."); continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Description: "); String desc = sc.nextLine();
                    System.out.print("Amount: "); double amt = Double.parseDouble(sc.nextLine());
                    System.out.print("Type (income/expense): "); String type = sc.nextLine();
                    System.out.print("Category: "); String cat = sc.nextLine();
                    System.out.print("Date (YYYY-MM-DD): "); String date = sc.nextLine();
                    tracker.addTransaction(new Transaction(desc, amt, type, cat, date));
                }
                case 2 -> {
                    System.out.print("Enter ID to remove: "); int id = Integer.parseInt(sc.nextLine());
                    tracker.removeTransactionById(id);
                }
                case 3 -> tracker.undoLastRemove();
                case 4 -> tracker.displayAll("date DESC");
                case 5 -> tracker.viewBalance();
                case 6 -> {
                    System.out.print("Export filename: "); String f = sc.nextLine();
                    tracker.exportToFile(f);
                }
                case 7 -> {
                    System.out.print("Import filename: "); String f = sc.nextLine();
                    tracker.importFromFile(f);
                }
                case 8 -> {
                    System.out.print("Category: "); String c = sc.nextLine();
                    tracker.filterCategory(c);
                }
                case 9 -> {
                    System.out.print("Start (YYYY-MM-DD): "); String s = sc.nextLine();
                    System.out.print("End (YYYY-MM-DD): "); String e = sc.nextLine();
                    tracker.filterDateRange(s, e);
                }
                case 10 -> {
                    System.out.print("Ascending? true/false: "); boolean asc = Boolean.parseBoolean(sc.nextLine());
                    tracker.sortByAmount(asc);
                }
                case 11 -> {
                    System.out.print("Ascending? true/false: "); boolean asc = Boolean.parseBoolean(sc.nextLine());
                    tracker.sortByDate(asc);
                }
                case 12 -> tracker.totalsByCategory();
                case 13 -> {
                    System.out.print("Month (MM): "); String m = sc.nextLine();
                    System.out.print("Year (YYYY): "); String y = sc.nextLine();
                    tracker.monthlySummary(m, y);
                }
                case 14 -> System.out.println("Exiting..."); 
                default -> System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    static void displayMenu() {
        System.out.println("==========================");
        System.out.println(" Personal Finance Tracker");        
        System.out.println("==========================");
        System.out.println("1.) Add Transaction");
        System.out.println("2.) Remove Transaction");
        System.out.println("3.) Undo Last Transaction");
        System.out.println("4.) View All Transactions");
        System.out.println("5.) View Current Balance");
        System.out.println("6.) Export Transactions to File");
        System.out.println("7.) Import Transactions from File");
        System.out.println("-------------------------------");
        System.out.println("8.) Filter Transactions by Category");
        System.out.println("9.) Filter Transactions by Date Range");
        System.out.println("10.) Sort Transactions by Amount");
        System.out.println("11.) Sort Transactions by Date");
        System.out.println("12.) View Totals by Category");
        System.out.println("13.) View Monthly Summary");
        System.out.println("14.) Exit");
        System.out.println("-------------------------------");
        System.out.print("Enter choice: ");
    }
}
