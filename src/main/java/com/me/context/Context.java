package com.me.context;

import com.me.enums.EnvEnums;
import com.me.resource.Resource;
import com.me.resource.ResourceManager;
import com.me.user.UserProfile;
import org.springframework.stereotype.Service;

/**
 * Created by kenya on 2017/11/28.
 */
public class Context {

    //private ThreadLocal<String> myThreadLocal = new ThreadLocal<String>();
    private ResourceManager resourceManager;
    private String uid;
    private String workingPath;
    private String phantomjs;
    private String templatePath;
    private String htjs;
    private String htcovjs;
    private String reduceJs ="reduce.js";
    private String backBoneHtml ="backbone.html";

    public Context(){
        templatePath = System.getenv("MAILBOT_TEMPLATE_PATH");
        workingPath = System.getenv("MAILBOT_WORKING_PATH");
        htjs = System.getenv("MAILBOT_HTJS_PATH");
        htcovjs = System.getenv("MAILBOT_HTCOVJS_PATH");
        phantomjs = System.getenv("MAILBOT_PHANTOMJS_PATH");
    }

    public String getReduceJs() {
        return reduceJs;
    }

    public void setReduceJs(String reduceJs) {
        this.reduceJs = reduceJs;
    }

    public String getPhantomjs() {
        return phantomjs;
    }

    public void setPhantomjs(String phantomjs) {
        this.phantomjs = phantomjs;
    }


    public String getHtjs() {
        return htjs;
    }

    public void setHtjs(String htjs) {
        this.htjs = htjs;
    }

    public String getHtcovjs() {
        return htcovjs;
    }

    public void setHtcovjs(String htcovjs) {
        this.htcovjs = htcovjs;
    }



    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getWorkingPath() {
        return workingPath;
    }

    public void setWorkingPath(String workingPath) {
        this.workingPath = workingPath;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public String getUid() {
        return uid;
    }

    public String getBackBoneHtml(){ return backBoneHtml; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Resource fetchResource(String rid){
        UserProfile userProfile = new UserProfile();
        userProfile.setId(uid);
        return resourceManager.fetchResource(userProfile, rid);
    }

}
