package com.me.mail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.me.context.Context;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by kenya on 2017/11/24.
 */
public class Recipient {

    //@JsonValue

    private Set<String> recipient;


    public void setRecipient(Set<String> recipient){
        this.recipient = recipient;
    }

    //@JsonGetter
    public Set<String> getRecipient(){
        return this.recipient;
    }

    public Object eval(Context context) {
        return new ArrayList<>(recipient);
    }
}
