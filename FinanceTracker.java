// FinanceTracker.java
import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main service class for managing financial transactions
 * Handles CRUD operations and reporting functionality
 */
public class FinanceTracker {
    private List<Transaction> transactions;
    private static final String DATA_FILE = "transactions.csv";
    
    public FinanceTracker() {
        this.transactions = new ArrayList<>();
        loadTransactionsFromFile();
    }
    
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        
        transactions.add(transaction);
        saveTransactionsToFile();
        return true;
    }
    
    public boolean removeTransaction(int transactionId) {
        boolean removed = transactions.removeIf(transaction -> transaction.getId() == transactionId);
        
        if (removed) {
            saveTransactionsToFile();
        }
        
        return removed;
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }
    
    public List<Transaction> getTransactionsSortedByDate() {
        return transactions.stream()
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .collect(Collectors.toList());
    }
    
    public Map<String, List<Transaction>> getTransactionsByCategory() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getCategory));
    }
    
    public List<Transaction> getTransactionsForCurrentMonth() {
        YearMonth currentMonth = YearMonth.now();
        return transactions.stream()
                .filter(transaction -> YearMonth.from(transaction.getDate()).equals(currentMonth))
                .collect(Collectors.toList());
    }
    
    public List<Transaction> searchTransactions(String keyword) {
        String searchTerm = keyword.toLowerCase().trim();
        return transactions.stream()
                .filter(transaction -> 
                    transaction.getDescription().toLowerCase().contains(searchTerm) ||
                    transaction.getCategory().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    public double calculateTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public double calculateTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public double getCurrentBalance() {
        return calculateTotalIncome() - calculateTotalExpenses();
    }
    
    public Map<String, Double> getMonthlyExpensesByCategory() {
        YearMonth currentMonth = YearMonth.now();
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .filter(t -> YearMonth.from(t.getDate()).equals(currentMonth))
                .collect(Collectors.groupingBy(
                    Transaction::getCategory,
                    Collectors.summingDouble(Transaction::getAmount)
                ));
    }
    
    private void saveTransactionsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            writer.println("id,description,amount,category,type,date");
            
            for (Transaction transaction : transactions) {
                writer.println(transaction.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error saving transactions to file: " + e.getMessage());
        }
    }
    
    private void loadTransactionsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String headerLine = reader.readLine(); // Skip CSV header
            String line;
            int loadedCount = 0;
            
            while ((line = reader.readLine()) != null) {
                Transaction transaction = Transaction.fromCSV(line);
                if (transaction != null) {
                    transactions.add(transaction);
                    loadedCount++;
                }
            }
            
            if (loadedCount > 0) {
                System.out.println("Loaded " + loadedCount + " transactions from file.");
            }
        } catch (IOException e) {
            System.err.println("Error loading transactions from file: " + e.getMessage());
        }
    }
    
    public int getTransactionCount() {
        return transactions.size();
    }
}