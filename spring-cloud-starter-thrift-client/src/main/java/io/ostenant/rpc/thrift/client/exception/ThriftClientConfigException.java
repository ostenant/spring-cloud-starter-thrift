package io.ostenant.rpc.thrift.client.exception;

public class ThriftClientConfigException extends RuntimeException {

    public ThriftClientConfigException(String message) {
        super(message);
    }

    public ThriftClientConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
