package com.me.render;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.me.context.Context;
import com.me.utils.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.python.core.*;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import java.io.*;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import org.apache.commons.codec.binary.Base64;
/**
 * Created by kenya on 2017/12/18.
 */
public class GraphicRender extends Render {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphicRender.class);

    //private static PythonInterpreter interp = new PythonInterpreter();



    private String template;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    private String makeTemplate(Context context, Object model){

        try {
            ObjectMapper mapperObj = new ObjectMapper();
            String modelStr = mapperObj.writeValueAsString(model);

            String templatePath = context.getTemplatePath()+ File.separator + "template_"+template + ".json";
            String templateStr  = FileUtils.readStringInFileSystem(templatePath);

            String pythonPath = context.getTemplatePath()+ File.separator + template + ".py";
            InputStream inputStream = FileUtils.readStreamInFileSystem(pythonPath);
            if(inputStream.available()>0) {
                PythonInterpreter interpreter = new PythonInterpreter();
                interpreter.setErr(System.err);
                interpreter.setOut(System.out);
                interpreter.set("model", new PyString(modelStr));
                interpreter.set("template", new PyString(templateStr));
                interpreter.execfile(inputStream);
                PyObject result = interpreter.get("result");
                return result.toString();
            }
        }catch (Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;

    }
    private byte[] runMakeCharts(Context context, String infile){

        File file = createTempFile(".png");
        String outfile = file.getAbsolutePath();

        /*
        //phantomjs.exe highcharts-convert.js highcharts.js -infile pt.json -outfile a.png -constr Chart
        StringBuffer sBuf = new StringBuffer();
        Formatter fmt = new Formatter(sBuf);
        fmt.format("\"%s\"  \"%s\"  \"%s\" -infile \"%s\" -outfile \"%s\" -constr Chart",
                context.getPhantomjs(),
                context.getHtcovjs(),
                context.getHtjs(),
                infile,
                outfile
                );


        fmt.format("%s %s %s -infile \"%s\" -outfile \"%s\" -constr Chart",
                context.getPhantomjs(),
                context.getHtcovjs(),
                context.getHtjs(),
                infile,
                outfile
        );*/

        try {
            //String out = exec(context.getPhantomjs(),sBuf.toString());
            //String out = exec(sBuf.toString());
            String out = exec(context.getPhantomjs(),context.getHtcovjs(), context.getHtjs(), "-infile",
                    infile, "-outfile", outfile,"-constr Chart");

            File fi = new File(outfile);
            byte[] fileContent = Files.readAllBytes(fi.toPath());
            return fileContent;
        }catch (Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
        //"data:image/png;base64,$(base64 -w 0 $htpng)"

    }


    protected String draw(Context context, Object model){

        byte[] result =null;

        try {
            List modelList = (List) model;
            String content = makeTemplate(context, modelList.get(0));
            File tempFile = createTempFile(".json");
            FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();

            result = runMakeCharts(context, tempFile.getAbsolutePath());
            tempFile.deleteOnExit();
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }

        StringBuffer sBuf = new StringBuffer();
        Formatter fmt = new Formatter(sBuf);
        fmt.format("<img src=\"data:image/png;base64,%s\" alt=\"Red dot\" /><br/>", new String(Base64.encodeBase64(result)));
        return sBuf.toString();
    }

    @Override
    public Object eval(Context context, Object model){
        return draw(context, model);
    }
}
