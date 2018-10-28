package io.ostenant.rpc.thrift.client.cache;

import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThriftServiceMethodCache {

    private Map<String, Method> methodCachedMap = Maps.newHashMap();

    private final Class<?> cacheClass;

    public ThriftServiceMethodCache(Class<?> cacheClass) {
        this.cacheClass = cacheClass;
        Method[] declaredMethods = cacheClass.getDeclaredMethods();
        List<String> nonCachedMethods = new ArrayList<>();

        for (Method method : declaredMethods) {
            if (!method.isAccessible() && ((1 & method.getModifiers()) > 0) && ((8 & method.getModifiers()) == 0)) {
                put(method);
                nonCachedMethods.add(method.getName());
            }
        }

        for (String methodName : nonCachedMethods) {
            methodCachedMap.remove(methodName);
        }
    }

    private void put(Method method) {
        Type[] types = method.getParameterTypes();
        StringBuilder cachedKey = new StringBuilder(method.getName() + types.length);
        for (Type type : types) {
            String typeName = type.toString();
            if (typeName.startsWith("class ")) {
                typeName = typeName.substring(6);
            }
            cachedKey.append(typeName);
        }
        methodCachedMap.put(cachedKey.toString(), method);
    }

    public Method getMethod(String name, Class<?>... arguments) throws NoSuchMethodException {
        Method method = methodCachedMap.get(name);
        if (method == null) {
            StringBuilder nameBuilder = new StringBuilder(name + arguments.length);
            for (Class<?> argument : arguments) {
                nameBuilder.append(argument.getName());
            }
            name = nameBuilder.toString();
            method = methodCachedMap.get(name);
        }
        return method;
    }

    public Class<?> getCacheClass() {
        return cacheClass;
    }
}
