package com.checkout;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping ("/api")
public class apiController {
    private final Logger log = LoggerFactory.getLogger(apiController.class);
    private String xApiKey = "AQEyhmfxKonIYxZGw0m/n3Q5qf3VaY9UCJ14XWZE03G/k2NFikzVGEiYj+4vtN01BchqAcwQwV1bDb7kfNy1WIxIIkxgBw==-JtQ5H0iXtu8rqQMD6iAb33gf2qZeGKGhrMpyQAt9zsw=-3wAkV)*$kP%bCcSf";
    private String merchantaccount = "AdyenRecruitmentCOM";
    public static String res;
    @PostMapping("/getPaymentMethods")
    public ResponseEntity<String> paymentmethods()
    {
        String url = "https://checkout-test.adyen.com/v68/paymentMethods";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-Key", xApiKey);
        Map<String, Object> map = new HashMap<>();
        map.put("merchantAccount", merchantaccount);
        map.put("countryCode", "NL");
        map.put("shopperLocale", "nl-NL");
        String[] paymenttype = {"ideal","scheme"};
        map.put("allowedPaymentMethods",paymenttype);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        log.info("REST request to make Adyen payment details {}", response.getBody());
        return ResponseEntity.ok().body(response.getBody());
    }

    @PostMapping("/startPayment")
    public ResponseEntity<String> startPayment(@RequestBody Map<String, Object> data) throws JsonProcessingException {
        var paymentmethod = data.get("paymentMethod");
        String url = "https://checkout-test.adyen.com/v68/payments";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-Key", xApiKey);
        Map<String, Object> amount = new HashMap<>();
        amount.put("currency", "EUR");
        amount.put("value","20000");
        Map<String, Object> map = new HashMap<>();
        map.put("merchantAccount", merchantaccount);
        var orderid = "akshay_checkoutChallenge_" + UUID.randomUUID().toString();
        map.put("reference",orderid);
        map.put("paymentMethod",paymentmethod);
        map.put("amount", amount);
        map.put("channel","web");
        map.put("returnUrl","http://localhost:8080/completed/pending");
        var browserinfo = data.get("browserInfo");
        map.put("browserInfo", browserinfo);
        map.put("origin","http://localhost");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        res = response.getBody();
        return ResponseEntity.ok().body(response.getBody());
    }

    }


