package io.ostenant.rpc.thrift.server.processor;

import io.ostenant.rpc.thrift.server.exception.ThriftServerInstantiateException;
import io.ostenant.rpc.thrift.server.wrapper.ThriftServiceWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.thrift.TProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TRegisterProcessorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(TRegisterProcessorFactory.class);

    private static class TRegisterProcessorHolder {
        private static final TRegisterProcessor REGISTER_PROCESSOR = new TRegisterProcessor();
    }

    static TRegisterProcessor getRegisterProcessor() {
        return TRegisterProcessorHolder.REGISTER_PROCESSOR;
    }

    @SuppressWarnings("unchecked")
    public static TRegisterProcessor registerProcessor(List<ThriftServiceWrapper> serviceWrappers) throws NoSuchMethodException {
        if (CollectionUtils.isEmpty(serviceWrappers)) {
            throw new ThriftServerInstantiateException("No thrift service wrapper found");
        }

        TRegisterProcessor registerProcessor = getRegisterProcessor();
        registerProcessor.setProcessorMap(new HashMap<>(serviceWrappers.size()));
        register(serviceWrappers, registerProcessor);
        LOGGER.info("Multiplexed processor totally owns {} service processors", registerProcessor.processorMetaMap.size());

        return registerProcessor;
    }

    private static void register(List<ThriftServiceWrapper> serviceWrappers, TRegisterProcessor registerProcessor) throws NoSuchMethodException {
        for (ThriftServiceWrapper serviceWrapper : serviceWrappers) {
            Object bean = serviceWrapper.getThriftService();
            Class<?> ifaceClass = serviceWrapper.getIfaceType();

            if (Objects.isNull(ifaceClass)) {
                ifaceClass = Stream.of(ClassUtils.getAllInterfaces(bean))
                        .filter(clazz -> clazz.getName().endsWith("$Iface"))
                        .filter(iFace -> iFace.getDeclaringClass() != null)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No thrift IFace found on implementation"));
            }

            Class<TProcessor> processorClass = Stream.of(ifaceClass.getDeclaringClass().getDeclaredClasses())
                    .filter(clazz -> clazz.getName().endsWith("$Processor"))
                    .filter(TProcessor.class::isAssignableFrom)
                    .findFirst()
                    .map(processor -> (Class<TProcessor>) processor)
                    .orElseThrow(() -> new IllegalStateException("No thrift IFace found on implementation"));

            Constructor<TProcessor> processorConstructor = processorClass.getConstructor(ifaceClass);

            TProcessor singleProcessor = BeanUtils.instantiateClass(processorConstructor, bean);
            String serviceSignature = serviceWrapper.getThriftServiceSignature();

            registerProcessor.processorMetaMap.putIfAbsent(serviceSignature, serviceWrapper);
            LOGGER.info("Processor bean {} with signature [{}] is instantiated", singleProcessor, serviceSignature);
            registerProcessor.registerProcessor(serviceSignature, singleProcessor);
            LOGGER.info("Single processor {} register onto multiplexed processor with signature [{}]", singleProcessor, serviceSignature);
        }
    }
}
