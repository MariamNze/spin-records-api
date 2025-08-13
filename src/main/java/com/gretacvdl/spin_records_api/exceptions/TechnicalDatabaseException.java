package com.gretacvdl.spin_records_api.exceptions;

public class TechnicalDatabaseException extends RuntimeException{

    public TechnicalDatabaseException(String message) {
        super(message);
    }

    public TechnicalDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

}