package io.ostenant.rpc.thrift.server.wrapper;

import io.ostenant.rpc.thrift.server.exception.ThriftServerInstantiateException;

import java.util.Arrays;
import java.util.Objects;

public final class ThriftServiceWrapperFactory {

    public static ThriftServiceWrapper wrapper(final String thriftServiceId, String thriftServiceName, Object thriftService, double version) {
        ThriftServiceWrapper thriftServiceWrapper;
        if (version <= -1) {
            thriftServiceWrapper = new ThriftServiceWrapper(thriftServiceName, thriftService.getClass(), thriftService);
        } else {
            thriftServiceWrapper = new ThriftServiceWrapper(thriftServiceName, thriftService.getClass(), thriftService, version);
        }

        Class<?> thriftServiceIface = Arrays.stream(thriftService.getClass().getInterfaces())
                .filter(iface -> iface.getName().endsWith("$Iface"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No thrift Iface found on service"));

        thriftServiceWrapper.setType(thriftService.getClass());
        thriftServiceWrapper.setIfaceType(thriftServiceIface);

        final String signature = String.join("$", new String[]{
                thriftServiceId, thriftServiceIface.getDeclaringClass().getName(),
                String.valueOf(version)
        });

        thriftServiceWrapper.setThriftServiceSignature(signature);

        return thriftServiceWrapper;
    }

    public static ThriftServiceWrapper wrapper(final String thriftServiceId, String thriftServiceName, Class<?> type, Object thriftService, double version) {
        if (Objects.isNull(thriftService) || !Objects.equals(type, thriftService.getClass())) {
            throw new ThriftServerInstantiateException("Thrift service initializing in wrong way");
        }

        ThriftServiceWrapper thriftServiceWrapper;
        if (version <= -1) {
            thriftServiceWrapper = new ThriftServiceWrapper(thriftServiceName, thriftService.getClass(), thriftService);
        } else {
            thriftServiceWrapper = new ThriftServiceWrapper(thriftServiceName, thriftService.getClass(), thriftService, version);
        }

        Class<?> thriftServiceIface = Arrays.stream(type.getInterfaces())
                .filter(iface -> iface.getName().endsWith("$Iface"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No thrift IFace found on service"));

        thriftServiceWrapper.setIfaceType(thriftServiceIface);

        final String signature = String.join("$", new String[]{
                thriftServiceId, thriftServiceIface.getDeclaringClass().getName(),
                String.valueOf(version)
        });

        thriftServiceWrapper.setThriftServiceSignature(signature);

        return thriftServiceWrapper;
    }

}
