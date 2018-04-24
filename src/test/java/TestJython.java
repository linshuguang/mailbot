import com.me.utils.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by kenya on 2017/12/18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class TestJython {

    @Test
    public void TestDummy() {
    }

    private PyObject python() {

        PythonInterpreter interp = new PythonInterpreter();
        //System.out.println("Hello, world from Java");
        //interp.execfile("hello.py");
        interp.set("a", new PyInteger(42));
        interp.exec("print a");
        interp.exec("x = 2+2");
        PyObject x = interp.get("x");
        return x;
    }

    //@Test
    public void TestCase() {

        PythonInterpreter interp = new PythonInterpreter();
        System.out.println("Hello, world from Java");

        try {
            interp.set("a", new PyInteger(42));

            interp.execfile(FileUtils.readStreamInClassPath("hello.py"));
            //interp.execfile("hello.py");
            //interp.exec("print a");
            //interp.exec("x = 2+2");
            PyObject x = interp.get("x");

        System.out.println("x: " + x);
        System.out.println("Goodbye ");
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    @Test
    public void TestCase222() {
        try {
            String t = FileUtils.readStringInClassPath("a.txt");
            int i = 0;
        }catch(Exception e){

        }

    }
}
