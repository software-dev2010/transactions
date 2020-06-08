package com.dgs.transactionpersistence.util;

import com.dgs.transactionpersistence.entity.Transaction;
import com.dgs.transactionpersistence.model.AccountStatement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.LinkedHashSet;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.dgs.transactionpersistence.util.TransactionsConstants.*;

public class GeneratePdfReport {

    public static ByteArrayInputStream pdfReport(AccountStatement accountStatement) {
        int i = 0;
        Set<String> transactions = new LinkedHashSet<>();
        transactions.add(IBAN_TO_IBAN_TRANSACTION_TYPE);
        transactions.add(IBAN_TO_WALLET_TRANSACTION_TYPE);
        transactions.add(WALLET_TO_IBAN_TRANSACTION_TYPE);
        transactions.add(WALLET_TO_WALLET_TRANSACTION_TYPE);
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            Paragraph paragraph = new Paragraph();
            String report = MessageFormat.format(
                    "1. Nume: {0}\n2. CNP: {1}\n3. IBAN: {2}\n\nTranzactii:\n",
                    accountStatement.getName(), accountStatement.getCnp(),
                    accountStatement.getIban());

            paragraph.add(new Chunk(report));
            for (String tr : transactions) {
                BigDecimal sum = new BigDecimal(0);
                i++;
                List<Transaction> transList = getNoTransactions(
                        accountStatement.getTransactionHistory(), tr);

                for (int j = 0; j < transList.size(); j++) {
                    if (transList.get(j).isPayment() == true) {
                        sum = sum.subtract(transList.get(j).getTransactionAmount());
                    } else {
                        sum = sum.add(transList.get(j).getTransactionAmount());
                    }
                }
                String newRow = "";
                if (transList.size() > 0) {
                    newRow = MessageFormat.format(
                            "{0}. {1} | {2} tranzactii | {3} RON\n",
                            i, tr, transList.size(), sum);

                    for (int k = 1; k <= transList.size(); k++) {
                        newRow = newRow.concat(MessageFormat.format(
                                "\b\b\b\b{0}. Detalii tranzactia {1}\n",
                                (char)(k + 96), k));

                        if (transList.get(k - 1).isPayment() == true) {
                            newRow = newRow.concat(MessageFormat.format(
                                    "\b\b\b\b\b\b- Plata: {0}\n",
                                    transList.get(k - 1).getTransactionAmount()));

                            newRow = newRow.concat(MessageFormat.format(
                                    "\b\b\b\b\b\b- Descriere: {0}\n",
                                    transList.get(k - 1).getDescription()));
                        } else {
                            newRow = newRow.concat(MessageFormat.format(
                                    "\b\b\b\b\b\b- Incasare: {0}\n",
                                    transList.get(k - 1).getTransactionAmount()));

                            newRow = newRow.concat(MessageFormat.format(
                                    "\b\b\b\b\b\b- Descriere: {0}\n",
                                    transList.get(k - 1).getDescription()));
                        }
                    }
                } else if (transList.size() == 0) {
                    newRow = MessageFormat.format("{0}. {1} | fara tranzactii\n", i, tr);
                }
                paragraph.add(new Chunk(newRow));
            }
            document.add(paragraph);
        } catch (DocumentException ex) {
            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private static List<Transaction> getNoTransactions(
            List<Transaction> transactionHistory, String transaction) {

        return transactionHistory.stream()
                .filter(e -> e.getTransactionType().equals(transaction))
                .collect(Collectors.toList());
    }
}