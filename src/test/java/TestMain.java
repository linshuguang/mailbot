/**
 * Created by kenya on 2017/12/12.
 */
import com.me.context.Context;
import com.me.executor.Executor;
import com.me.executor.SQLExecutor;
import com.me.render.ExcelRender;
import com.me.render.HtmlTable;
import com.me.render.PieRender;
import com.me.render.Render;
import com.me.resource.ResourceManager;
import com.me.service.ConsoleMain;
import com.me.service.ServiceMain;
import com.me.utils.FileUtils;
import com.me.utils.XSDValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestMain {

    @Autowired
    ResourceManager resourceManager ;


    private String rootPath = "e:/project/mailbot/deploy";


    @Test
    public void TestDummy() {
        try {
            File tempDir = FileUtils.createTempDir("server-upload");
            FileInputStream fin=new FileInputStream("E:\\project\\mailbot\\demo2.zip");
            String full = tempDir.getAbsolutePath();
            FileUtils.unzip(fin, full);
            String xsdPath = "mail.xsd";
            InputStream xmlStream = FileUtils.readStreamInFileSystem(full+"/"+"conf/demo.xml");
            InputStream xsdStream = FileUtils.readStreamInClassPath(xsdPath);
            boolean success = XSDValidator.validateXMLSchema(xsdStream, xmlStream);
            System.out.println(success);
        }catch (Exception e){
            e.printStackTrace();
        }
    }




    //@Test
    public void TestService() {
        ServiceMain serviceMain = new ServiceMain();
        serviceMain.serve(8096);
    }

    //@Test
    public void TestRecord(){
        System.setProperty("MAILBOT_ROOT_PATH",rootPath);
        System.setProperty("MAILBOT_TEMPLATE_PATH",rootPath+"/template");
        System.setProperty("MAILBOT_MAGIC_PATH",rootPath+"/bin/phantomjs/bin/magic.sh");
        System.setProperty("MAILBOT_MAGIC2_PATH",rootPath+"/bin/phantomjs/bin/magic2.sh");
        System.setProperty("MAILBOT_WORKING_PATH",rootPath);
        System.setProperty("MAILBOT_HTJS_PATH",rootPath+"/bin/highcharts/highcharts.js");
        System.setProperty("MAILBOT_HTCOVJS_PATH",rootPath+"/bin/highcharts/highcharts-convert.js");
        System.setProperty("MAILBOT_PHANTOMJS_PATH",rootPath+"/bin/phantomjs/bin/phantomjs.exe");

        Context context = new Context();
        context.setResourceManager(resourceManager);
        Executor executor = new SQLExecutor("select user,host from user order by user");

        Object data = executor.eval(context);
        HtmlTable render = new HtmlTable();
        render.setCols("0");

        Object o = render.eval(context, data);
    }

    //@Test
    public void TestPie(){
        /*
        System.setEnv("MAILBOT_ROOT_PATH",rootPath);
        System.setProperty("MAILBOT_TEMPLATE_PATH",rootPath+"/template");
        System.setProperty("MAILBOT_WORKING_PATH",rootPath+"/..");
        System.setProperty("MAILBOT_HTJS_PATH",rootPath+"/bin/highcharts/highcharts.js");
        System.setProperty("MAILBOT_HTCOVJS_PATH",rootPath+"/bin/highcharts/highcharts-convert.js");
        System.setProperty("MAILBOT_PHANTOMJS_PATH",rootPath+"/bin/phantomjs/bin/phantomjs.exe");
        */
        Context context = new Context();

        context.setTemplatePath(rootPath+"/template");
        context.setWorkingPath(rootPath+"/..");
        context.setHtjs(rootPath+"/bin/highcharts/highcharts.js");
        context.setHtcovjs(rootPath+"/bin/highcharts/highcharts-convert.js");
        context.setPhantomjs(rootPath+"/bin/phantomjs/bin/phantomjs.exe");


        context.setResourceManager(resourceManager);
        Executor executor = new SQLExecutor("select user, count(*) as val from mysql.user group by user");

        Object data = executor.eval(context);
        PieRender render = new PieRender();
        render.setTemplate("pie");

        Object o = render.eval(context, data);
    }


    //@Test
    public void TestExcel(){

        Context context = new Context();

        context.setTemplatePath(rootPath+"/template");
        context.setWorkingPath(rootPath+"/..");
        context.setHtjs(rootPath+"/bin/highcharts/highcharts.js");
        context.setHtcovjs(rootPath+"/bin/highcharts/highcharts-convert.js");
        context.setPhantomjs(rootPath+"/bin/phantomjs/bin/phantomjs.exe");

        context.setResourceManager(resourceManager);
        Executor executor = new SQLExecutor("select user, count(*) as val from mysql.user group by user");

        Object data = executor.eval(context);
        ExcelRender render = new ExcelRender();
        render.setName("example.xls");

        Object o = render.eval(context, data);
    }


}
