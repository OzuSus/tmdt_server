package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.*;
import com.TTLTTBDD.server.models.dto.ProductOrderDTO;
import com.TTLTTBDD.server.models.entity.*;
import com.TTLTTBDD.server.repositories.*;
import lombok.extern.java.Log;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OderService {

    @Autowired
    private OderRepository oderRepository;
    @Autowired
    private OderDetailRepository oderDetailRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    @Autowired
    private PaymentMethopRepository paymentMethopRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public void placeOrder(Integer idUser, Integer idPaymentMethop) {
        Cart cart = cartRepository.findByIdUser_Id(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Cart không tồn tại cho User này."));

        List<CartDetail> cartDetails = cartDetailRepository.findAllByIdCart_Id(cart.getId());
        if (cartDetails.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng trống, không thể đặt đơn.");
        }

        PaymentMethop paymentMethop = paymentMethopRepository.findById(idPaymentMethop)
                .orElseThrow(() -> new IllegalArgumentException("Phương thức thanh toán không hợp lệ."));
        Status status = statusRepository.findById(5)
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái không tồn tại."));

        Oder oder = new Oder();
        oder.setIdUser(cart.getIdUser());
        oder.setDateOrder(LocalDate.now());
        oder.setIdPaymentMethop(paymentMethop);
        oder.setIdStatus(status);

        oderRepository.save(oder);

        for (CartDetail cartDetail : cartDetails) {
            Product product = cartDetail.getIdProduct();
            Integer cartQuantity = cartDetail.getQuantity();

            BigDecimal price = BigDecimal.valueOf(product.getPrize());
            BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(cartQuantity));

            OderDetail oderDetail = new OderDetail();
            oderDetail.setIdOder(oder);
            oderDetail.setIdProduct(product);
            oderDetail.setQuantity(cartQuantity);
            oderDetail.setPrice(totalPrice.doubleValue());

            oderDetailRepository.save(oderDetail);
        }

        cartDetailRepository.deleteAll(cartDetails);
    }
    public List<OrderDTO> getOrdersByUserId(int userId) {
        List<Oder> orders = oderRepository.findByIdUser_Id(userId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<OderDetailDTO> getOrderDetailsByIdOder_Id(int orderId){
        List<OderDetail> orders = oderDetailRepository.findByIdOder_Id(orderId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Oder oder) {
        StatusDTO statusDTO = StatusDTO.builder()
                .id(oder.getIdStatus().getId())
                .name(oder.getIdStatus().getName())
                .build();
        List<OderDetailDTO> oderDetailDTOList = oderDetailRepository.findByIdOder_Id(oder.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        System.out.println("Found " + oderDetailDTOList.size() + " OderDetail(s) for order ID " + oder.getId());

        return OrderDTO.builder()
                .idOrder(oder.getId())
                .userId(oder.getIdUser().getId())
                .dateOrder(oder.getDateOrder())
                .paymentMethodName(oder.getIdPaymentMethop().getTypePayment())
                .statusName(statusDTO)
                .orderDetails(oderDetailDTOList)
                .build();
    }
    private OderDetailDTO convertToDTO(OderDetail oder) {
        ProductDTO productDTO = ProductDTO.builder()
                .id(oder.getIdProduct().getId())
                .name(oder.getIdProduct().getName())
                .price(oder.getIdProduct().getPrize())
                .quantity(oder.getIdProduct().getQuantity())
                .image(oder.getIdProduct().getImage())
                .description(oder.getIdProduct().getDescription())
                .reviewCount(oder.getIdProduct().getReview())
                .rating(oder.getIdProduct().getRating())
                .categoryID(oder.getIdProduct().getIdCategory().getId())
                .build();
        return OderDetailDTO.builder()
                .id(oder.getId())
                .idOder(oder.getIdOder().getId())
                .idProduct(productDTO)
                .quantity(oder.getQuantity())
                .totalprice(oder.getPrice())
                .build();
    }

    // Thêm phương thức này vào OderService
    public Integer getLastOrderId() {
        Oder lastOrder = oderRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new IllegalArgumentException("Không có đơn hàng nào trong cơ sở dữ liệu."));
        return lastOrder.getId();
    }


    // Lấy tất cả orders kèm tổng giá trị
    public List<Map<String, Object>> getAllOrdersWithTotalPrice() {
        List<Oder> orders = oderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("orderId", order.getId());
                    result.put("userId", order.getIdUser().getId());
                    result.put("dateOrder", order.getDateOrder());
                    result.put("paymentMethodId", order.getIdPaymentMethop().getId());
                    result.put("paymentMethodName", order.getIdPaymentMethop().getTypePayment());
                    result.put("statusId", order.getIdStatus().getId());
                    result.put("statusName", order.getIdStatus().getName());
                    result.put("totalPrice", calculateOrderTotalPrice(order.getId()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    // Thêm sản phẩm vào order
    public void addProductToOrder(Integer orderId, Integer productId, Integer quantity) {
        Oder order = oderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order không tồn tại."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại."));

        OderDetail existingDetail = oderDetailRepository.findAll()
                .stream()
                .filter(od -> od.getIdOder().getId().equals(orderId) && od.getIdProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingDetail != null) {
            existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
            existingDetail.setPrice(existingDetail.getPrice() + product.getPrize() * quantity);
            oderDetailRepository.save(existingDetail);
        } else {
            OderDetail newDetail = new OderDetail();
            newDetail.setIdOder(order);
            newDetail.setIdProduct(product);
            newDetail.setQuantity(quantity);
            newDetail.setPrice(product.getPrize() * quantity);
            oderDetailRepository.save(newDetail);
        }
    }

    // Xóa sản phẩm khỏi order
    public void removeProductFromOrder(Integer orderId, Integer productId) {
        OderDetail detail = oderDetailRepository.findAll()
                .stream()
                .filter(od -> od.getIdOder().getId().equals(orderId) && od.getIdProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Chi tiết sản phẩm không tồn tại trong order."));
        oderDetailRepository.delete(detail);
    }

    // Cập nhật trạng thái order
    public void updateOrderStatus(Integer orderId, Integer statusId) {
        Oder order = oderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order không tồn tại."));
        Status status = statusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Trạng thái không tồn tại."));
        order.setIdStatus(status);
        oderRepository.save(order);
    }

    // Tăng/giảm số lượng sản phẩm trong order
    public void updateProductQuantity(Integer orderId, Integer productId, Integer quantity) {
        OderDetail detail = oderDetailRepository.findAll()
                .stream()
                .filter(od -> od.getIdOder().getId().equals(orderId) && od.getIdProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Chi tiết sản phẩm không tồn tại trong order."));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0.");
        }

        detail.setQuantity(quantity);
        detail.setPrice(detail.getIdProduct().getPrize() * quantity);
        oderDetailRepository.save(detail);
    }

    // Xóa order
    public void deleteOrder(Integer orderId) {
        Oder order = oderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order không tồn tại."));
        List<OderDetail> details = oderDetailRepository.findAll()
                .stream()
                .filter(od -> od.getIdOder().getId().equals(orderId))
                .collect(Collectors.toList());
        oderDetailRepository.deleteAll(details);
        oderRepository.delete(order);
    }

    // Tính tổng giá trị đơn hàng (không lưu vào DB)
    public BigDecimal calculateOrderTotalPrice(Integer orderId) {
        List<OderDetail> details = oderDetailRepository.findAll()
                .stream()
                .filter(od -> od.getIdOder().getId().equals(orderId))
                .toList();
        return details.stream()
                .map(detail -> BigDecimal.valueOf(detail.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Map<String, Object>> getAllOrderDetailsByOrderId(Integer orderId) {
        // Kiểm tra xem order có tồn tại không
        Oder order = oderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order không tồn tại."));

        // Lấy danh sách chi tiết đơn hàng
        List<OderDetail> details = oderDetailRepository.findAll()
                .stream()
                .filter(od -> od.getIdOder().getId().equals(orderId))
                .toList();

        return details.stream()
                .map(detail -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("orderDetailId",detail.getId());result.put("orderId",order.getId());
                    result.put("productId",detail.getIdProduct().getId());
                    result.put("productName", detail.getIdProduct().getName());
                    result.put("quantity", detail.getQuantity());
                    result.put("unitPrice", BigDecimal.valueOf(detail.getIdProduct().getPrize()));
                    result.put("totalPrice", BigDecimal.valueOf(detail.getPrice()).toPlainString());
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void createOrder(Integer id_user, Integer id_payment_methop, List<ProductOrderDTO> products) {
        // Kiểm tra người dùng và phương thức thanh toán
        User user = userRepository.findById(id_user)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        PaymentMethop paymentMethop = paymentMethopRepository.findById(id_payment_methop)
                .orElseThrow(() -> new IllegalArgumentException("Payment method not found"));

        // Tạo Oder mới
        Oder newOrder = new Oder();
        newOrder.setIdUser(user);
        newOrder.setDateOrder(LocalDate.now());
        newOrder.setIdPaymentMethop(paymentMethop);
        newOrder.setIdStatus(statusRepository.findById(5)
                .orElseThrow(() -> new IllegalArgumentException("Status not found")));
        oderRepository.save(newOrder);

        // Xử lý từng sản phẩm
        for (ProductOrderDTO productRequest : products) {
            Product product = productRepository.findById(productRequest.getIdProduct())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            if (productRequest.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }

            // Tính giá totalPrice
            Double totalPrice = product.getPrize() * productRequest.getQuantity();

            // Tạo OderDetail
            OderDetail orderDetail = new OderDetail();
            orderDetail.setIdOder(newOrder);
            orderDetail.setIdProduct(product);
            orderDetail.setQuantity(productRequest.getQuantity());
            orderDetail.setPrice(totalPrice);
            oderDetailRepository.save(orderDetail);
        }
    }
}
