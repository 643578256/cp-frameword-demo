package com.winshare.demo.proxyconfig;


public class RemoteInvocationException extends RuntimeException {
    private final String message;

    public RemoteInvocationException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
