package com.bill.controllers;

import com.bill.payload.Converter;
import com.bill.payload.request.Bill;
import com.bill.payload.response.MessageResponse;
import com.bill.payload.response.Page;
import com.bill.services.BillService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    BillService billService;

    @Autowired
    TemplateEngine templateEngine;

    @PostMapping("/")
    public ResponseEntity<Bill> create(@Valid @RequestBody Bill bill) {
        Bill saved = billService.create(Converter.fromPayload(bill));
        return ResponseEntity.created(URI.create("/api/bill")).body(saved);
    }

    @GetMapping("/")
    public ResponseEntity<Page<Bill>> get(@RequestParam(defaultValue = "0") int pageNo, @RequestParam(defaultValue = "10") int size) {
        Page<Bill> page = billService.getPage(pageNo, size);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> get(@PathVariable String id) {
        Bill bill = billService.get(id);
        return ResponseEntity.ok(bill);
    }

    @PutMapping("/")
    public ResponseEntity<Bill> update(@Valid @RequestBody Bill bill) {
        Bill saved = billService.update(Converter.fromPayload(bill));
        return ResponseEntity.accepted().body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable String id) {
        MessageResponse msg = billService.delete(id);
        return ResponseEntity.ok(msg);
    }

    @GetMapping(value = "downloadTestPdf", produces = "application/json")
    public ResponseEntity<?> getDocsDetails() throws Exception{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfRendererBuilder builder = new PdfRendererBuilder();

        Context ctx = new Context();
        ctx.setVariable("name", "Mohammed Mujahed Khan");
        String html = templateEngine.process("bill", ctx);
        builder.withHtmlContent(html, null);
        //builder.withHtmlContent("<div><h1>Hello New</h1><p style='color:red;'>hello</p><img src='http://localhost:8080/GETURPRICE/images/seller-sent.jpg' title='Smiley Face' alt='Smiley face' height='42' width='42'></img></div>", null);
        // builder.withUri("http://localhost:8080/GETURPRICE/views/Directives/timeOutPopup.html");
        builder.toStream(baos);
        builder.run();

        HttpHeaders headers = new HttpHeaders();
        headers.setLastModified(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        headers.setCacheControl("no-cache");
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    }
}
