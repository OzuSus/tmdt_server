package com.TTLTTBDD.server.config;

public class VNPayConfig {
    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String vnp_TmnCode = "9Q01DKS0";
    public static String vnp_HashSecret = "T6GHIJ230LVDDT5X1WO1SOH5HS1X8GES";
    public static String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:3000/payment-return";
    public static String vnp_NotifyUrl = " https://41b1-14-169-26-36.ngrok-free.app/api/vnpay/payment-notify";
}
