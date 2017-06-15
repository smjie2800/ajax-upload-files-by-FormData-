//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.twinkling.stream.util;

import cn.twinkling.stream.util.IoUtil;
import java.io.IOException;

public class TokenUtil {
    public TokenUtil() {
    }

    public static String generateToken(String name, String size) throws IOException {
        if(name != null && size != null) {
            int code = name.hashCode();

            try {
                String e = (code > 0?"A":"B") + Math.abs(code) + "_" + size.trim();
                IoUtil.storeToken(e);
                return e;
            } catch (Exception var4) {
                throw new IOException(var4);
            }
        } else {
            return "";
        }
    }
}
