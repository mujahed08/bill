package com.bill.repository;

import com.bill.models.Product;
import com.bill.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
