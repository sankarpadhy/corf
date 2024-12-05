package com.example.cors.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cors.model.TransferRequest;
import com.example.cors.model.ApiResponse;

/**
 * Banking API Controller with CORS Support
 * 
 * Request Flow:
 * ┌─────────────┐    ┌─────────────┐    ┌──────────────┐    ┌────────────┐
 * │ Client App  │───▶│   Browser   │───▶│ CORS Filter  │───▶│ Controller │
 * └─────────────┘    └─────────────┘    └──────────────┘    └────────────┘
 *        ▲                                                         │
 *        └─────────────────────────────────────────────────────────┘
 *                            Response Flow
 * 
 * Endpoints:
 * GET  /api/banking/balance   - Check account balance
 * POST /api/banking/transfer  - Transfer funds
 */
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
