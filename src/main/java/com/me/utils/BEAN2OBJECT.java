package com.me.utils;

import com.me.consumer.ConsumerManager;
import com.me.context.Context;
import com.me.mail.EMail;
import com.me.resource.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.Resource;

/**
 * Created by kenya on 2017/11/24.
 */
public class BEAN2OBJECT {
    private static final Logger LOGGER = LoggerFactory.getLogger(BEAN2OBJECT.class);

    /*
    public static Object Transform(String uid, String workingPath,Resource...resources) {

        //Resource resource = new ByteArrayResource(contentXML.getBytes());
        //GenericXmlApplicationContext contentContext = new GenericXmlApplicationContext();
        //contentContext.load(new ByteArrayResource(contentXML.getBytes()));
        //contentContext.refresh();


        //Resource resource = new ByteArrayResource(contextXML.getBytes());
        //GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(contentContext);
        //applicationContext.load(new ByteArrayResource(contextXML.getBytes()));
        //applicationContext.refresh();

        //for (String content: contentXML){
        //    System.out.print(i + " ");
        //Resource contentResource = new ByteArrayResource(contentXML.getBytes());
        //Resource contextResource = new ByteArrayResource(contextXML.getBytes());

        //String[] strArray={"applicationContext_console.xml"};
        //ApplicationContext applicationContext = new ClassPathXmlApplicationContext(strArray, genericContext);

        GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(resources);
        //applicationContext.refresh();

        ResourceManager resourceManager = (ResourceManager) applicationContext.getBean(ResourceManager.class);
        Context context = new Context();
        context.setResourceManager(resourceManager);
        context.setUid(uid);
        context.setWorkingPath(workingPath);
        EMail myBean = (EMail) applicationContext.getBean(EMail.class);


        return myBean.eval(context);

    }
    */


    public static Object Transform(boolean isDeliver, String uid, String workingPath,Resource...resources) {

        GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(resources);
        ResourceManager resourceManager = (ResourceManager) applicationContext.getBean(ResourceManager.class);
        Context context = new Context();
        context.setResourceManager(resourceManager);
        context.setUid(uid);
        context.setWorkingPath(workingPath);
        EMail myBean = (EMail) applicationContext.getBean(EMail.class);


        Object obj = myBean.eval(context);

        if(isDeliver){
            ConsumerManager consumerManager = applicationContext.getBean(ConsumerManager.class);
            if(consumerManager!=null) {
                consumerManager.fetchConsumer(null).consume(context, obj);
            }
        }

        return obj;

    }

}
