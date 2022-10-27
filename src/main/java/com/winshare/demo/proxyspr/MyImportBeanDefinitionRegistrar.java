package com.winshare.demo.proxyspr;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
       // importingClassMetadata.getAnnotationTypes()
     //   Set<String> annotationTypes = importingClassMetadata.getan();
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(ss.class.getName()));
        ClassPathBeanDefinitionScanner scanner = new MyClassPathBeanDefinitionScanner(registry);

        scanner.resetFilters(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(MyService.class));

        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(mapperScanAttrs.getString("basePackages"));
        candidateComponents.forEach(db->{
           if(db instanceof ScannedGenericBeanDefinition){
               BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(TestFactoyBean.class);

               String className = ((ScannedGenericBeanDefinition) db).getMetadata().getClassName();
               String name = beanNameGenerator.generateBeanName(db, registry);
               beanDefinitionBuilder.addPropertyValue("interfaceClass",className);
               beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
               registry.registerBeanDefinition(name,beanDefinitionBuilder.getBeanDefinition());
           }
        });

        //((MyClassPathBeanDefinitionScanner) scanner).doScan("com.youzan.cloud.zyyy.custom.biz.member");
    }
}
