package com.revolut.task.controller;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.revolut.task.ApplicationTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.revolut.task.controller.TransferControllerTest.JSON_PATTERN;
import static com.revolut.task.controller.TransferControllerTest.TRANSFER_URL;
import static com.revolut.task.util.AccountTestData.ACCOUNT1;
import static com.revolut.task.util.AccountTestData.ACCOUNT3;
import static com.revolut.task.util.AccountTestData.ACCOUNT4;
import static org.junit.Assert.assertEquals;
import static spark.Spark.awaitStop;
import static spark.Spark.stop;

@RunWith(ConcurrentTestRunner.class)
public class TransferControllerMultithreadedTest {

    private BigDecimal initialBalanceAccount4 = ACCOUNT4.getBalance();
    private BigDecimal initialBalanceAccount1 = ACCOUNT1.getBalance();
    private BigDecimal initialBalanceAccount3 = ACCOUNT3.getBalance();
    private int amount1 = 50;
    private int amount2 = 2000;
    private int threadsNumber = 4;

    @ClassRule
    public static SparkServer<ApplicationTest> testServer = new SparkServer<>(ApplicationTest.class);

    @AfterClass
    public static void stopServer() {
        stop();
        awaitStop();
    }

    @Test
    public void transfer() throws Exception {
        PostMethod post1 = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT4.getNumber(), ACCOUNT1.getNumber(), amount1),
                false);
        PostMethod post2 = testServer.post(TRANSFER_URL,
                String.format(JSON_PATTERN, ACCOUNT3.getNumber(), ACCOUNT1.getNumber(), amount2),
                false);
        testServer.execute(post1);
        testServer.execute(post2);
    }

    @After
    public void testCount() {
        assertEquals(initialBalanceAccount4.subtract((new BigDecimal(threadsNumber * amount1))), ACCOUNT4.getBalance());
        assertEquals(initialBalanceAccount1.add((new BigDecimal(threadsNumber * amount1 + 2 * amount2))), ACCOUNT1.getBalance());
        assertEquals(initialBalanceAccount3.subtract((new BigDecimal(2 * amount2))), ACCOUNT3.getBalance());
    }
}
