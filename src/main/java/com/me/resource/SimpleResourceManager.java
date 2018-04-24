package com.me.resource;

import com.me.user.UserProfile;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kenya on 2017/11/30.
 */
public class SimpleResourceManager extends ResourceManager {

    private String defKey;

    private Map<String, Resource> resourceMap;

    public String getDefKey() {
        return defKey;
    }

    public void setDefKey(String defKey) {
        this.defKey = defKey;
    }

    public SimpleResourceManager( ){
        this.resourceMap = new HashMap<>();
    }

    public SimpleResourceManager( LinkedHashMap<String, Resource> resourceMap){
        this.resourceMap = resourceMap;
    }

    @Override
    public Resource fetchResource(UserProfile userProfile, String key){
        if(resourceMap.containsKey(key)){
            return resourceMap.get(key);
        }else if(resourceMap.containsKey(defKey)){
            return resourceMap.get(defKey);
        }
        return null;
    }
}
