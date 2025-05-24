package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.OrderRequestDTO;
import com.TTLTTBDD.server.models.dto.OrderDetailDTO;
import com.TTLTTBDD.server.models.dto.OrderDTO;
//import com.TTLTTBDD.server.services.OderService;
import com.TTLTTBDD.server.models.dto.StatusDTO;
import com.TTLTTBDD.server.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @RequestParam Integer idUser,
            @RequestParam Integer idPaymentMethop,
            @RequestParam String fullname,
            @RequestParam String address,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam Double totalPrice) {
        try {
            orderService.placeOrder(idUser, idPaymentMethop, fullname, address, email, phone, totalPrice);
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
}

