package com.me.mail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.me.context.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kenya on 2017/11/24.
 */
@JsonIgnoreProperties({ "prefix", "title", "postfix" })
public class Subject {

    private String prefix;

    private String title;

    private String postfix;

    @JsonValue
    private String qualifyTitle;


    //@JsonGetter("subject")
    public String getQualifyTitle() {
        return qualifyTitle;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPostfix(String postfix){
        this.postfix = postfix;
    }

    public Object eval(Context context){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);
        Date sevendaybefore = new Date(System.currentTimeMillis()-7*24*60*60*1000);

        postfix = postfix.replaceAll("%[cC][uU][rR][dD][aA][tT][eE]%",dateFormat.format(date));
        postfix = postfix.replaceAll("%[bB][iI][Zz][dD][aA][tT][eE]%",dateFormat.format(yesterday));
        postfix = postfix.replaceAll("%[bB][iI][Ww][dD][aA][tT][eE]%",dateFormat.format(sevendaybefore));

        qualifyTitle = prefix + "-" + title + "-" + postfix;
        return qualifyTitle;
    }
}
