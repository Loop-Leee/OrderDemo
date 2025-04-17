package com.lloop.orderdemo.controller;

import com.lloop.orderdemo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/seckill")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{couponId}")
    public ResponseEntity<String> seckill(@PathVariable Long couponId, @RequestParam Long userId) {
        String result = orderService.seckill(couponId, userId);
        return ResponseEntity.ok(result);
    }
}