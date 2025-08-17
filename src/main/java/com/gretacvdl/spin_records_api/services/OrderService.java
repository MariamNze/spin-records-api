package com.gretacvdl.spin_records_api.services;

import com.gretacvdl.spin_records_api.daos.CustomerDao;
import com.gretacvdl.spin_records_api.daos.OrderDao;
import com.gretacvdl.spin_records_api.daos.OrderItemDao;
import com.gretacvdl.spin_records_api.daos.ProductDao;
import com.gretacvdl.spin_records_api.dtos.CartItemDto;
import com.gretacvdl.spin_records_api.dtos.CartRequestDto;
import com.gretacvdl.spin_records_api.dtos.OrderDto;
import com.gretacvdl.spin_records_api.dtos.OrderItemDto;
import com.gretacvdl.spin_records_api.entities.Customer;
import com.gretacvdl.spin_records_api.entities.Order;
import com.gretacvdl.spin_records_api.entities.OrderItem;
import com.gretacvdl.spin_records_api.entities.Product;
import com.gretacvdl.spin_records_api.exceptions.OutOfStockException;
import com.gretacvdl.spin_records_api.exceptions.ProductNotFoundException;
import com.gretacvdl.spin_records_api.mappers.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private CustomerDao customerDao;

    @Transactional
    public OrderDto cart(CartRequestDto req) {
        if (req == null || req.getEmail() == null || req.getEmail().isBlank() || req.getItems() == null || req.getItems().isEmpty()) {
            throw new IllegalArgumentException("Email et items requis");
        }

        Customer customer = customerDao.findByEmail(req.getEmail());

        // Preparation items
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItemsToInsert = new ArrayList<>();

        for (CartItemDto ci : req.getItems()) {
            Product product = productDao.findById(ci.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Le produit avec l'ID " + ci.getProductId() + " est introuvable");
            }

            if (ci.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantité invalide pour produit avec l'ID " + ci.getProductId());
            }
            // Check stock
            int available = productDao.getStock(product.getId());
            if (available < ci.getQuantity()) {
                throw new OutOfStockException("Stock épuisé pour " + product.getTitle());
            }
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(lineTotal);

            OrderItem oi = new OrderItem(null, null, product.getId(), ci.getQuantity(), product.getPrice());
            orderItemsToInsert.add(oi);
        }

        Order order = new Order(null, customer.getId(), total, null);
        order = orderDao.create(order);

        // Decrease stock and create order_item
        List<OrderItemDto> orderItemDto = new ArrayList<>();
        for (OrderItem oi : orderItemsToInsert) {
            productDao.decreaseStock(oi.getProductId(), oi.getQuantity());
            oi.setOrderId(order.getId());
            orderItemDao.create(oi);

            // Retrieve title for DTO
            Product product = productDao.findById(oi.getProductId());
            String productTitle = product != null ? product.getTitle() : null;
            orderItemDto.add(new OrderItemDto(oi.getId(), oi.getOrderId(), oi.getProductId(), oi.getQuantity(), oi.getUnitPrice(), productTitle));
        }

        return OrderMapper.toDto(order, customer.getEmail(), orderItemDto);
    }

    public List<OrderDto> orderListByEmail(String email) {
        List<Order> orders = orderDao.findByCustomerEmail(email);
        List<OrderDto> result = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItem> items = orderDao.findItemsWithProductTitle(order.getId());

            List<OrderItemDto> itemDto = new ArrayList<>();
            for (OrderItem oi : items) {
                Product product = productDao.findById(oi.getProductId());
                String productTitle = product != null ? product.getTitle() : null;
                itemDto.add(new OrderItemDto(oi.getId(), oi.getOrderId(), oi.getProductId(), oi.getQuantity(), oi.getUnitPrice(), productTitle));
            }

            Customer customer = customerDao.findByEmail(email);
            result.add(OrderMapper.toDto(order, customer.getEmail(), itemDto));
        }
        return result;
    }

    public List<OrderDto> listAll() {
        List<OrderDto> result = new ArrayList<>();

        List<Order> orders = orderDao.findAll();

        for (Order order : orders) {
            List<OrderItem> items = orderItemDao.findByOrderId(order.getId());
            List<OrderItemDto> itemDtos = new ArrayList<>();

            for (OrderItem item : items) {
                Product product = productDao.findById(item.getProductId());
                String productTitle = product != null ? product.getTitle() : "Produit inconnu";

                itemDtos.add(new OrderItemDto(
                        item.getId(),
                        item.getOrderId(),
                        item.getProductId(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        productTitle
                ));
            }

            Customer customer = customerDao.findById(order.getCustomerId());
            String email = customer != null ? customer.getEmail() : "client inconnu";

            result.add(OrderMapper.toDto(order, email, itemDtos));
        }

        return result;
    }
}