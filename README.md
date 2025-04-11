# Financial Tracker Application

A comprehensive web-based financial management system built with Spring Boot that helps users track their income, expenses, budgets, and financial goals.

## Features

- **User Authentication**: Secure login and registration system
- **Dashboard**: Overview of financial status with key metrics
- **Income Management**: Track and categorize different sources of income
- **Expense Tracking**: Monitor and categorize expenses
- **Budget Planning**: Set and track budgets by category
- **Financial Goals**: Set and monitor progress towards financial goals


## Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 17 or later
- Maven 3.6 or later
- MySQL 8.0 or later
- Git (optional, for cloning the repository)

## Installation & Setup

1. **Clone the Repository** (if using Git)
   ```bash
   git clone <repository-url>
   cd financial-tracker
   ```

2. **Database Setup**
   - Create a MySQL database:
   ```sql
   CREATE DATABASE financialtracker;
   ```
   - The application will automatically create the required tables on first run

3. **Configure Application**
   - Open `src/main/resources/application.properties`
   - Update the database configuration:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/financialtracker?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build the Application**
   ```bash
   mvn clean install
   ```

## Running the Application

1. **Start the Application**
   ```bash
   mvn spring-boot:run
   ```
   Or run the JAR file directly:
   ```bash
   java -jar target/financial-tracker-1.0.0.jar
   ```

2. **Access the Application**
   - Open a web browser and navigate to: `http://localhost:8080`
   - Register a new account or login with existing credentials
