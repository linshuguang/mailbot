package com.me.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by kenya on 2017/11/24.
 */
public class XML2BEAN {

    private static final Logger LOGGER = LoggerFactory.getLogger(XML2BEAN.class);

    public static String Transform(InputStream xmlStream, InputStream xslStream) {
        try {
            TransformerFactory tFac = TransformerFactory.newInstance();

            Source xslSource = new StreamSource(xslStream);
            Transformer t = tFac.newTransformer(xslSource);

            Source source = new StreamSource(xmlStream);

            StreamResult result = new StreamResult( new ByteArrayOutputStream());

            t.transform(source, result);

            //Charset charset = StandardCharsets.UTF_8;
            String content = result.getOutputStream().toString();
            content = content.replaceAll("xmlns[\\s]*=[\\s]*['\"][\\s]*['\"][\\s]*", "");
            return content;
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public static void main(String[] args) {


        String xmlPath = "/conf/demo.xml";
        String xslPath = "/mail.xsl";

        try {
            InputStream xmlStream = FileUtils.readStreamInClassPath(xmlPath);
            InputStream xslStream = FileUtils.readStreamInClassPath(xslPath);

            String content = Transform(xmlStream, xslStream);
            LOGGER.debug(content);
        }catch (Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

    }
}
