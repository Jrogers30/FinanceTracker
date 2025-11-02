package financetracker;

import java.util.List;

public class FinanceTracker {
    private TransactionDAO dao;
    private int lastRemovedId = -1;

    public FinanceTracker() {
        dao = new TransactionDAO();
    }

    public void addTransaction(Transaction t) {
        int id = dao.insertTransaction(t);
        if (id != -1) {
            t.setId(id);
            System.out.println("Added with id: " + id);
        }
    }

    public void removeTransactionById(int id) {
        // store last removed for undo
        Transaction removed = dao.getTransactionById(id);
        if (removed != null && dao.deleteTransactionById(id)) {
            lastRemovedId = id;
            System.out.println("Removed transaction id: " + id);
        } else {
            System.out.println("No transaction with id " + id);            }
    }

    public void undoLastRemove() {
        if (lastRemovedId == -1) {
            System.out.println("Nothing to undo.");
            return;
        }
        Transaction t = dao.getTransactionById(lastRemovedId);
        if (t == null) {
            System.out.println("Cannot undo (record not available).");
        } else {
            // nothing to do since it was deleted; undo would require storing the record before deletion
            System.out.println("Undo is not supported for DB deletions in this simple version."); 
        }
        lastRemovedId = -1;
    }

    public void displayAll(String orderBy) {
        List<Transaction> list = dao.getAllTransactions(orderBy);
        if (list.isEmpty()) {
            System.out.println("No transactions."); return;
        }
        for (Transaction t : list) System.out.println(t);
    }

    public void viewBalance() {
        double bal = dao.calculateBalance();
        System.out.printf("Current balance: $%.2f\n", bal);
    }

    public void filterCategory(String cat) {
        List<Transaction> list = dao.filterByCategory(cat);
        if (list.isEmpty()) System.out.println("No transactions for category: " + cat);
        else list.forEach(System.out::println);
    }

    public void filterDateRange(String start, String end) {
        List<Transaction> list = dao.filterByDateRange(start, end);
        if (list.isEmpty()) System.out.println("No transactions in that range.");
        else list.forEach(System.out::println);
    }

    public void sortByAmount(boolean asc) {
        String order = "amount " + (asc?"ASC":"DESC");
        displayAll(order);
    }

    public void sortByDate(boolean asc) {
        String order = "date " + (asc?"ASC":"DESC");
        displayAll(order);
    }

    public void totalsByCategory() {
        List<TransactionDAO.CategoryTotal> totals = dao.getTotalsByCategory();
        for (TransactionDAO.CategoryTotal c : totals) {
            System.out.printf("%s: $%.2f\n", c.category == null?"Uncategorized":c.category, c.total);
        }
    }

    public void monthlySummary(String month, String year) {
        // month in MM format, year YYYY, date stored as YYYY-MM-DD
        String start = year + "-" + month + "-01";
        String end = year + "-" + month + "-31";
        List<Transaction> list = dao.filterByDateRange(start, end);
        double income=0, expense=0;
        for (Transaction t: list) {
            if (t.getType().equalsIgnoreCase("income")) income += t.getAmount();
            else expense += t.getAmount();
        }
        System.out.println("Monthly Summary " + month + "/" + year);
        System.out.printf("Income: $%.2f\nExpense: $%.2f\nNet: $%.2f\n", income, expense, income - expense);
    }

    public void exportToFile(String filename) {
        List<Transaction> list = dao.getAllTransactions(null);
        FileManager.exportToCSV(list, filename);
    }

    public void importFromFile(String filename) {
        List<Transaction> list = FileManager.readFromCSV(filename);
        for (Transaction t: list) dao.insertTransaction(t);
    }
}
