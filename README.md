Personal Finance Tracker

A comprehensive console-based Java application for managing personal finances. Track income, expenses, and generate detailed financial reports with persistent data storage.
This project demonstrates practical application of core Java concepts including object-oriented programming, file I/O operations, stream processing, and data persistence. It's designed to be both functional for real-world use and educational for understanding software development principles.
Features
Core Functionality

    Transaction Management: Add, remove, and edit financial transactions
    Categorization: Organize transactions by customizable categories (Food, Transportation, Salary, etc.)
    Income & Expense Tracking: Separate handling of income and expense transactions
    Data Persistence: Automatic saving and loading of transaction data using CSV format
    Search Functionality: Find transactions by description or category keywords

Reporting & Analytics

    Monthly Reports: Detailed breakdown of income, expenses, and net amount
    Category Analysis: View spending patterns grouped by categories
    Balance Tracking: Real-time calculation of current financial balance
    Expense Breakdown: Identify top spending categories with sorted reports
    Transaction History: Chronological listing of all financial activities

User Experience

    Intuitive Menu System: Easy-to-navigate console interface
    Input Validation: Robust error handling and data validation
    Professional Output: Clean, formatted display of financial information
    Automatic Data Backup: Transactions automatically saved to CSV file

Technical Highlights
Programming Concepts Demonstrated

    Object-Oriented Design: Clean separation of concerns with Transaction and FinanceTracker classes
    Enum Usage: Type-safe transaction categorization with TransactionType enum
    Stream API: Modern Java functional programming for data filtering and processing
    File I/O Operations: CSV-based data persistence with proper error handling
    Date/Time Handling: LocalDate and YearMonth for accurate date management
    Collections Framework: Efficient use of ArrayList, HashMap, and stream operations
    Exception Handling: Comprehensive error handling throughout the application

Code Quality Features

    Input Sanitization: Safe handling of user input with validation
    Data Integrity: Proper CSV parsing with quote handling and escape characters
    Memory Management: Efficient data structures and resource cleanup
    Modular Design: Clear separation between data models, business logic, and UI
    Professional Documentation: Comprehensive JavaDoc-style comments

Installation & Setup
Prerequisites

    Java Development Kit (JDK) 8 or higher
    Command line interface (Command Prompt, PowerShell, or Terminal)

Installation Steps

    Clone the Repository

bash
'''
git clone https://github.com/yourusername/personal-finance-tracker.git
cd personal-finance-tracker
'''

Compile the Application
bash
'''
javac *.java
'''

Run the Application

    bash
    '''

    java PersonalFinanceApp
    '''

