package com.bill.services;

import com.bill.payload.Converter;
import com.bill.payload.response.MessageResponse;
import com.bill.payload.response.Page;
import com.bill.payload.response.Product;
import com.bill.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    public ProductRepository productRepository;

    public Product create(com.bill.models.Product product) {
        product.setUpdatedAt(LocalDateTime.now());
        return Converter.toPayload(productRepository.save(product));
    }

    public Page<Product> getPage(int pageNo, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Pageable pg = PageRequest.of(pageNo, size, sort);
        org.springframework.data.domain.Page<com.bill.models.Product> page = productRepository.findAll(pg);
        Page<Product> p = new Page<>();
        p.setPage(pageNo);
        p.setSize(page.getSize());
        p.setData(page.getContent().stream().map(Converter::toPayload).collect(Collectors.toList()));
        p.setTotal(page.getTotalElements());
        return p;
    }

    public Product get(String id) {
        return Converter.toPayload(productRepository.findById(id).get());
    }

    public Product update(com.bill.models.Product product) {
        product.setUpdatedAt(LocalDateTime.now());
        return Converter.toPayload(productRepository.save(product));
    }

    public MessageResponse delete(String productId) {
        productRepository.deleteById(productId);
        return new MessageResponse(String.format("Product with {} has been deleted", productId));
    }

}
