package com.example.maws_restassured;

import groovy.json.JsonException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class TestCreateUser {

    private final String CONTEXT_PATH = "/mws";

    @BeforeEach
    void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 1130;
    }

    @Test
    final void testCreateUser() {

        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Vancouver");
        shippingAddress.put("country", "Canada");
        shippingAddress.put("streetName", "123 street name");
        shippingAddress.put("postalCode", "123456");
        shippingAddress.put("type", "shipping");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("city", "Vancouver");
        billingAddress.put("country", "Canada");
        billingAddress.put("streetName", "123 street name");
        billingAddress.put("postalCode", "123456");
        billingAddress.put("type", "shipping");

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Erfan");
        userDetails.put("lastName", "AkhavanRad");
        userDetails.put("email", "erfanakhavanrad@hotlook.com");
        userDetails.put("password", "ThisIsPassword6754");
        userDetails.put("addresses", userAddresses);


        Response response = given()
                .contentType("application/json")
                .accept("application/json")
                .body(userDetails)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract().response();

        response.jsonPath();

        String userId = response.jsonPath().getString("userID");
        assertNotNull(userId);
        assertTrue(userId.length() == 30);

        String bodyString = response.body().asString();
        try {
            JSONObject responseBodyJson = new JSONObject(bodyString);
            JSONArray addresses = responseBodyJson.getJSONArray("addresses");

            assertNotNull(addresses);
            assertTrue(addresses.length() == 2);

            String addressId = addresses.getJSONObject(0).getString("addressID");
            assertNotNull(addressId);
            assertTrue(addressId.length() == 30);

        } catch (JsonException | JSONException e) {
            fail(e.getMessage());
        }

    }
}
