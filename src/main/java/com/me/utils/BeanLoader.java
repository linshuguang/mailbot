package com.me.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by kenya on 2017/11/24.
 */
public class BeanLoader implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanLoader.applicationContext = applicationContext;
    }

    private static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> c) {
        return (T) BeanFactoryUtils.beanOfTypeIncludingAncestors(getApplicationContext(), c);
    }

    public static <T> T getBeanByName(String name,Class<T> c){
        return (T) applicationContext.getBean(name,c);
    }
}
