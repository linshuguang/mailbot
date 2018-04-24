package com.me.consumer;

import com.me.resource.Resource;
import com.me.user.UserProfile;

import java.util.*;

/**
 * Created by kenya on 2017/12/26.
 */
public class SimpleConsumerManager extends ConsumerManager {

    private Consumer consumer;

    public SimpleConsumerManager( ){
    }

    public SimpleConsumerManager( Consumer consumer){
        this.consumer = consumer;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public Consumer fetchConsumer(UserProfile userProfile){
        return consumer;
    }
}
