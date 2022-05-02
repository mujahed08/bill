package com.bill.services;

import com.bill.payload.Converter;
import com.bill.payload.response.MessageResponse;
import com.bill.payload.response.Page;
import com.bill.payload.request.Bill;
import com.bill.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class BillService {

    @Autowired
    public BillRepository billRepository;

    public Bill create(com.bill.models.Bill bill) {
        bill.setUpdatedAt(LocalDateTime.now());
        return Converter.toPayload(billRepository.save(bill));
    }

    public Page<Bill> getPage(int pageNo, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedAt");
        Pageable pg = PageRequest.of(pageNo, size, sort);
        org.springframework.data.domain.Page<com.bill.models.Bill> page = billRepository.findAll(pg);
        Page<Bill> p = new Page<>();
        p.setPage(pageNo);
        p.setSize(page.getSize());
        p.setData(page.getContent().stream().map(Converter::toPayload).collect(Collectors.toList()));
        p.setTotal(page.getTotalElements());
        return p;
    }

    public Bill get(String id) {
        return Converter.toPayload(billRepository.findById(id).get());
    }

    public Bill update(com.bill.models.Bill bill) {
        bill.setUpdatedAt(LocalDateTime.now());
        return Converter.toPayload(billRepository.save(bill));
    }

    public MessageResponse delete(String billId) {
        billRepository.deleteById(billId);
        return new MessageResponse(String.format("Bill with {} has been deleted", billId));
    }
}
