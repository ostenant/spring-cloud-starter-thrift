package io.ostenant.rpc.thrift.client.exception;

public class ThriftClientRegistryException extends RuntimeException {

    public ThriftClientRegistryException(String message) {
        super(message);
    }

    public ThriftClientRegistryException(String message, Throwable t) {
        super(message, t);
    }
}
