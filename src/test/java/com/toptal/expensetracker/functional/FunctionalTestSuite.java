package com.toptal.expensetracker.functional;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author: Sergey Royz
 * Date: 03.01.2015
 */
@Slf4j
//@Ignore("Functional Tests")
public class FunctionalTestSuite {

    public static final String SERVICE_URL = "http://127.0.0.1:8080/expense-tracker/api";
    public static final String LOGIN_PATH = "/login";
    public static final String EXPENSES_PATH = "/expenses";
    public static final String REGISTER_PATH = "/register";
    public static final String UNSUBSCRIBE_PATH = "/unsubscribe";
    public static final String REPORT_PATH = "/weekly";

    public static final String USERNAME = "zjor.se@gmail.com";
    public static final String PASSWORD = "s3cr3t";
    public static final String EXPENSE_ID = "430ba9d5-1447-4cb0-a64a-f9ad1e1ecb52";

    private HttpClient httpClient;

    @Before
    public void setUp() {
        httpClient = HttpClientBuilder.create().build();
    }

    @Test
    public void shouldLogin() throws IOException {
        HttpResponse httpResponse = httpClient.execute(createLoginRequest(USERNAME, PASSWORD));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            fail(httpResponse.getStatusLine().toString());
        }
    }

    @Test
    public void shouldCreateExpense() throws IOException {
        HttpResponse httpResponse = httpClient.execute(createLoginRequest(USERNAME, PASSWORD));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            fail(httpResponse.getStatusLine().toString());
        }
        HttpPost createExpenseRequest = new HttpPost(SERVICE_URL + EXPENSES_PATH);
        createExpenseRequest.setEntity(HttpRequestUtils.createUrlEncodedEntity("description", "Tickets to museum", "comment", "National Museum of Technology", "amount", "5", "timestamp", "" + System.currentTimeMillis()));
        httpResponse = httpClient.execute(createExpenseRequest);
        System.out.println(httpResponse.getStatusLine());
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Test
    public void shouldEditExpense() throws IOException {
        HttpResponse httpResponse = httpClient.execute(createLoginRequest(USERNAME, PASSWORD));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            fail(httpResponse.getStatusLine().toString());
        }
        HttpPost editRequest = new HttpPost(SERVICE_URL + EXPENSES_PATH + "/" + EXPENSE_ID);
        editRequest.setEntity(HttpRequestUtils.createUrlEncodedEntity("description", "Breakfast", "comment", "Starbucks", "amount", "35", "timestamp", "" + System.currentTimeMillis()));
        httpResponse = httpClient.execute(editRequest);
        System.out.println(httpResponse.getStatusLine());
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Test
    public void shouldDeleteExpense() throws IOException {
        HttpResponse httpResponse = httpClient.execute(createLoginRequest(USERNAME, PASSWORD));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            fail(httpResponse.getStatusLine().toString());
        }
        HttpDelete editRequest = new HttpDelete(SERVICE_URL + EXPENSES_PATH + "/5a382580-fa10-4154-a8d8-f21e56228212");
        httpResponse = httpClient.execute(editRequest);
        System.out.println(httpResponse.getStatusLine());
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Test
    public void shouldFetchExpenses() throws IOException {
        HttpResponse httpResponse = httpClient.execute(createLoginRequest(USERNAME, PASSWORD));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            fail(httpResponse.getStatusLine().toString());
        }
        HttpGet editRequest = new HttpGet(SERVICE_URL + EXPENSES_PATH);
        httpResponse = httpClient.execute(editRequest);
        System.out.println(httpResponse.getStatusLine());
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Test
    public void shouldReturnWeeklyReport() throws IOException {
        HttpResponse httpResponse = httpClient.execute(createLoginRequest(USERNAME, PASSWORD));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            fail(httpResponse.getStatusLine().toString());
        }
        HttpGet editRequest = new HttpGet(SERVICE_URL + REPORT_PATH + "?date=" + System.currentTimeMillis());
        httpResponse = httpClient.execute(editRequest);
        System.out.println(httpResponse.getStatusLine());
        System.out.println(EntityUtils.toString(httpResponse.getEntity()));
    }

    @Test
    public void shouldRegisterAndLogin() throws IOException {
        HttpPost registerRequest = new HttpPost(SERVICE_URL + REGISTER_PATH);
        String email = "developer@host.com";
        String password = "s3cr3t";
        registerRequest.setEntity(HttpRequestUtils.createUrlEncodedEntity("email", email, "password", password));
        assertEquals(201, httpClient.execute(registerRequest).getStatusLine().getStatusCode());
        log.info("Registered");
        assertEquals(200, httpClient.execute(createLoginRequest(email, password)).getStatusLine().getStatusCode());
        log.info("Logged in");
    }

    @Test
    public void shouldLoginAndUnsubscribe() throws IOException {
        String email = "developer@host.com";
        String password = "s3cr3t";
        assertEquals(200, httpClient.execute(createLoginRequest(email, password)).getStatusLine().getStatusCode());
        log.info("Logged in");
        HttpDelete unsubscribeRequest = new HttpDelete(SERVICE_URL + UNSUBSCRIBE_PATH);
        assertEquals(200, httpClient.execute(unsubscribeRequest).getStatusLine().getStatusCode());
        log.info("Unsubscribed");
    }


    private HttpPost createLoginRequest(String username, String password) throws UnsupportedEncodingException {
        HttpPost loginRequest = new HttpPost(SERVICE_URL + LOGIN_PATH);
        loginRequest.setEntity(HttpRequestUtils.createUrlEncodedEntity("username", username, "password", password));
        return loginRequest;
    }

}
