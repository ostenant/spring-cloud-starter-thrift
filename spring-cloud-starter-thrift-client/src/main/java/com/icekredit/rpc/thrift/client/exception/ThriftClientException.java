package com.icekredit.rpc.thrift.client.exception;

public class ThriftClientException extends RuntimeException {

    public ThriftClientException(String message) {
        super(message);
    }

    public ThriftClientException(String message, Throwable t) {
        super(message, t);
    }
}
