package qau.cjn.flysnail.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

public class StringUtil {
	public static String string(String nameString){
    String split = ".";
    StringTokenizer token = new StringTokenizer(nameString, split);
    while (token.hasMoreTokens()) {
    	nameString=token.nextToken();
    	break;
    	}
    return nameString;
    }
    public static byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }
}

