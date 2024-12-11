
# Banking System Project

Welcome to the Banking System Project, a Spring Boot application designed to manage basic banking operations. This project provides features such as:

Deposit and Withdrawal

Account Statement (including date, amount, and balance)

Statement Printing

Additionally, you can explore and test the API endpoints directly using the Swagger documentation at:

```shell
http://localhost:8080/swagger-ui/index.html?configUrl
```
## Features

1. Deposit Money: Securely add funds to your account.
2. Withdraw Money: Seamlessly withdraw money from your account.
3. Account Statement: View and print your account's transaction history, including balances.

## Usage
1. Create a Client:

Endpoint: POST /api/v1/clients.

2. Create a Bank Account:

Endpoint: POST /api/v1/bank-accounts

3. Deposit Money

Endpoint: POST /api/v1/bank-account-operations/{accountNumber}/deposit

4. Withdraw Money

Endpoint: POST /api/v1/bank-account-operations/{accountNumber}/withdrawal

5. View Account Statement

Endpoint: GET /api/v1/bank-account-operations/{accountNumber}/statement
