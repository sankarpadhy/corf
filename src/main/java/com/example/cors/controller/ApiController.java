package com.example.cors.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/data")
    public Map<String, String> getData() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "This data is accessible from allowed origins");
        return data;
    }

    @PostMapping("/submit")
    public Map<String, String> submitData(@RequestBody Map<String, String> payload) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Data received: " + payload.toString());
        return response;
    }

    @Options
    public void handleOptions() {
        // This method handles OPTIONS requests
        // Spring will automatically add appropriate CORS headers
    }
}
