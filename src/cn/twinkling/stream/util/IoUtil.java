//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.twinkling.stream.util;

import cn.twinkling.stream.config.Configurations;
import cn.twinkling.stream.servlet.Range;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * @from http://www.twinkling.cn/
 */
public class IoUtil {
    static final Pattern RANGE_PATTERN = Pattern.compile("bytes \\d+-\\d+/\\d+");

    public IoUtil() {
    }

    public static File getFile(String filename) throws IOException {
        if(filename != null && !filename.isEmpty()) {
            String name = filename.replaceAll("/", Matcher.quoteReplacement(File.separator));
            File f = new File(Configurations.getFileRepository() + File.separator + name);
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            if(!f.exists()) {
                f.createNewFile();
            }

            return f;
        } else {
            return null;
        }
    }

    public static File getTokenedFile(String key) throws IOException {
        if(key != null && !key.isEmpty()) {
            File f = new File(Configurations.getFileRepository() + File.separator + key);
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            if(!f.exists()) {
                f.createNewFile();
            }

            return f;
        } else {
            return null;
        }
    }

    public static void storeToken(String key) throws IOException {
        if(key != null && !key.isEmpty()) {
            File f = new File(Configurations.getFileRepository() + File.separator + key);
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            if(!f.exists()) {
                f.createNewFile();
            }

        }
    }

    public static void close(Closeable stream) {
        try {
            if(stream != null) {
                stream.close();
            }
        } catch (IOException var2) {
            ;
        }

    }

    public static Range parseRange(HttpServletRequest req) throws IOException {
        String range = req.getHeader("content-range");
        Matcher m = RANGE_PATTERN.matcher(range);
        if(m.find()) {
            range = m.group().replace("bytes ", "");
            String[] rangeSize = range.split("/");
            String[] fromTo = rangeSize[0].split("-");
            long from = Long.parseLong(fromTo[0]);
            long to = Long.parseLong(fromTo[1]);
            long size = Long.parseLong(rangeSize[1]);
            return new Range(from, to, size);
        } else {
            throw new IOException("Illegal Access!");
        }
    }

    public static long streaming(InputStream in, String key, String fileName) throws IOException {
        FileOutputStream out = null;
        File f = getTokenedFile(key);

        try {
            out = new FileOutputStream(f);
            boolean dst = false;
            byte[] length = new byte[10485760];

            while(true) {
                int dst1;
                if((dst1 = in.read(length)) == -1) {
                    out.flush();
                    break;
                }

                out.write(length, 0, dst1);
            }
        } finally {
            close(out);
        }

        File dst2 = getFile(fileName);
        dst2.delete();
        f.renameTo(dst2);
        long length1 = getFile(fileName).length();
        if(Configurations.isDeleteFinished()) {
            dst2.delete();
        }

        return length1;
    }
}
