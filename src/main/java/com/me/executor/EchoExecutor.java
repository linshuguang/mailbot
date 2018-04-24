package com.me.executor;

import com.me.context.Context;

public class EchoExecutor extends Executor {

    private String value;

    public EchoExecutor(String value ){
        this.value = value;
    }

    @Override
    public Object eval(Context context) {
        return value;
    }

}
