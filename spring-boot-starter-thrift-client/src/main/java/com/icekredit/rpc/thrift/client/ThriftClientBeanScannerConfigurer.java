package com.icekredit.rpc.thrift.client;

import com.icekredit.rpc.thrift.client.scanner.ThriftClientBeanScanner;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.*;

public class ThriftClientBeanScannerConfigurer implements ApplicationContextAware, BeanFactoryPostProcessor {

    private Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private static final String SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN = "spring.thrift.client.package-to-scan";

    private final static String DEFAULT_SCAN_PACKAGE = "";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry definitionRegistry = (BeanDefinitionRegistry) beanFactory;

        ThriftClientBeanScanner beanScanner = new ThriftClientBeanScanner(definitionRegistry);
        beanScanner.setResourceLoader(applicationContext);
        beanScanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
        beanScanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);

        RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(applicationContext.getEnvironment());
        setScannedPackages(beanScanner, propertyResolver.getProperty(SPRING_THRIFT_CLIENT_PACKAGE_TO_SCAN));
    }

    private void setScannedPackages(ThriftClientBeanScanner beanScanner, String basePackages) {
        if (StringUtils.isBlank(basePackages)) {
            beanScanner.scan(DEFAULT_SCAN_PACKAGE);
            return;
        }

        int delimiterIndex = StringUtils.indexOf(basePackages, ",");
        if (delimiterIndex > -1) {
            StringTokenizer tokenizer = new StringTokenizer(basePackages, ",");
            Set<String> packageToScanSet = new HashSet<>();
            while (tokenizer.hasMoreTokens()) {
                String subPackage = tokenizer.nextToken();
                packageToScanSet.add(subPackage);
                log.info("Subpackage {} is to be scanned by {}", subPackage, beanScanner);
            }

            List<String> packageToScanList = new ArrayList<>(packageToScanSet);
            String[] packagesToScan = packageToScanList.toArray(new String[packageToScanList.size()]);
            beanScanner.scan(packagesToScan);
        } else {
            log.info("Base package {} is to be scanned with {}", basePackages, beanScanner);
            beanScanner.scan(basePackages);
        }
    }

}
