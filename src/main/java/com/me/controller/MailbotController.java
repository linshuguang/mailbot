package com.me.controller;

import com.me.service.MainApp;
import com.me.service.ServiceMain;
import com.me.utils.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by kenya on 2017/12/15.
 */
@Controller
@RequestMapping(value = "/mailbot", method = {RequestMethod.GET, RequestMethod.POST})
public class MailbotController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailbotController.class);

    private Map<String,String> transform(String postData){
        String [] data = postData.split("&");
        Map<String, String> postMap = new HashMap<>();
        for( String d: data ){
            String [] kv = d.split("=");
            String key = URLDecoder.decode(kv[0]);
            String val = URLDecoder.decode(kv[1]);
            postMap.put(key,val);
        }
        return postMap;
    }

    @RequestMapping(value = "/echo",method = {RequestMethod.POST})
    public String echo(ModelMap model,@RequestBody String postData) {
        Map<String, String> postMap = transform(postData);
        model.addAttribute("message", "Hello "+ postMap.get("code"));
        return "echo";
    }

    @RequestMapping(value = "/run")
    public String exec(ModelMap model,@RequestBody String postData) {

        String uid      = MainApp.DEFAULT_UID;
        String xslPath  = MainApp.DEFAULT_XSLPATH;
        String xsdPath  = MainApp.DEFAULT_XSDPATH;

        Map<String, String> postMap = transform(postData);
        String xml = postMap.get("code");

        ServiceMain serviceMain = new ServiceMain();
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        serviceMain.serve(uid, xml, xslPath,xsdPath, outputStream);
        model.addAttribute("result", ""+ outputStream.toString());
        //return new ModelAndView("mailbot");
        return "mailbot";
    }





    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    String upload(@RequestParam("entry") String entry,
                                @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {
            try {
                entry = "conf/demo.xml";
                byte[] bytes = file.getBytes();
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                File tempDir = FileUtils.createTempDir("server-upload");
                FileUtils.unzip(bis, tempDir.getAbsolutePath());
                String uid      = MainApp.DEFAULT_UID;
                String xslPath  = MainApp.DEFAULT_XSLPATH;
                String xsdPath  = MainApp.DEFAULT_XSDPATH;

                ServiceMain serviceMain = new ServiceMain();
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                String pathName = tempDir.getAbsolutePath()+"/"+entry;
                String xml = FileUtils.readStringInFileSystem(pathName);
                //LOGGER.info(pathName);

                xml = xml.replace("\uFEFF", "");
                LOGGER.debug("----\n"+xml+"\n----");
                serviceMain.setDeliver(true);
                serviceMain.serve(uid, xml, xslPath,xsdPath, outputStream);

                return outputStream.toString();
                //return "ok";
            } catch (Exception e) {
                LOGGER.error(ExceptionUtils.getStackTrace(e));
                return "You failed to upload";
            }
        } else {
            return "You failed to upload because the file was empty.";
        }
    }


}
