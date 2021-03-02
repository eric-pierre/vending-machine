package com.ai.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.fasterxml.jackson.databind.JsonNode;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MachineControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private JsonNode makeCall(String target) {
        return restTemplate.getForObject("http://localhost:" + port + target, JsonNode.class);
    }

    @Test
    void testGetInventory() {

        JsonNode results = makeCall("/inventory");
        for (int i = 0; i < 10; i++) {
            assertTrue(results.at("/data/" + i + "/location").isTextual());
            assertTrue(results.at("/data/" + i + "/quantity").isInt());
            assertTrue(results.at("/data/" + i + "/item/SKU").isInt());
            assertTrue(results.at("/data/" + i + "/item/price").isTextual());
            assertTrue(results.at("/data/" + i + "/item/name").isTextual());

        }

    }

    @Test
    void testInsertBalanceAndCancel() {
        insertThreeDollars();


        assertEquals("$3.00", makeCall("/balance").at("/balance").asText());
        assertEquals("$3.00", makeCall("/cancel").at("/money").asText());
        assertEquals("$0.00", makeCall("/balance").at("/balance").asText());


    }

    @Test
    void testBuyACrunchBar() {
        insertThreeDollars();
        JsonNode rslt = makeCall("/dispense?loc=A2");

        assertEquals("Crunch", rslt.at("/item/name").asText());
        assertEquals("$0.50", rslt.at("/money").asText());

    }

    @Test
    void testCheckItem() {
        JsonNode rslt = makeCall("/checkItem?loc=E2");
        assertEquals("Kit Kat", rslt.at("/data/item/name").asText());

    }

    private void insertThreeDollars() {

        makeCall("/insertMoney?c=USD&v=1");
        makeCall("/insertMoney?c=USD&v=1");
        JsonNode rslt = makeCall("/insertMoney?c=USD&v=1");


        assertEquals("$1.00", rslt.at("/money").asText());
        assertEquals("$3.00", rslt.at("/balance").asText());

    }
}
