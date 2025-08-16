// PersonalFinanceApp.java
import java.util.*;

/**
 * Console-based Personal Finance Tracker Application
 * Provides a menu-driven interface for managing personal finances
 */
public class PersonalFinanceApp {
    private FinanceTracker financeTracker;
    private Scanner scanner;
    private boolean running;
    
    public PersonalFinanceApp() {
        this.financeTracker = new FinanceTracker();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }
    
    public void start() {
        displayWelcomeMessage();
        
        while (running) {
            displayMainMenu();
            int choice = getUserChoice();
            handleMenuSelection(choice);
            
            if (running) {
                promptToContinue();
            }
        }
        
        scanner.close();
    }
    
    private void displayWelcomeMessage() {
        System.out.println("=====================================");
        System.out.println("    Personal Finance Tracker v1.0");
        System.out.println("      Track Your Income & Expenses");
        System.out.println("=====================================");
        System.out.println();
    }
    
    private void displayMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Add New Transaction");
        System.out.println("2. Remove Transaction");
        System.out.println("3. View All Transactions");
        System.out.println("4. View Transactions by Category");
        System.out.println("5. Generate Monthly Report");
        System.out.println("6. Search Transactions");
        System.out.println("7. View Current Balance");
        System.out.println("8. Exit Application");
        System.out.print("\nPlease select an option (1-8): ");
    }
    
    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }
    
    private void handleMenuSelection(int choice) {
        switch (choice) {
            case 1:
                handleAddTransaction();
                break;
            case 2:
                handleRemoveTransaction();
                break;
            case 3:
                handleViewAllTransactions();
                break;
            case 4:
                handleViewTransactionsByCategory();
                break;
            case 5:
                handleGenerateMonthlyReport();
                break;
            case 6:
                handleSearchTransactions();
                break;
            case 7:
                handleViewCurrentBalance();
                break;
            case 8:
                handleExit();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
    
    private void handleAddTransaction() {
        System.out.println("\n--- ADD NEW TRANSACTION ---");
        
        System.out.print("Enter description: ");
        String description = scanner.nextLine().trim();
        
        if (description.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }
        
        double amount = getValidAmount();
        if (amount <= 0) {
            return;
        }
        
        System.out.print("Enter category (e.g., Food, Transportation, Salary): ");
        String category = scanner.nextLine().trim();
        
        if (category.isEmpty()) {
            System.out.println("Category cannot be empty.");
            return;
        }
        
        Transaction.TransactionType type = getTransactionType();
        if (type == null) {
            return;
        }
        
        Transaction transaction = new Transaction(description, amount, category, type);
        
        if (financeTracker.addTransaction(transaction)) {
            System.out.println("Transaction added successfully!");
        } else {
            System.out.println("Failed to add transaction. Please try again.");
        }
    }
    
    private double getValidAmount() {
        System.out.print("Enter amount: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (amount <= 0) {
                System.out.println("Amount must be greater than zero.");
                return -1;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount format. Please enter a valid number.");
            return -1;
        }
    }
    
    private Transaction.TransactionType getTransactionType() {
        System.out.println("\nTransaction Type:");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        System.out.print("Select type (1 or 2): ");
        
        try {
            int typeChoice = Integer.parseInt(scanner.nextLine().trim());
            switch (typeChoice) {
                case 1:
                    return Transaction.TransactionType.INCOME;
                case 2:
                    return Transaction.TransactionType.EXPENSE;
                default:
                    System.out.println("Invalid selection. Transaction not added.");
                    return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter 1 or 2.");
            return null;
        }
    }
    
    private void handleRemoveTransaction() {
        System.out.println("\n--- REMOVE TRANSACTION ---");
        
        List<Transaction> allTransactions = financeTracker.getTransactionsSortedByDate();
        
        if (allTransactions.isEmpty()) {
            System.out.println("No transactions found to remove.");
            return;
        }
        
        System.out.println("Recent transactions:");
        System.out.println("-".repeat(80));
        
        // Show only the 10 most recent transactions
        allTransactions.stream()
            .limit(10)
            .forEach(System.out::println);
        
        System.out.print("\nEnter the ID of the transaction to remove: ");
        
        try {
            int transactionId = Integer.parseInt(scanner.nextLine().trim());
            
            if (financeTracker.removeTransaction(transactionId)) {
                System.out.println("Transaction removed successfully!");
            } else {
                System.out.println("Transaction with ID " + transactionId + " not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a valid number.");
        }
    }
    
    private void handleViewAllTransactions() {
        System.out.println("\n--- ALL TRANSACTIONS ---");
        
        List<Transaction> allTransactions = financeTracker.getTransactionsSortedByDate();
        
        if (allTransactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.println("Total transactions: " + allTransactions.size());
        System.out.println("-".repeat(100));
        
        for (Transaction transaction : allTransactions) {
            System.out.println(transaction);
        }
    }
    
    private void handleViewTransactionsByCategory() {
        System.out.println("\n--- TRANSACTIONS BY CATEGORY ---");
        
        Map<String, List<Transaction>> transactionsByCategory = financeTracker.getTransactionsByCategory();
        
        if (transactionsByCategory.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        for (Map.Entry<String, List<Transaction>> entry : transactionsByCategory.entrySet()) {
            String category = entry.getKey();
            List<Transaction> categoryTransactions = entry.getValue();
            
            double totalAmount = categoryTransactions.stream()
                .mapToDouble(t -> t.getType() == Transaction.TransactionType.INCOME ? 
                           t.getAmount() : -t.getAmount())
                .sum();
            
            System.out.println("\n" + category.toUpperCase() + " (Net: $" + String.format("%.2f", totalAmount) + ")");
            System.out.println("-".repeat(50));
            
            for (Transaction transaction : categoryTransactions) {
                System.out.println("  " + transaction);
            }
        }
    }
    
    private void handleGenerateMonthlyReport() {
        System.out.println("\n--- MONTHLY REPORT ---");
        
        List<Transaction> monthlyTransactions = financeTracker.getTransactionsForCurrentMonth();
        
        if (monthlyTransactions.isEmpty()) {
            System.out.println("No transactions found for the current month.");
            return;
        }
        
        double monthlyIncome = monthlyTransactions.stream()
            .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        double monthlyExpenses = monthlyTransactions.stream()
            .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
            .mapToDouble(Transaction::getAmount)
            .sum();
        
        double netAmount = monthlyIncome - monthlyExpenses;
        
        System.out.println("Report for: " + java.time.YearMonth.now());
        System.out.println("-".repeat(40));
        System.out.printf("Total Income:     $%.2f%n", monthlyIncome);
        System.out.printf("Total Expenses:   $%.2f%n", monthlyExpenses);
        System.out.println("-".repeat(40));
        System.out.printf("Net Amount:       $%.2f%n", netAmount);
        
        if (netAmount < 0) {
            System.out.println("WARNING: You spent more than you earned this month!");
        }
        
        // Show expense breakdown by category
        Map<String, Double> expensesByCategory = financeTracker.getMonthlyExpensesByCategory();
        
        if (!expensesByCategory.isEmpty()) {
            System.out.println("\nExpenses by Category:");
            System.out.println("-".repeat(30));
            
            expensesByCategory.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("%-15s: $%.2f%n", entry.getKey(), entry.getValue()));
        }
    }
    
    private void handleSearchTransactions() {
        System.out.println("\n--- SEARCH TRANSACTIONS ---");
        System.out.print("Enter search keyword (description or category): ");
        
        String keyword = scanner.nextLine().trim();
        
        if (keyword.isEmpty()) {
            System.out.println("Search keyword cannot be empty.");
            return;
        }
        
        List<Transaction> searchResults = financeTracker.searchTransactions(keyword);
        
        if (searchResults.isEmpty()) {
            System.out.println("No transactions found matching '" + keyword + "'");
            return;
        }
        
        System.out.println("Search results for '" + keyword + "':");
        System.out.println("Found " + searchResults.size() + " transaction(s)");
        System.out.println("-".repeat(80));
        
        for (Transaction transaction : searchResults) {
            System.out.println(transaction);
        }
    }
    
    private void handleViewCurrentBalance() {
        System.out.println("\n--- CURRENT BALANCE ---");
        
        double totalIncome = financeTracker.calculateTotalIncome();
        double totalExpenses = financeTracker.calculateTotalExpenses();
        double currentBalance = financeTracker.getCurrentBalance();
        
        System.out.println("Financial Summary:");
        System.out.println("-".repeat(30));
        System.out.printf("Total Income:     $%.2f%n", totalIncome);
        System.out.printf("Total Expenses:   $%.2f%n", totalExpenses);
        System.out.println("-".repeat(30));
        System.out.printf("Current Balance:  $%.2f%n", currentBalance);
        
        if (currentBalance < 0) {
            System.out.println("Note: Your expenses exceed your income.");
        } else if (currentBalance == 0) {
            System.out.println("Note: You've broken even.");
        } else {
            System.out.println("Note: You have a positive balance. Good job!");
        }
    }
    
    private void handleExit() {
        System.out.println("\nThank you for using Personal Finance Tracker!");
        System.out.println("Your data has been automatically saved.");
        running = false;
    }
    
    private void promptToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    public static void main(String[] args) {
        PersonalFinanceApp app = new PersonalFinanceApp();
        app.start();
    }
}