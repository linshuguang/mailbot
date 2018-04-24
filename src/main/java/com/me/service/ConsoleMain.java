package com.me.service;

import com.me.utils.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
/**
 * Created by kenya on 2017/11/27.
 */
public class ConsoleMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleMain.class);

    private boolean isFsRead;

    public ConsoleMain(){
        isFsRead = true;
    }

    public void setIsFsRead(boolean isFsRead){
        this.isFsRead = isFsRead;
    }

    private InputStream readXmlStream(String xmlPath) throws Exception{

        if(isFsRead) {
            return FileUtils.readStreamInFileSystem(xmlPath);
        }else{
            return FileUtils.readStreamInClassPath(xmlPath);
        }
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

    private String applyTransform(String xslPath, String xmlPath) throws Exception{

        String content = "";
        if(xslPath != null) {
            InputStream xmlStream = readXmlStream(xmlPath);
            String xsl = FileUtils.readStringInClassPath(xslPath);
            InputStream xslStream = FileUtils.readStreamInClassPath(xslPath);
            content = XML2BEAN.Transform(xmlStream, xslStream);
            //LOGGER.debug("xml2bean:{}", content);
            LOGGER.debug("xml2bean transform ok");
        }else{
            LOGGER.debug("no xsl performed");
        }
        return content;
    }

    public void serve(String uid,String xmlPath, String xslPath,
                             String xsdPath, OutputStream outputStream, boolean isDeliver){
        try{

            boolean isValid = checkBeforeTransform(xsdPath, xmlPath);
            if (!isValid) {
                LOGGER.debug("not valid xml");
                return;
            } else {
                LOGGER.debug("validation passed" );
            }


            LOGGER.debug("xmlPath:{}",xmlPath);
            String bean = applyTransform(xslPath, xmlPath);

            LOGGER.debug("bean:{}",bean);

            if(bean != null) {
                //System.out.println(bean);

                Object obj = BEAN2OBJECT.Transform(
                        isDeliver,
                        uid,
                        System.getProperty("user.dir"),
                        new ByteArrayResource(bean.getBytes()),
                        new ClassPathResource("applicationContext_console.xml")
                );

                String json = OBJECT2JSON.Transform(obj);
                //LOGGER.debug("json:{}", json);
                outputStream.write(json.getBytes());
            }else{
                LOGGER.error("transform xml error");
            }

        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }

}
