package com.gretacvdl.spin_records_api.controllers;

import com.gretacvdl.spin_records_api.dtos.CartRequestDto;
import com.gretacvdl.spin_records_api.dtos.OrderDto;
import com.gretacvdl.spin_records_api.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/cart")
    public ResponseEntity<OrderDto> createOrderFromCart(@RequestBody @Valid CartRequestDto req) {
        return ResponseEntity.ok(orderService.cart(req));
    }

    @GetMapping("/orders/{email}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerEmail(@PathVariable String email) {
        return ResponseEntity.ok(orderService.orderListByEmail(email));
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.listAll());
    }
}
