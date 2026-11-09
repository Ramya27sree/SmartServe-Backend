
package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Customer;
import com.example.demo.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerCustomer(
            @RequestBody Customer customer) {

        Customer savedCustomer =
                customerService.registerCustomer(customer);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                    "customerId", savedCustomer.getCustomerId(),
                    "customerName", savedCustomer.getCustomerName(),
                    "message", "Customer registered successfully"
                ));
    }
}
