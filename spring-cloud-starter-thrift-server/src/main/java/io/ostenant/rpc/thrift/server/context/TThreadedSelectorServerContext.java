package io.ostenant.rpc.thrift.server.context;

import io.ostenant.rpc.thrift.server.argument.TThreadedSelectorServerArgument;
import io.ostenant.rpc.thrift.server.properties.ThriftServerProperties;
import io.ostenant.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class TThreadedSelectorServerContext implements ContextBuilder {

    private static TThreadedSelectorServerContext serverContext;
    private TThreadedSelectorServer.Args args;

    private TThreadedSelectorServerContext() {
    }

    public static TThreadedSelectorServerContext context() {
        if (Objects.isNull(serverContext)) {
            serverContext = new TThreadedSelectorServerContext();
        }
        return serverContext;
    }

    @Override
    public ContextBuilder prepare() {
        return context();
    }

    @Override
    public TServer buildThriftServer(ThriftServerProperties properties,
                                     List<ThriftServiceWrapper> serviceWrappers)
            throws TTransportException, IOException {
        serverContext = (TThreadedSelectorServerContext) prepare();
        serverContext.args = new TThreadedSelectorServerArgument(serviceWrappers, properties);
        return new TThreadedSelectorServer(serverContext.args);
    }

}
