# NoBrokeZone 
*A Smart Expense Tracker for Sustainable Financial Habits*

## Overview

**NoBrokeZone** is a Java-based desktop application that enables users to manage their personal finances with discipline and insight. 
Unlike generic expense trackers, NoBrokeZone is designed to help users **set a savings goal based on their income** and track their spending in real-time to **ensure they never go broke**.

Whether you're a student, a young professional, or just someone who wants more control over where the money is going, NoBrokeZone offers a clear and intuitive way to stay on top of your budget.

---

## Key Features

- **Set Monthly Income and Target Savings**
  - Users can define their income and specify how much they aim to save.
  - Remaining amount becomes the budget for expenses.
  
- **Track Expenses Across Categories**
  - Add expenses with category, description, and amount.
  - All expenses are stored and displayed in a structured table.

- **Spending Limit Warning System**
  - Once the user hits 90% of the allowed expense limit, a **warning notification** is triggered.
  - Helps prevent overspending and build mindful financial habits.

- **MongoDB Database Integration**
  - Stores user data, income records, and expenses.
  - Scalable for future enhancements like login, monthly analysis, and cross-device syncing.

- **Real-time Balance and Savings Indicator**
  - See your balance update instantly as you log expenses.
  - Visual percentage to show how close you are to your savings goal.

- **Simple and Responsive Swing UI**
  - User-friendly interface built with Java Swing.
  - Clean layouts and meaningful color themes for readability.

---

## Tech Stack

- **Java (Swing)** – For building the desktop GUI
- **MongoDB** – Backend database for storing income and expense data
- **Maven** – Project build management

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/rkananya/NoBrokeZone.git
Navigate to the project:

``` bash
cd NoBrokeZone
```
Make sure you have MongoDB installed and running locally (default port: 27017).

Compile and run the application using your IDE or through terminal (if Maven configured):

``` bash

mvn compile
mvn exec:java
```
## Future Enhancements
  * Login and signup system
  
  * Monthly/yearly data visualization (charts)
  
  * Export reports as PDF
  
  * Mobile-responsive version
  
  * Dark mode toggle
---

##  ScreenShots :
**1.Add Income and your saving target:**
 - ![Screenshot 2025-06-04 135234](https://github.com/user-attachments/assets/4a1b0f65-b769-464d-a980-db005d6e1ec7)

**2.Add Expense :**
  - ![Screenshot 2025-06-04 135254](https://github.com/user-attachments/assets/d3a8a19a-d958-4aee-b1a5-c4c56fef6316)

**3. Get warnings:**
  - ![Screenshot 2025-06-04 135347](https://github.com/user-attachments/assets/de820568-d186-405a-b3f1-2bbf66259bcf)

**4.Keep yourself reminded!**:
  - ![Screenshot 2025-06-04 135358](https://github.com/user-attachments/assets/c7fe93ab-399b-4f73-b857-9576c98ff0ef)

  - [status panel reminds you- that your spending limit is nearing]

**5.View Expenses:**
  - ![Screenshot 2025-06-04 135406](https://github.com/user-attachments/assets/9939f327-bb51-48bb-9a4f-c921a0b4571d)

**6.Analyse your expense**
  - ![Screenshot 2025-06-04 135432](https://github.com/user-attachments/assets/1a1d5e31-1af1-43fa-9f43-c4758d67a151)

---
## Final Note:

NoBrokeZone isn’t just about tracking where your money goes — it’s about taking charge of it. With savings-first philosophy and intuitive design, the app empowers users to make smarter financial decisions, one rupee at a time.
