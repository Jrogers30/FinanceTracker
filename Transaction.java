package financetracker;

public class Transaction {
    private int id;
    private String description;
    private double amount;
    private String type; // "income" or "expense"
    private String category;
    private String date; // YYYY-MM-DD

    public Transaction(int id, String description, double amount, String type, String category, String date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
    }

    public Transaction(String description, double amount, String type, String category, String date) {
        this(-1, description, amount, type, category, date);
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public String getDate() { return date; }

    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | $%.2f | %s", id, date, description, amount, category);
    }
}
