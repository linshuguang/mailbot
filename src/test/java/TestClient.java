import com.me.client.ZipClient;
import com.me.service.ClientMain;
import com.me.service.ConsoleMain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.OutputStream;

/**
 * Created by kenya on 2018/1/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_client.xml"})
public class TestClient {

    @Autowired
    ZipClient zipClient;

    @Test
    public void TestServe() {

        zipClient.serve("E:\\project\\mailbot\\cases\\demo2","conf/demo.xml",System.out);
    }
}
