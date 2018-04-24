package com.me.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

/**
 * Created by kenya on 2017/11/24.
 */
public class XSDValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(XSDValidator.class);

    public static boolean validateXMLSchema(InputStream xsdStream, InputStream xmlStream){

        StreamSource xsdSource = new StreamSource(xsdStream);
        StreamSource xmlSource = new StreamSource(xmlStream);

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdSource);
            Validator validator = schema.newValidator();
            validator.validate(xmlSource);
        } catch (Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String xsdPath = "/mail.xsd";
        String xmlPath = "/deploy/demo/students.xml";


        try {
            InputStream xmlStream = FileUtils.readStreamInClassPath(xmlPath);
            InputStream xsdStream = FileUtils.readStreamInClassPath(xsdPath);

            boolean isValid = validateXMLSchema(xsdStream, xmlStream);
            if (isValid) {
                LOGGER.debug(" {} conformed with {}", xmlPath, xsdPath);
            } else {
                LOGGER.debug(" {} is invalid with {}", xmlPath, xsdPath);
            }
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

    }
}
