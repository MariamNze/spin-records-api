package com.gretacvdl.spin_records_api.mappers;

import com.gretacvdl.spin_records_api.dtos.OrderDto;
import com.gretacvdl.spin_records_api.dtos.OrderItemDto;
import com.gretacvdl.spin_records_api.entities.Order;

import java.util.List;

public class OrderMapper {
    public static OrderDto toDto(Order order, String customerEmail, List<OrderItemDto> items) {
        return new OrderDto(
                order.getId(),
                customerEmail,
                order.getTotal(),
                order.getCreatedAt(),
                items
        );
    }
}