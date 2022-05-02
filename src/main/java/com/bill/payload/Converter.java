package com.bill.payload;

import com.bill.models.Bill;
import com.bill.models.EPacking;
import com.bill.payload.response.Product;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class Converter {

    public static Product toPayload(com.bill.models.Product product) {
        Product payload = new Product();
        if(null != product.getPacking())
            payload.setPacking(product.getPacking().name());
        BeanUtils.copyProperties(product, payload, "packing");
        return payload;
    }

    public static com.bill.models.Product fromPayload(Product product) {
        com.bill.models.Product model = new com.bill.models.Product();
        if(null != product.getPacking())
            model.setPacking(EPacking.valueOf(product.getPacking()));
        BeanUtils.copyProperties(product, model, "packing");
        return model;
    }

    public static com.bill.payload.request.Bill toPayload(Bill bill) {
        com.bill.payload.request.Bill payload = new com.bill.payload.request.Bill();
        BeanUtils.copyProperties(bill, payload, "products");
        if(CollectionUtils.isNotEmpty(bill.getProducts())) {
            List<Product> products = bill.getProducts().stream().map(Converter::toPayload)
                .collect(Collectors.toList());
            payload.setProducts(products);
        }
        return payload;
    }

    public static Bill fromPayload(com.bill.payload.request.Bill payload) {
        Bill bill = new Bill();
        BeanUtils.copyProperties(payload, bill, "products");
        if(CollectionUtils.isNotEmpty(payload.getProducts())) {
            List<com.bill.models.Product> products = payload.getProducts().stream().map(Converter::fromPayload)
                .collect(Collectors.toList());
            bill.setProducts(products);
        }
        return bill;
    }

}
