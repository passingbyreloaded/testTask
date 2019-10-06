package com.revolut.task.controller;

import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.revolut.task.ApplicationTest;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.math.BigDecimal;

import static com.revolut.task.Application.*;
import static com.revolut.task.util.AccountTestData.ACCOUNT1;
import static com.revolut.task.util.AccountTestData.ACCOUNT2;
import static com.revolut.task.util.AccountTestData.NOT_EXISTING_ACCOUNT;
import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitStop;
import static spark.Spark.stop;

public class TransferControllerTest {

    final static String TRANSFER_URL = "/transfer";
    final static String JSON_PATTERN = "{\"accountNumberFrom\":%s,\"accountNumberTo\":%s,\"amount\":%s}";

    @ClassRule
    public static SparkServer<ApplicationTest> testServer = new SparkServer<>(ApplicationTest.class);

    @AfterClass
    public static void stopServer() {
        stop();
        awaitStop();
    }

    @Test
    public void transfer_Ok() throws Exception {
        BigDecimal amount = new BigDecimal("50");
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), ACCOUNT1.getNumber(), amount),
                false);
        BigDecimal initialBalanceFrom = ACCOUNT2.getBalance();
        BigDecimal initialBalanceTo = ACCOUNT1.getBalance();
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(initialBalanceFrom.subtract(amount), ACCOUNT2.getBalance());
        assertEquals(initialBalanceTo.add(amount), ACCOUNT1.getBalance());
        assertEquals(OK, httpResponse.code());
        assertEquals("The amount transferred successfully", new String(httpResponse.body()));
    }

    @Test
    public void transfer_ValidationException_zeroAmount() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), ACCOUNT1.getNumber(), 0),
                false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(BAD_REQUEST, httpResponse.code());
        assertEquals("Invalid request: Amount must be positive number", new String(httpResponse.body()));
    }

    @Test
    public void transfer_ValidationException_negativeAmount() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), ACCOUNT1.getNumber(), -100),
                false);
        BigDecimal initialBalanceFrom = ACCOUNT2.getBalance();
        BigDecimal initialBalanceTo = ACCOUNT1.getBalance();
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(initialBalanceFrom, ACCOUNT2.getBalance());
        assertEquals(initialBalanceTo, ACCOUNT1.getBalance());
        assertEquals(BAD_REQUEST, httpResponse.code());
        assertEquals("Invalid request: Amount must be positive number", new String(httpResponse.body()));
    }

    @Test
    public void transfer_ValidationException_sameAccounts() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), ACCOUNT2.getNumber(), 100),
                false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(BAD_REQUEST, httpResponse.code());
        assertEquals("Invalid request: Accounts cannot be the same", new String(httpResponse.body()));
    }

    @Test
    public void transfer_ValidationException_nullField() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                "{\"accountNumberFrom\":" + ACCOUNT2.getNumber() +
                        ",\"amount\":100}",
                false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(BAD_REQUEST, httpResponse.code());
        assertEquals("Invalid request: Request fields cannot be null or empty", new String(httpResponse.body()));
    }

    @Test
    public void transfer_ValidationException_emptyField() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), "\"\"", 100),
                false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(BAD_REQUEST, httpResponse.code());
        assertEquals("Invalid request: Request fields cannot be null or empty", new String(httpResponse.body()));
    }

    @Test
    public void transfer_ValidationException_jsonParseFail() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), ACCOUNT1.getNumber(), ""),
                false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(BAD_REQUEST, httpResponse.code());
        assertEquals("Invalid request: ", new String(httpResponse.body()).substring(0, 17));
    }

    @Test
    public void transfer_AccountNotFound() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, NOT_EXISTING_ACCOUNT.getNumber(), ACCOUNT1.getNumber(), 50),
                false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(NOT_FOUND, httpResponse.code());
        assertEquals("No account for number " + NOT_EXISTING_ACCOUNT.getNumber(), new String(httpResponse.body()));
    }

    @Test
    public void transfer_ExceedingAmount() throws Exception {
        PostMethod post = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT2.getNumber(), ACCOUNT1.getNumber(), 150),
                false);
        BigDecimal initialBalanceFrom = ACCOUNT2.getBalance();
        BigDecimal initialBalanceTo = ACCOUNT1.getBalance();
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(initialBalanceFrom, ACCOUNT2.getBalance());
        assertEquals(initialBalanceTo, ACCOUNT1.getBalance());
        assertEquals(NOT_ACCEPTABLE, httpResponse.code());
        assertEquals("Amount exceeds balance", new String(httpResponse.body()));
    }
}
