package io.ostenant.rpc.thrift.client.cache;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class ThriftServiceMethodCacheManager {

    private static final Map<String, ThriftServiceMethodCache> methodCachedMap = Maps.newConcurrentMap();

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
        String targetClassName = targetClass.getName();
        ThriftServiceMethodCache methodCache = methodCachedMap.get(targetClassName);
        if (methodCache == null) {
            methodCache = new ThriftServiceMethodCache(targetClass);
            ThriftServiceMethodCache updateMethodCache = methodCachedMap.putIfAbsent(targetClassName, methodCache);
            if (Objects.nonNull(updateMethodCache)) {
                methodCache = updateMethodCache;
            }
        }
        return methodCache;
    }

}
