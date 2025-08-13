package com.gretacvdl.spin_records_api.exceptions;

public class OrderNotFoundException extends  RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}