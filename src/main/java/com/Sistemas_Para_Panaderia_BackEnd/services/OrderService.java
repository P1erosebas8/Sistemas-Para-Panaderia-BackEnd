package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.OrderItemRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.OrderItemResponseDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.OrderRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.OrderResponseDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Order;
import com.Sistemas_Para_Panaderia_BackEnd.entities.OrderItem;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Product;
import com.Sistemas_Para_Panaderia_BackEnd.entities.User;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.OrderRepository;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.ProductRepository;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status("PENDIENTE")
                .totalAmount(BigDecimal.ZERO)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryPhone(request.getDeliveryPhone())
                .deliveryNotes(request.getDeliveryNotes())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequestDTO itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + product.getName());
            }

            product.setStock(product.getStock() - itemRequest.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        order = orderRepository.save(order);

        return mapToOrderResponseDTO(order);
    }

    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        order.setStatus(newStatus);
        order = orderRepository.save(order);
        
        return mapToOrderResponseDTO(order);
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getOrderItems().stream()
                .map(item -> OrderItemResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .imageUrl(item.getProduct().getImageUrl())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subTotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryPhone(order.getDeliveryPhone())
                .deliveryNotes(order.getDeliveryNotes())
                .items(itemDTOs)
                .build();
    }
}
