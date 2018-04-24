package com.me.render;

import com.me.context.Context;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by kenya on 2017/12/18.
 */
public class RawRender extends Attachment {
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String path;

    @Override
    public Object eval(Context context, Object model){
        try {
            FileOutputStream fout = new FileOutputStream(makeRealPath(context, path));
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(model);
        }catch (Exception e){

        }
        return model;
    }


}
