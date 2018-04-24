package com.me.render;

import com.me.context.Context;
import org.json.simple.JSONObject;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.util.*;

/**
 * Created by kenya on 2017/12/18.
 */
public class PieRender extends GraphicRender {
    @Override
    public Object eval(Context context, Object model){
        return draw(context, model);
    }
}
