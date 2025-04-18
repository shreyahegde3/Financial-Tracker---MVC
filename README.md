# Financial Tracker Application

A comprehensive web-based financial management system built with Spring Boot that helps users track their income, expenses, budgets, and financial goals.

## Features

- **User Authentication**: Secure login and registration system
- **Dashboard**: Overview of financial status with key metrics
- **Income Management**: Track and categorize different sources of income
- **Expense Tracking**: Monitor and categorize expenses
- **Budget Planning**: Set and track budgets by category
- **Financial Goals**: Set and monitor progress towards financial goals
- **Responsive Design**: Works on desktop and mobile devices

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

## Usage Guide

### Dashboard
- View overall financial summary
- See recent transactions
- Monitor budget status

### Income
- Add new income entries with:
  - Amount
  - Source (Salary, Freelance, Investments, etc.)
  - Category (Fixed, Variable)
  - Date
  - Recurring status

### Expenses
- Track expenses with:
  - Amount
  - Category (Housing, Food, Transportation, etc.)
  - Description
  - Date
  - Recurring status

### Budgets
- Create budgets for different categories
- Set monthly spending limits
- Monitor spending against budgets
- View remaining amounts

### Financial Goals
- Set short and long-term financial goals
- Track progress towards goals
- Set target dates and amounts

## Technology Stack

- **Backend**: Spring Boot 3.2.3
- **Frontend**: Thymeleaf, Bootstrap 5
- **Database**: MySQL 8
- **Security**: Spring Security
- **Build Tool**: Maven

## Dependencies

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- Spring Boot Starter Thymeleaf
- MySQL Connector
- Lombok
- Thymeleaf Extras Spring Security 6

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Security Considerations

- All passwords are encrypted using BCrypt
- Session management is handled by Spring Security
- CSRF protection is enabled
- Form validation is implemented both client and server-side

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL is running
   - Check database credentials in application.properties
   - Ensure database exists

2. **Application Won't Start**
   - Verify Java version (must be 17 or higher)
   - Check port 8080 is available
   - Review application logs for errors

3. **Compilation Errors**
   - Run `mvn clean install` to ensure all dependencies are downloaded
   - Verify Java version compatibility

### Getting Help

If you encounter any issues:
1. Check the application logs
2. Review the documentation
3. Submit an issue on the repository

## License

This project is licensed under the MIT License - see the LICENSE file for details 