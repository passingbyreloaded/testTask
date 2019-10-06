package com.revolut.task.controller;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;
import com.despegar.sparkjava.test.SparkServer;
import com.revolut.task.ApplicationTest;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;

import static com.revolut.task.Application.NOT_FOUND;
import static com.revolut.task.Application.OK;
import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitStop;
import static spark.Spark.stop;
import static com.revolut.task.util.AccountTestData.*;

public class BalanceControllerTest {

    private final static String BALANCE_URL = "/balance/";

    @ClassRule
    public static SparkServer<ApplicationTest> testServer = new SparkServer<>(ApplicationTest.class);

    @AfterClass
    public static void stopServer() {
        stop();
        awaitStop();
    }

    @Test
    public void balance_Ok() throws Exception {
        GetMethod get = testServer.get(BALANCE_URL + ACCOUNT3.getNumber(), false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(OK, httpResponse.code());
        assertEquals(ACCOUNT3.getBalance().toString(), new String(httpResponse.body()));
    }

    @Test
    public void balance_AccountNotFound() throws Exception {
        GetMethod get = testServer.get(BALANCE_URL + NOT_EXISTING_ACCOUNT.getNumber(), false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(NOT_FOUND, httpResponse.code());
        assertEquals("No account for number " + NOT_EXISTING_ACCOUNT.getNumber(), new String(httpResponse.body()));
    }
}
