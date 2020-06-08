package com.dgs.transactionpersistence.api;

import com.dgs.transactionpersistence.model.AccountStatement;
import com.dgs.transactionpersistence.request.AccountStatementRequest;
import com.dgs.transactionpersistence.service.TransactionService;
import com.dgs.transactionpersistence.util.GeneratePdfReport;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;

@RestController
@AllArgsConstructor
public class StatementController {

    private final TransactionService transactionService;

    @PostMapping(path = "/pdfreport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getStatement(@Valid @RequestBody AccountStatementRequest accountStatementRequest) {
        AccountStatement accountStatement = transactionService.getStatement(accountStatementRequest);

        ByteArrayInputStream bis = GeneratePdfReport.pdfReport(accountStatement);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=transactions.pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
