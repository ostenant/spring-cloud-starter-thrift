package io.ostenant.rpc.thrift.client.exception;

public class ThriftApplicationException extends RuntimeException {

    public ThriftApplicationException(String message) {
        super(message);
    }

    public ThriftApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
