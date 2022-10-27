package com.winshare.demo.proxyconfig;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ImportBeanDefinitionRegistrar的实现类，
 * 配合Import注解，这里是在解析配置类的时候往容器中添加RemoteServiceScannerConfigurer
 * 类型的BeanDefinition
 * 2.0版本
 */
public class RemoteServiceScannerRegister implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(RemoteServiceScanner.class.getName()));
        if (mapperScanAttrs != null) {
            List<String> basePackages = Arrays.stream(mapperScanAttrs.getStringArray("basePackages")).filter(StringUtils::hasLength).
                    collect(Collectors.toList());
            registryRemoteServiceBeanDefinitions(registry, basePackages, generateBaseBeanName(importingClassMetadata));
        }
    }

    /**
     * 注册远程服务beanDefinition
     *
     * @param registry
     * @param basePackages
     */
    private void registryRemoteServiceBeanDefinitions(BeanDefinitionRegistry registry, List<String> basePackages, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RemoteServiceScannerConfigurer.class);
        builder.addPropertyValue("basePackages", basePackages);
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata) {
        return importingClassMetadata.getClassName() + "#" + RemoteServiceScannerRegister.class.getSimpleName() + "#" + 0;
    }
}
