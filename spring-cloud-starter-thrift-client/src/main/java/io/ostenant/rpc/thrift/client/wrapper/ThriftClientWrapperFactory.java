package io.ostenant.rpc.thrift.client.wrapper;


import java.util.Arrays;
import java.util.Objects;

public final class ThriftClientWrapperFactory {

    private ThriftClientWrapperFactory() {
    }

    public static ThriftClientWrapper wrapper(final String thriftServiceId, String thriftServiceName, Object thriftClient, double version) {
        ThriftClientWrapper thriftServiceWrapper;

        if (version <= 0) {
            thriftServiceWrapper = new ThriftClientWrapper(thriftServiceName, thriftClient.getClass(), thriftClient);
        } else {
            thriftServiceWrapper = new ThriftClientWrapper(thriftServiceName, thriftClient.getClass(), thriftClient, version);
        }

        Class<?> clientClass = thriftClient.getClass().getSuperclass();
        if (Objects.isNull(clientClass)) {
            throw new IllegalStateException("No thrift Client found on service");
        }
        thriftServiceWrapper.setClientType(clientClass);

        Class<?> ifaceClass = Arrays.stream(clientClass.getDeclaringClass().getDeclaredClasses())
                .filter(clazz -> clazz.getName().endsWith("$Iface"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No thrift Iface found on service"));

        thriftServiceWrapper.setType(thriftClient.getClass());

        final String signature = String.join("$", new String[]{
                thriftServiceId,
                ifaceClass.getCanonicalName(),
                String.valueOf(version)
        });

        thriftServiceWrapper.setThriftServiceSignature(signature);

        return thriftServiceWrapper;
    }

}
