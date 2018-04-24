package com.me.render;

import com.me.context.Context;
import com.me.utils.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.Formatter;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Created by kenya on 2017/11/28.
 */
public class Advance extends Render {

    private static final Logger LOGGER = LoggerFactory.getLogger(Advance.class);

    private String element;
    private String attribute;
    private String value;

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Object eval(Context context, Object model){
        String result="";
        try {
            InputStream fileIS = FileUtils.readStreamInFileSystem((String) model);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fileIS);
            XPath xPath = XPathFactory.newInstance().newXPath();

            StringBuffer sbuf = new StringBuffer();
            Formatter fmt = new Formatter(sbuf);

            String expression = "//";
            expression += element;
            expression += "[@";
            expression += attribute;
            fmt.format("=\"%s\"", value);
            expression += sbuf.toString();
            expression += "/descendant-or-self::*";

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            result = nodeList.item(0).toString()+"<br>";
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

        return result;
    }

}
