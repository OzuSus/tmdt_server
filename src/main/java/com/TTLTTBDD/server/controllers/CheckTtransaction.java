package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.Helper.VnpayHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class CheckTtransaction {
    @GetMapping("/api/vnpay/check-transaction")
    public ResponseEntity<?> checkTransaction(@RequestParam("amount") int amount) {
        try {
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "querydr");
            vnp_Params.put("vnp_TmnCode", "9Q01DKS0");
            vnp_Params.put("vnp_TxnRef", "ORDER12345");
            vnp_Params.put("vnp_TransactionDate", "20250521121212");
            vnp_Params.put("vnp_CreateDate", "20250521121512");
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");

            String hashData = VnpayHelper.hashAllFields(vnp_Params); // tạo chuỗi query string
            String secureHash = VnpayHelper.hmacSHA512("MQCNL7TMYNFB9NQTXX9X4347UD87TPXK", hashData);
            vnp_Params.put("vnp_SecureHash", secureHash);

            // Gửi HTTP request tới VNPAY API endpoint
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://sandbox.vnpayment.vn/merchant_webapi/api/transaction"))
                    .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(vnp_Params)))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode json = new ObjectMapper().readTree(response.body());

            if ("00".equals(json.get("vnp_ResponseCode").asText()) &&
                    "00".equals(json.get("vnp_TransactionStatus").asText())) {
                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.ok(Map.of("success", false));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "error", e.getMessage()));
        }
    }



}
