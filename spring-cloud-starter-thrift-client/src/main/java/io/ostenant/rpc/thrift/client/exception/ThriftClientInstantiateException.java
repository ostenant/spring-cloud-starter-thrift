package io.ostenant.rpc.thrift.client.exception;

public class ThriftClientInstantiateException extends RuntimeException {

    public ThriftClientInstantiateException(String message) {
        super(message);
    }

    public ThriftClientInstantiateException(String message, Throwable cause) {
        super(message, cause);
    }
}
