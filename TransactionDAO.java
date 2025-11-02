package financetracker;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public TransactionDAO() {
        DatabaseManager.initializeDatabase();
    }

    public int insertTransaction(Transaction t) {
        String sql = "INSERT INTO transactions(description, amount, type, category, date) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, t.getDescription());
            ps.setDouble(2, t.getAmount());
            ps.setString(3, t.getType());
            ps.setString(4, t.getCategory());
            ps.setString(5, t.getDate());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting transaction: " + e.getMessage());
        }
        return -1;
    }

    public boolean deleteTransactionById(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
        }
        return false;
    }

    public Transaction getTransactionById(int id) {
        String sql = "SELECT id,description,amount,type,category,date FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transaction: " + e.getMessage());
        }
        return null;
    }

    public List<Transaction> getAllTransactions(String orderBy) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT id,description,amount,type,category,date FROM transactions";
        if (orderBy != null && !orderBy.isEmpty()) {
            sql += " ORDER BY " + orderBy;
        }
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error reading transactions: " + e.getMessage());
        }
        return list;
    }

    public List<Transaction> filterByCategory(String category) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT id,description,amount,type,category,date FROM transactions WHERE LOWER(category)=LOWER(?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error filtering by category: " + e.getMessage());
        }
        return list;
    }

    public List<Transaction> filterByDateRange(String start, String end) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT id,description,amount,type,category,date FROM transactions WHERE date BETWEEN ? AND ? ORDER BY date";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, start);
            ps.setString(2, end);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error filtering by date range: " + e.getMessage());
        }
        return list;
    }

    private Transaction mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String desc = rs.getString("description"); 
        double amt = rs.getDouble("amount"); 
        String type = rs.getString("type"); 
        String cat = rs.getString("category"); 
        String date = rs.getString("date"); 
        return new Transaction(id, desc, amt, type, cat, date);
    }

    public double calculateBalance() {
        String sql = "SELECT type, SUM(amount) as total FROM transactions GROUP BY type";
        double balance = 0;
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String t = rs.getString("type");
                double total = rs.getDouble("total");
                if (t.equalsIgnoreCase("income")) balance += total;
                else balance -= total;
            }
        } catch (SQLException e) {
            System.out.println("Error calculating balance: " + e.getMessage());
        }
        return balance;
    }

    public int getLastInsertedId() {
        String sql = "SELECT id FROM transactions ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Error getting last id: " + e.getMessage());
        }
        return -1;
    }

    public List<CategoryTotal> getTotalsByCategory() {
        List<CategoryTotal> list = new ArrayList<>();
        String sql = "SELECT category, SUM(CASE WHEN LOWER(type)='income' THEN amount ELSE -amount END) as total FROM transactions GROUP BY category";
        try (Connection conn = DatabaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new CategoryTotal(rs.getString("category"), rs.getDouble("total")));
            }
        } catch (SQLException e) {
            System.out.println("Error calculating totals: " + e.getMessage());
        }
        return list;
    }

    public static class CategoryTotal {
        public String category;
        public double total;
        public CategoryTotal(String category, double total) {
            this.category = category;
            this.total = total;
        }
    }
}
