package com.ai.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.ai.web.MachineControllerTest.InventoryResponse.InventoryItem;
import com.ai.web.MachineControllerTest.InventoryResponse.InventoryItem.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MachineControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetInventory() {
        InventoryResponse node = restTemplate.getForObject("http://localhost:" + port + "/inventory", InventoryResponse.class);
        node.getData().sort((v1, v2) -> v1.location.compareTo(v2.location));
        InventoryItem a2 = node.getData().get(1);
        assertEquals("A2", a2.location);
        assertEquals("Crunch", a2.getItem().getName());
        assertEquals("$2.50", a2.getItem().getPrice());
        assertEquals(10006, a2.getItem().getSku());
    }

    @Test
    void testInsertBalanceAndCancel() {
        insertThreeDollars();

        MoneyResponse node = restTemplate.getForObject("http://localhost:" + port + "/balance", MoneyResponse.class);
        assertEquals("$3.00", node.getBalance());
        node = restTemplate.getForObject("http://localhost:" + port + "/cancel", MoneyResponse.class);
        assertEquals("$3.00", node.getMoney());
        node = restTemplate.getForObject("http://localhost:" + port + "/balance", MoneyResponse.class);
        assertEquals("$0.00", node.getBalance());

    }

    @Test
    void testBuyACrunchBar() {
        insertThreeDollars();
        DispenseResponse node = restTemplate.getForObject("http://localhost:" + port + "/dispense?loc=A2", DispenseResponse.class);

        assertEquals("Crunch", node.getItem().getName());
        assertEquals("$0.50", node.getMoney());

    }

    @Test
    void testCheckItem() {
        JsonNode node = restTemplate.getForObject("http://localhost:" + port + "/checkItem?loc=E2", JsonNode.class);
        assertEquals("Kit Kat", node.at("/data/item/name").asText());

    }

    private void insertThreeDollars() {
        MoneyResponse node = restTemplate.getForObject("http://localhost:" + port + "/insertMoney?c=USD&v=1", MoneyResponse.class);
        node = restTemplate.getForObject("http://localhost:" + port + "/insertMoney?c=USD&v=1", MoneyResponse.class);
        node = restTemplate.getForObject("http://localhost:" + port + "/insertMoney?c=USD&v=1", MoneyResponse.class);

        assertEquals("$1.00", node.getMoney());
        assertEquals("$3.00", node.getBalance());

    }

    ///////////// Bunch of fake pojos to make the assertions above easier


    @Getter
    public static class DispenseResponse {

        private Item item;
        private String location;
        private String money;
    }

    @Getter
    public static class MoneyResponse {

        private String balance;
        private String money;
    }

    @Getter
    public static class InventoryResponse {

        private List<InventoryItem> data;

        @Getter

        public static class InventoryItem {

            private String location;
            private Integer quantity;
            private Item item;

            @Getter

            public static class Item {

                private String name;
                private String price;
                @JsonProperty("SKU")
                private Integer sku;

            }
        }
    }

}
