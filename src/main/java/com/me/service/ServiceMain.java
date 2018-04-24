package com.me.service;

import com.me.server.JettyServer;
import com.me.utils.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by kenya on 2017/11/24.
 */
public class ServiceMain {

    private final Logger LOGGER = LoggerFactory.getLogger(ServiceMain.class);

    private boolean isDeliver=false;

    public boolean isDeliver() {
        return isDeliver;
    }

    public void setDeliver(boolean deliver) {
        isDeliver = deliver;
    }

    public void serve(int port){
        JettyServer jettyServer = new JettyServer();
        try {
            jettyServer.start(port);
        }catch (Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }


    private InputStream readXmlStream(String xml) throws Exception{
        return new ByteArrayInputStream(xml.getBytes());
    }
    private boolean checkBeforeTransform(String xsdPath, String xmlPath) throws Exception{

        if(xsdPath != null) {
            InputStream xmlStream = readXmlStream(xmlPath);
            InputStream xsdStream = FileUtils.readStreamInClassPath(xsdPath);
            return XSDValidator.validateXMLSchema(xsdStream, xmlStream);
        }else{
            LOGGER.debug("no xsd performed");
        }
        return true;
    }

    private boolean checkBeforeTransform(InputStream xmlStream, InputStream xsdStream) throws Exception{
        return XSDValidator.validateXMLSchema(xsdStream, xmlStream);
    }

    private String applyTransform(String xslPath, String xml) throws Exception{

        String content = "";
        if(xslPath != null) {
            InputStream xmlStream = readXmlStream(xml);
            InputStream xslStream = FileUtils.readStreamInClassPath(xslPath);
            content = XML2BEAN.Transform(xmlStream, xslStream);
            LOGGER.debug("xml2bean:{}", content);
        }else{
            LOGGER.debug("no xsl performed");
        }
        return content;
    }

    public void serve(String uid,String xml, String xslPath,
                      String xsdPath, OutputStream outputStream){
        try{

            boolean isValid = checkBeforeTransform(xsdPath, xml);
            if (!isValid) {
                LOGGER.debug("not valid xml");
                return;
            } else {
                LOGGER.debug("validation passed" );
            }
            String bean = applyTransform(xslPath, xml);

            LOGGER.debug("bean:{}",bean);
            System.out.println(bean);


            Object obj = BEAN2OBJECT.Transform(
                    isDeliver,
                    uid,
                    System.getProperty("user.dir"),
                    new ByteArrayResource(bean.getBytes()),
                    new ClassPathResource("applicationContext_service.xml")
            );

            String json = OBJECT2JSON.Transform(obj);
            //LOGGER.debug("json:{}",json);
            outputStream.write(json.getBytes());

        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }
    }

}
