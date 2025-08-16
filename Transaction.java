// Transaction.java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a financial transaction (income or expense)
 * @author Your Name
 */
public class Transaction {
    private static int nextId = 1;
    private int id;
    private String description;
    private double amount;
    private String category;
    private LocalDate date;
    private TransactionType type;
    
    public enum TransactionType {
        INCOME, EXPENSE
    }
    
    // Constructor for new transactions
    public Transaction(String description, double amount, String category, TransactionType type) {
        this.id = nextId++;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = LocalDate.now();
    }
    
    // Constructor for loading existing transactions
    public Transaction(int id, String description, double amount, String category, 
                      TransactionType type, LocalDate date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        
        // Update next ID to prevent conflicts
        if (id >= nextId) {
            nextId = id + 1;
        }
    }
    
    // Getters
    public int getId() { 
        return id; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public double getAmount() { 
        return amount; 
    }
    
    public String getCategory() { 
        return category; 
    }
    
    public TransactionType getType() { 
        return type; 
    }
    
    public LocalDate getDate() { 
        return date; 
    }
    
    // Setters for editing transactions
    public void setDescription(String description) { 
        this.description = description; 
    }
    
    public void setAmount(double amount) { 
        this.amount = amount; 
    }
    
    public void setCategory(String category) { 
        this.category = category; 
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("ID: %d | Date: %s | Type: %s | Amount: $%.2f | Category: %s | Description: %s", 
                           id, date.format(formatter), type.toString(), amount, category, description);
    }
    
    // Convert transaction to CSV format for file storage
    public String toCSV() {
        return String.format("%d,\"%s\",%.2f,%s,%s,%s", 
                           id, description.replace("\"", "\"\""), amount, category, type.toString(), date.toString());
    }
    
    // Create transaction object from CSV string
    public static Transaction fromCSV(String csvLine) {
        // Simple CSV parsing - in production, would use a proper CSV library
        String[] parts = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        
        if (parts.length < 6) {
            return null;
        }
        
        try {
            int id = Integer.parseInt(parts[0].trim());
            String description = parts[1].trim().replaceAll("^\"|\"$", "").replace("\"\"", "\"");
            double amount = Double.parseDouble(parts[2].trim());
            String category = parts[3].trim();
            TransactionType type = TransactionType.valueOf(parts[4].trim());
            LocalDate date = LocalDate.parse(parts[5].trim());
            
            return new Transaction(id, description, amount, category, type, date);
        } catch (Exception e) {
            System.err.println("Error parsing CSV line: " + csvLine);
            return null;
        }
    }
}