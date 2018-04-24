package com.me.mail;

import com.me.context.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kenya on 2017/11/24.
 */

public class EMail implements Serializable{

    private Content content;

    private Recipient recipient;

    private Subject subject;

    //@JsonGetter
    public Content getContent() {
        return content;
    }

    //@JsonGetter
    public Recipient getRecipient(){
        return recipient;
    }

    //@JsonGetter
    public Subject getSubject(){
        return subject;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }


    public Object eval(Context context)
    {
        Object s = subject.eval(context);
        Object r = recipient.eval(context);
        Object c = content.eval(context);

        Map<String, Object> result = new HashMap<>();
        result.put("subject", s);
        result.put("recipient", r);
        result.put("content", c);


        return result;
    }
}
