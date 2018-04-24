package com.me.render;

import com.me.context.Context;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Formatter;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by kenya on 2017/12/18.
 */
public class RawImage extends GraphicRender {

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException{

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    @Override
    public Object eval(Context context, Object model){

        try
        {
            String encode = encodeFileToBase64Binary(model.toString());
            StringBuffer sBuf = new StringBuffer();
            Formatter fmt = new Formatter(sBuf);
            fmt.format("<img src=\"data:image/png;base64,%s\" alt=\"Red dot\" />", encode);
            return sBuf.toString();
        }catch (IOException e){

        }
        return null;
    }

}
