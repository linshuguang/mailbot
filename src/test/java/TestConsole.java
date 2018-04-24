import com.me.service.ConsoleMain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.OutputStream;

/**
 * Created by kenya on 2017/12/29.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_console.xml"})
public class TestConsole {

    @Test
    public void TestServe() {

        String uid="kenya";
        String xmlPath="deploy/demo/demo_20171229.xml";
        String xslPath="mail.xsl";
        String xsdPath="mail.xsd";
        OutputStream outputStream = System.out;

        ConsoleMain consoleMain = new ConsoleMain();

        consoleMain.serve(uid, xmlPath, xslPath, xsdPath,
                outputStream, true);
    }

}
