# FinanceTracker (Console + SQLite)

## Description
A console-based personal finance tracker that lets users add, remove, view, and categorize income and expenses. All data is stored in a local SQLite database, with features for filtering, sorting, monthly summaries, and CSV import/export. Designed to help users track their finances.

## Features
- Add, remove, and view transactions
- Undo last transaction (basic)
- Filter by category or date range
- Sort transactions by amount or date
- View totals by category
- Monthly income/expense summary
- Export and import transactions to/from CSV
- Database-backed persistence using SQLite

## Requirements
- Java 24+
- SQLite JDBC driver ([download here](https://github.com/xerial/sqlite-jdbc/releases))

## Setup & Run
1. Download the `sqlite-jdbc-(version).jar` and add it to your project libraries in NetBeans.
2. Open the project in NetBeans.
3. Run `MainApp.java`. The database file `finance.db` will be created automatically.
4. Use the console menu to interact with your transactions.
