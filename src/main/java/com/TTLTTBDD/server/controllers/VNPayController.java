package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.config.VNPayConfig;
import com.TTLTTBDD.server.models.dto.VNPayRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/vnpay")
public class VNPayController {

    @PostMapping("/create-payment")
    public ResponseEntity<String> createPayment(@RequestBody VNPayRequest request) {
        try {
            Long amount = request.getAmount();
            Long orderId = request.getOrderId();

            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
            String vnp_OrderInfo = "PlaceOrder" + vnp_TxnRef;
            String vnp_Amount = String.valueOf(amount * 100);
//            String vnp_BankCode = "VNPAYQR";
            String vnp_Locale = "vn";
            String vnp_CurrCode = "VND";
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String vnp_ReturnUrl = VNPayConfig.vnp_ReturnUrl;
            String vnp_NotifyUrl = VNPayConfig.vnp_NotifyUrl;
            String vnp_TxnTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_Locale", vnp_Locale);
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", "127.0.0.1");
            vnp_Params.put("vnp_CreateDate", vnp_TxnTime);
//            vnp_Params.put("vnp_BankCode", vnp_BankCode);
//            vnp_Params.put("vnp_NotifyUrl", vnp_NotifyUrl);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            for (String name : fieldNames) {
                String value = vnp_Params.get(name);
                if (hashData.length() > 0) {
                    hashData.append('&');
                    query.append('&');
                }
                hashData.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                query.append(name).append('=').append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            }
            String vnp_SecureHash = hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);
            String paymentUrl = VNPayConfig.vnp_Url + "?" + query.toString();
            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi tạo thanh toán: " + e.getMessage());
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes());
        return bytesToHex(bytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hash.append('0');
            hash.append(hex);
        }
        return hash.toString();
    }
    @PostMapping("/payment-notify")
    public ResponseEntity<String> paymentNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Enumeration<String> names = request.getParameterNames(); names.hasMoreElements(); ) {
            String name = names.nextElement();
            params.put(name, request.getParameter(name));
        }
        String secureHash = params.remove("vnp_SecureHash");
        String signData = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        try {
            String hash = hmacSHA512(VNPayConfig.vnp_HashSecret, signData);
            if (hash.equals(secureHash)) {
                String responseCode = params.get("vnp_ResponseCode");
                if ("00".equals(responseCode)) {
                    return ResponseEntity.ok("OK");
                }
            }
            return ResponseEntity.status(400).body("INVALID");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERROR");
        }
    }
}
