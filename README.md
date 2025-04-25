## Project Overview:
The Expense Tracker Mobile App is a comprehensive mobile application designed to help users manage, track, and analyze their personal finances with ease. It allows users to record expenses, categorize transactions,and set monthly budgets — all through an intuitive, user-friendly interface.

## Features Implemented:
- Authentication: Firebase secure login/signup, Forgot Password.
- Expense Tracking: Add, edit, and delete expenses with filtering.
- Budget Setting: Set budgets with specific limit.
- Category Management: Custom categories with color & limits.
- Offline Support: Works without internet; syncs when online.
- Dark Mode: Optional light/dark theme.
- Language Support: Supports Arabic and English languages.

## Tools Used:
| Tools      | Purpose |
|------------|---------|
| **Kotlin** | Main programming language |
| **Jetpack Compose** | Modern UI toolkit for building UI |
| **Room Database** | Local data storage |
| **Hilt** | Dependency Injection |
| **MVVM Architecture** | Clean separation of concerns |
| **Material Design 3** | Styling and theming |
| **Compose Navigation** | Seamless screen transitions |

## How To Work:


## Core Screens: 

### Home Screen
- Display all recorded transactions in a clean list.
- Filter by **Daily**, **Weekly**, **Monthly**, or **Yearly**.
- Built-in **search functionality** to quickly find any transaction.
- Option to **edit** or **delete** individual transactions.

### Add Transaction Screen
- Add new transactions with the following fields:
  - Amount
  - Date
  - Category
  - Note
- Includes **validation** to ensure all fields are filled properly.

### Categories Screen
- View all **user-created** categories.
- Features:
  - Color-coded categories for better visibility
  - Edit and Delete options
  - Search and filter functionality

### Add Category Screen
- Create new categories with:
  - Category name
  - Category type
  - Color picker
  - Budget limit setting (Optional)
  - Note (Optional)

### Budgets Screen
- View all**user-created** budgets
  - Total budget
  - Option to edit or delete budget settings.
  - Start and end date

### Add Budgets Screen
- View all**user-created** budgets
  - Total budget amount
  - Start and end date
  - Notes (Optional)

## Screenshot Output:
