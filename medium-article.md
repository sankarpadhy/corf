# Implementing Secure CORS for Banking Applications: A Step-by-Step Guide

In today's interconnected web landscape, securing communication between different domains is crucial, especially for sensitive applications like banking systems. This guide walks you through implementing Cross-Origin Resource Sharing (CORS) in a multi-application banking environment, ensuring both security and functionality.

## Why CORS Matters in Banking?

Imagine you're building a banking ecosystem with multiple applications:
- A mobile banking app for daily transactions
- A financial planner for investment decisions
- An investment dashboard for portfolio management

Each of these applications needs to communicate securely with your banking API while preventing unauthorized access. This is where CORS comes into play.

## Project Architecture

Our banking system consists of three distinct frontend applications and a secure backend API:

```
┌─────────────────┐     ┌──────────────┐     ┌────────────────┐
│  Mobile Banking │     │  Financial   │     │   Investment   │
│      App        │     │   Planner    │     │   Dashboard    │
│ localhost:3000  │     │localhost:3001│     │ localhost:3002 │
└────────┬────────┘     └─────┬────────┘     └───────┬────────┘
         │                    │                       │
         │                    │                       │
         └──────────────┬────┴───────────────────────┘
                       │
                 ┌─────▼─────┐
                 │           │
                 │  Banking  │
                 │   API     │
                 │           │
                 └───────────┘
```

## Technical Implementation

### 1. Backend Configuration

First, let's set up our Spring Security configuration to handle CORS properly:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors
                .configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/api/banking/balance", "/api/banking/transfer").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:3002"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### 2. API Endpoints

Our banking API implements secure endpoints with proper CORS support:

```java
@RestController
@RequestMapping("/api/banking")
public class BankingController {
    /**
     * Get Account Balance
     * 
     * Same-Origin Flow:
     * mybank.com ───▶ api.mybank.com/balance
     *   (No CORS check needed)
     * 
     * Cross-Origin Flow:
     * partner.com ──[CORS]──▶ api.mybank.com/balance
     */
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {
        // Simulate balance check
        return ResponseEntity.ok(new ApiResponse("success", "Balance: ₹10,000"));
    }
    
    /**
     * Transfer Funds
     * 
     * Preflight Flow:
     * 1. OPTIONS request
     *    partner.com ──[CORS]──▶ api.mybank.com/transfer
     * 2. Actual POST
     *    partner.com ──[CORS]──▶ api.mybank.com/transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> makeTransfer(@RequestBody TransferRequest request) {
        // Simulate transfer
        return ResponseEntity.ok(new ApiResponse("success", 
            String.format("Transferred ₹%d to account", request.getAmount())));
    }

    /**
     * Handle OPTIONS requests explicitly
     * Required for CORS preflight requests
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
```

### 3. Frontend Implementation

Here's our actual implementation of the mobile banking frontend with CORS-aware fetch calls:

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MyBank Mobile App</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .bank-card {
            background: linear-gradient(45deg, #2193b0, #6dd5ed);
            color: white;
            border-radius: 15px;
            padding: 20px;
            margin: 20px 0;
        }
        .balance-amount {
            font-size: 2em;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <div class="row">
            <div class="col-md-8 offset-md-2">
                <h1 class="mb-4">MyBank Mobile Banking</h1>
                <div class="bank-card">
                    <h3>Account Balance</h3>
                    <div id="balanceDisplay" class="balance-amount">Loading...</div>
                    <button onclick="checkBalance()" class="btn btn-light mt-3">Refresh Balance</button>
                </div>
                
                <div class="card mt-4">
                    <div class="card-body">
                        <h3>Transfer Money</h3>
                        <div class="mb-3">
                            <label for="amount" class="form-label">Amount (₹)</label>
                            <input type="number" class="form-control" id="amount">
                        </div>
                        <div class="mb-3">
                            <label for="toAccount" class="form-label">To Account</label>
                            <input type="text" class="form-control" id="toAccount">
                        </div>
                        <button onclick="transfer()" class="btn btn-primary">Transfer</button>
                    </div>
                </div>
                
                <div id="message" class="alert mt-3" style="display: none;"></div>
            </div>
        </div>
    </div>

    <script>
        const API_BASE = 'http://localhost:8080/api/banking';

        async function checkBalance() {
            try {
                const response = await fetch(`${API_BASE}/balance`, {
                    credentials: 'include'
                });
                const data = await response.json();
                document.getElementById('balanceDisplay').textContent = data.message;
                showMessage('success', 'Balance updated successfully');
            } catch (error) {
                showMessage('error', 'Failed to fetch balance: ' + error.message);
            }
        }

        async function transfer() {
            const amount = document.getElementById('amount').value;
            const toAccount = document.getElementById('toAccount').value;

            try {
                const response = await fetch(`${API_BASE}/transfer`, {
                    method: 'POST',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ amount, toAccount })
                });
                const data = await response.json();
                showMessage('success', data.message);
            } catch (error) {
                showMessage('error', 'Transfer failed: ' + error.message);
            }
        }

        function showMessage(type, text) {
            const msgDiv = document.getElementById('message');
            msgDiv.className = `alert alert-${type === 'success' ? 'success' : 'danger'} mt-3`;
            msgDiv.textContent = text;
            msgDiv.style.display = 'block';
            setTimeout(() => msgDiv.style.display = 'none', 5000);
        }

        // Initial balance check
        checkBalance();
    </script>
</body>
</html>
```

## Testing and Validation

We've implemented comprehensive test cases to ensure our CORS protection works as intended:

### 1. Mobile Banking App Balance Check
- **Request**: GET `/api/banking/balance`
- **Expected Outcome**: Successfully fetches balance with status "success"
- **Result**: Balance: ₹10,000

### 2. Financial Planner Transfer
- **Preflight Request**: OPTIONS `/api/banking/transfer`
- **Actual Transfer**: POST `/api/banking/transfer`
- **Expected Outcome**: Successfully transfers funds
- **Result**: Transferred ₹5000 to account

### 3. Investment Dashboard Portfolio Check
- **Request**: GET `/api/banking/balance`
- **Expected Outcome**: Successfully fetches balance
- **Result**: Balance: ₹10,000

### 4. Unauthorized Origin Access
- **Request**: Any request from unauthorized origin
- **Expected Outcome**: Request blocked
- **Result**: Invalid CORS request

## Best Practices and Security Considerations

1. **Explicit Origin Specification**
   - Never use wildcards (*) for origins
   - List each allowed origin explicitly

2. **Method Restrictions**
   - Only allow necessary HTTP methods
   - Include OPTIONS for preflight requests

3. **Header Controls**
   - Restrict allowed headers
   - Only expose required response headers

4. **Credential Handling**
   - Use allowCredentials wisely
   - Ensure secure cookie configurations

## Conclusion

Implementing CORS correctly is crucial for securing modern web applications, especially in the banking sector. This guide demonstrates how to achieve a balance between security and functionality, ensuring that your banking applications can communicate safely while preventing unauthorized access.

Remember:
- Always validate origins explicitly
- Test thoroughly across different scenarios
- Monitor and log CORS-related issues
- Keep security configurations up to date

The complete source code and additional resources are available in the project repository. Feel free to adapt this implementation to your specific needs while maintaining the security principles discussed.

---

Have you implemented CORS in your banking applications? Share your experiences and challenges in the comments below!
