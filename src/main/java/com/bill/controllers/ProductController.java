package com.bill.controllers;

import com.bill.payload.Converter;
import com.bill.payload.request.LoginRequest;
import com.bill.payload.response.MessageResponse;
import com.bill.payload.response.Page;
import com.bill.payload.response.Product;
import com.bill.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        Product saved = productService.create(Converter.fromPayload(product));
        return ResponseEntity.created(URI.create("/api/product")).body(saved);
    }

    @GetMapping("/")
    public ResponseEntity<Page<Product>> get(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int size) {
        Page<Product> page = productService.getPage(pageNo, size);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable String id) {
        Product product = productService.get(id);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/")
    public ResponseEntity<Product> update(@Valid @RequestBody Product product) {
        Product saved = productService.update(Converter.fromPayload(product));
        return ResponseEntity.accepted().body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable String id) {
        MessageResponse msg = productService.delete(id);
        return ResponseEntity.ok(msg);
    }
}
