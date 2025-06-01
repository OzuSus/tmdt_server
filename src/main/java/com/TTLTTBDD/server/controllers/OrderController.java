package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.OrderRequestDTO;
import com.TTLTTBDD.server.models.dto.OrderDetailDTO;
import com.TTLTTBDD.server.models.dto.OrderDTO;
//import com.TTLTTBDD.server.services.OderService;
import com.TTLTTBDD.server.models.dto.StatusDTO;
import com.TTLTTBDD.server.repositories.OrderDetailRepository;
import com.TTLTTBDD.server.repositories.OrderRepository;
import com.TTLTTBDD.server.services.OrderDetailService;
import com.TTLTTBDD.server.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam Integer idUser,
            @RequestParam Integer idPaymentMethop,
            @RequestParam String fullname,
            @RequestParam String address,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam Integer idDeliveryMethop) {
        try {
            orderService.placeOrder(idUser, idPaymentMethop, fullname, address, email, phone, idDeliveryMethop);
            return ResponseEntity.ok("Đặt đơn thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable int userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orderDetails/{orderId}")
    public ResponseEntity<List<OrderDetailDTO>> getOrderDetailsByOrderId(@PathVariable int orderId) {
        List<OrderDetailDTO> orders = orderService.getOrderDetailsByIdOder_Id(orderId);
        return ResponseEntity.ok(orders);
    }

    // API: Lấy tất cả orders kèm tổng giá trị
    @GetMapping
    public ResponseEntity<?> getAllOrdersWithTotalPrice() {
        try {
            return ResponseEntity.ok(orderService.getAllOrdersWithTotalPrice());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API: Thêm sản phẩm vào order
    @PutMapping("/{orderId}/add-product")
    public ResponseEntity<?> addProductToOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
            orderService.addProductToOrder(orderId, productId, quantity);
            return ResponseEntity.ok("Thêm sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/latest-id")
    public ResponseEntity<?> getLastOrderId() {
        try {
            Integer lastOrderId = orderService.getLastOrderId();
            return ResponseEntity.ok(Map.of("orderId", lastOrderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API: Xóa sản phẩm khỏi order
    @PutMapping("/{orderId}/remove-product")
    public ResponseEntity<?> removeProductFromOrder(
            @PathVariable Integer orderId,
            @RequestParam Integer productId) {
        try {
            orderService.removeProductFromOrder(orderId, productId);
            return ResponseEntity.ok("Xóa sản phẩm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API: Cập nhật trạng thái order
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestParam Integer statusId) {
        try {
            orderService.updateOrderStatus(orderId, statusId);
            return ResponseEntity.ok("Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API: Tăng/giảm số lượng sản phẩm trong order
    @PutMapping("/{orderId}/update-quantity")
    public ResponseEntity<?> updateProductQuantity(
            @PathVariable Integer orderId,
            @RequestParam Integer productId,
            @RequestParam Integer quantity) {
        try {
            orderService.updateProductQuantity(orderId, productId, quantity);
            return ResponseEntity.ok("Cập nhật số lượng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // API: Xóa order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Integer orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Xóa đơn hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}/details")
    public ResponseEntity<?> getAllOrderDetails(@PathVariable Integer orderId) {
        try {
            return ResponseEntity.ok(orderService.getAllOrderDetailsByOrderId(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        try {
            orderService.createOrder(orderRequest.getIdUser(), orderRequest.getIdPaymentMethop(), orderRequest.getProducts());
            return ResponseEntity.ok("Order created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @GetMapping("/statuses")
    public ResponseEntity<List<StatusDTO>> getAllStatuses() {
        List<StatusDTO> statuses = orderService.getAllStatuses();
        return ResponseEntity.ok(statuses);
    }
    @GetMapping("/filter-by-status/{statusId}/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatusAndUser(
            @PathVariable Integer statusId,
            @PathVariable Integer userId) {
        List<OrderDTO> orders = orderService.getOrdersByStatusAndUser(statusId, userId);
        return ResponseEntity.ok(orders);
    }
    @GetMapping("/check-purchased")
    public ResponseEntity<Boolean> checkUserPurchased(@RequestParam Integer userId, @RequestParam Integer productId) {
        boolean hasPurchased = orderService.hasUserPurchasedProduct(userId, productId);
        return ResponseEntity.ok(hasPurchased);
    }
    @GetMapping("/monthly-revenue")
    public ResponseEntity<?> getMonthlyRevenue(@RequestParam int year) {
        List<Object[]> data = orderService.getRevenuePerMonth(year);

        Map<Integer, Double> revenueMap = new HashMap<>();
        for (Object[] row : data) {
            Integer month = (Integer) row[0];
            Double revenue = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            revenueMap.put(month, revenue);
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Map<String, Object> map = new HashMap<>();
            map.put("month", month);
            map.put("revenue", revenueMap.getOrDefault(month, 0.0));
            response.add(map);
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/category-revenue")
    public ResponseEntity<?> getCategoryRevenue() {
        List<Object[]> data = orderDetailService.getRevenueByCategory();
        List<Map<String, Object>> response = data.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("category", row[0]);
            map.put("count", row[1]);
            return map;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/total-price/status/{statusId}")
    public ResponseEntity<Double> getTotalPriceByStatus(@PathVariable int statusId) {
        Double totalPrice = orderService.getTotalPriceByStatus(statusId);
        return ResponseEntity.ok(totalPrice != null ? totalPrice : 0.0);
    }

}

