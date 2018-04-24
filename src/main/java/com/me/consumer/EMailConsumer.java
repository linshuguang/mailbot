package com.me.consumer;

/**
 * Created by kenya on 2017/12/13.
 */
import com.me.context.Context;
import com.me.controller.MailbotController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.MimeMessage;
import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

public class EMailConsumer extends Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMailConsumer.class);

    private String host;
    private int port;
    private String user;
    private String password;
    private String from;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void consume(Context context, Object obj){

        Map<String, Object> bulk = (Map<String, Object>) obj;
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", ""+port);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        try {

            List<String> recipientList = (List<String>)bulk.get("recipient");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(StringUtils.join(recipientList,",")));
            message.setSubject(bulk.get("subject").toString());



            Map<String,Object> contentMap = (Map<String,Object>)bulk.get("content");
            List<Object> contentList = (List<Object>)contentMap.get("records");

            StringBuffer stringBuffer = new StringBuffer();
            Formatter formatter = new Formatter(stringBuffer);
            for(Object o: contentList){
                if(o instanceof String){
                    formatter.format("%s",o.toString());
                }else if(o instanceof List){
                    List<Object> ol =(List<Object>)o;
                    for(Object oo: ol){
                        formatter.format("%s",oo.toString());
                    }
                }
            }
            //message.setText(stringBuffer.toString());
            message.setContent(stringBuffer.toString(), "text/html; charset=utf-8");
            Transport.send(message);
            LOGGER.info("well done");

        } catch (MessagingException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }


}
