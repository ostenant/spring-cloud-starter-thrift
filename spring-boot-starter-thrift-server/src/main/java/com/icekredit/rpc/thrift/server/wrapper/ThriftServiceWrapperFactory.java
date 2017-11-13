package com.icekredit.rpc.thrift.server.wrapper;

import com.icekredit.rpc.thrift.server.exception.ThriftServerInstantiateException;

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

        Class<?> thriftServiceIFace = Arrays.stream(thriftService.getClass().getInterfaces())
                .filter(iFace -> iFace.getName().endsWith("$Iface"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No thrift IFace found on service"));

        thriftServiceWrapper.setType(thriftService.getClass());
        thriftServiceWrapper.setIfaceType(thriftServiceIFace);

        final String signature = String.join("$", new String[]{
                thriftServiceId, thriftServiceIFace.getDeclaringClass().getName(),
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

        Class<?> thriftServiceIFace = Arrays.stream(type.getInterfaces())
                .filter(iFace -> iFace.getName().endsWith("$Iface"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No thrift IFace found on service"));

        thriftServiceWrapper.setIfaceType(thriftServiceIFace);

        final String signature = String.join("$", new String[]{
                thriftServiceId, thriftServiceIFace.getDeclaringClass().getName(),
                String.valueOf(version)
        });

        thriftServiceWrapper.setThriftServiceSignature(signature);

        return thriftServiceWrapper;
    }

}
