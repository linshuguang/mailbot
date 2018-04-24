package com.me.mail;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.me.context.Context;
import com.me.executor.Executor;
import com.me.render.Render;

/**
 * Created by kenya on 2017/11/24.
 */
public class Record {

    private  Render render=null;
    private Executor executor=null;

    @JsonGetter("view")
    public Render getRender() {
        return render;
    }

    @JsonGetter("model")
    public Executor getExecutor() {
        return executor;
    }

    public void setRender(Render render){

        this.render = render;
    }

    public void setExecutor(Executor executor){

        this.executor = executor;
    }

    public Object eval(Context context) {
        Object data = executor.eval(context);
        return render.eval(context, data);
    }
}
