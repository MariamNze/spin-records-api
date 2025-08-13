package com.gretacvdl.spin_records_api.exceptions;

public class OrderItemNotFoundException extends  RuntimeException {

    public OrderItemNotFoundException(String message) {
        super(message);
    }
}