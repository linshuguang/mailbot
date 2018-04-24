package com.me.render;

import com.me.context.Context;

/**
 * Created by kenya on 2017/11/28.
 */
public class EchoRender extends Render {

    @Override
    public Object eval(Context context, Object model){
        return model;
    }

}
