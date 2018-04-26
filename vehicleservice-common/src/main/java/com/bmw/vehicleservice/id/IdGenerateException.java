package com.bmw.vehicleservice.id;

public class IdGenerateException extends RuntimeException{

    public IdGenerateException() {
        super();
    }

    public IdGenerateException(String message) {
        super(message);
    }

    public IdGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdGenerateException(Throwable cause) {
        super(cause);
    }
}
