package com.winshare.demo.proxyconfig;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * BeanDefinitionRegistryPostProcessor类型，在扫描完beanDefinition后BeanFactoryPostProcesser之前执行
 * 可以用来修改容器中beanDefinition信息
 * 后期可改为@import 好一些
 */
public class RemoteServiceScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String[] basePackages;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        RemoteServiceClassPathBeanDefinitionScanner scanner = new RemoteServiceClassPathBeanDefinitionScanner(registry);
        scanner.resetFilters(false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
        scanner.doScan(basePackages);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

}
