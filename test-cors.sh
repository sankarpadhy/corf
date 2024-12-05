#!/bin/bash

# Test 1: Mobile Banking App Balance Check
echo "Testing Mobile Banking App (CORS)"
curl -X GET \
     -H "Origin: http://localhost:3000" \
     -H "Access-Control-Request-Method: GET" \
     -v http://localhost:8080/api/banking/balance

echo -e "\n\n"

# Test 2: Financial Planner Transfer (Preflight + Actual Request)
echo "Testing Financial Planner - Preflight Request"
curl -X OPTIONS \
     -H "Origin: http://localhost:3001" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: Content-Type" \
     -v http://localhost:8080/api/banking/transfer

echo -e "\n\n"

echo "Testing Financial Planner - Actual Transfer"
curl -X POST \
     -H "Origin: http://localhost:3001" \
     -H "Content-Type: application/json" \
     -d '{"amount": 5000, "toAccount": "mutual-fund"}' \
     -v http://localhost:8080/api/banking/transfer

echo -e "\n\n"

# Test 3: Investment Dashboard Balance Check
echo "Testing Investment Dashboard (CORS)"
curl -X GET \
     -H "Origin: http://localhost:3002" \
     -H "Access-Control-Request-Method: GET" \
     -v http://localhost:8080/api/banking/balance

# Test 4: Unauthorized Origin (Should be blocked)
echo -e "\n\nTesting Unauthorized Origin (Should be blocked)"
curl -X GET \
     -H "Origin: http://malicious-site.com" \
     -H "Access-Control-Request-Method: GET" \
     -v http://localhost:8080/api/banking/balance
