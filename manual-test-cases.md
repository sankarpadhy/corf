# Manual Test Cases for CORS Protection

## Test Case 1: Mobile Banking App Balance Check
- **Objective**: Verify that the mobile banking app can access the balance endpoint.
- **Steps**:
  1. Open the browser and navigate to `http://localhost:3000`.
  2. Click on the "Refresh Balance" button.
- **Expected Result**: The balance should be displayed without any CORS errors.
- **Updated Testing Steps**:
  - **Request**: GET `/api/banking/balance`
  - **Expected Outcome**: Successfully fetches balance with status "success" and message "Balance: ₹10,000".

## Test Case 2: Financial Planner Transfer
- **Objective**: Verify that the financial planner app can perform a fund transfer.
- **Steps**:
  1. Open the browser and navigate to `http://localhost:3001`.
  2. Enter an amount and select an investment account.
  3. Click on the "Invest Now" button.
- **Expected Result**: The transfer should be successful, and a confirmation message should appear.
- **Updated Testing Steps**:
  - **Preflight Request**:
    - **Request**: OPTIONS `/api/banking/transfer`
    - **Expected Outcome**: Successful preflight with appropriate CORS headers.
  - **Actual Transfer Request**:
    - **Request**: POST `/api/banking/transfer`
    - **Expected Outcome**: Successfully transfers funds with status "success" and message "Transferred ₹5000 to account".

## Test Case 3: Investment Dashboard Portfolio Check
- **Objective**: Verify that the investment dashboard can access the portfolio value.
- **Steps**:
  1. Open the browser and navigate to `http://localhost:3002`.
  2. Click on the "Refresh Portfolio" button.
- **Expected Result**: The portfolio value should be displayed without any CORS errors.
- **Updated Testing Steps**:
  - **Request**: GET `/api/banking/balance`
  - **Expected Outcome**: Successfully fetches balance with status "success" and message "Balance: ₹10,000".

## Test Case 4: Unauthorized Origin Access
- **Objective**: Ensure unauthorized origins are blocked from accessing the API.
- **Steps**:
  1. Use a tool like Postman or curl to send a request to `http://localhost:8080/api/banking/balance`.
  2. Set the `Origin` header to `http://malicious-site.com`.
- **Expected Result**: The request should be blocked, and a CORS error should be returned.
- **Updated Testing Steps**:
  - **Request**: Any request from an unauthorized origin
  - **Expected Outcome**: Request is blocked with "Invalid CORS request" message.
