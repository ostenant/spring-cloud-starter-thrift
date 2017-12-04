package com.icekredit.rpc.thrift.client.cache;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Map;

public class ThriftServiceMethodCacheManager {

    private final static Map<String, ThriftServiceMethodCache> methodCachedMap = Maps.newConcurrentMap();

    private ThriftServiceMethodCacheManager() {
    }

    public static Method getMethod(Class<?> targetClass, String methodName, Class<?>... arguments) throws NoSuchMethodException {
        ThriftServiceMethodCache methodCache = putIfAbsent(targetClass);
        return methodCache.getMethod(methodName, arguments);
    }

    public static void put(Class<?> targetClass) {
        ThriftServiceMethodCache methodCache = new ThriftServiceMethodCache(targetClass);
        methodCachedMap.put(targetClass.getName(), methodCache);
    }

    public static ThriftServiceMethodCache putIfAbsent(Class<?> targetClass) {
        ThriftServiceMethodCache methodCache = methodCachedMap.get(targetClass.getName());
        if (methodCache == null) {
            methodCache = new ThriftServiceMethodCache(targetClass);
            methodCachedMap.put(targetClass.getName(), methodCache);
        }
        return methodCache;
    }

}
