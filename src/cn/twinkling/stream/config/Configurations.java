package cn.twinkling.stream.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @from http://www.twinkling.cn/
 */
public class Configurations {
    static final String CONFIG_FILE = "stream-config.properties";
    private static Properties properties = null;
    private static final String REPOSITORY;

    private Configurations() {
        this.init();
        System.out.println("[NOTICE] File Repository Path ¡Ý¡Ý¡Ý " + getFileRepository());
    }

    void init() {
        try {
            ClassLoader e = cn.twinkling.stream.config.Configurations.class.getClassLoader();
            InputStream in = e.getResourceAsStream("stream-config.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException var3) {
            System.err.println("reading `stream-config.properties` error!" + var3);
        }

    }

    public static String getConfig(String key) {
        return getConfig(key, (String)null);
    }

    public static String getConfig(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getConfig(String key, int defaultValue) {
        String val = getConfig(key);
        boolean setting = false;

        int setting1;
        try {
            setting1 = Integer.parseInt(val);
        } catch (NumberFormatException var5) {
            setting1 = defaultValue;
        }

        return setting1;
    }

    public static String getFileRepository() {
        String val = getConfig("STREAM_FILE_REPOSITORY");
        if (File.separator.equals("\\")) {   //  windows systems
            val = getConfig("WINDOWS_STREAM_FILE_REPOSITORY");
        }

        return val;
    }

    public static String getTempRepository() {
        String val = getConfig("temp_dir");
        if (File.separator.equals("\\")) {   //  windows systems
            val = getConfig("windows_temp_dir");
        }

        return val;
    }




    public static String getCrossServer() {
        return getConfig("STREAM_CROSS_SERVER");
    }

    public static String getCrossOrigins() {
        return getConfig("STREAM_CROSS_ORIGIN");
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getConfig(key));
    }

    public static boolean isDeleteFinished() {
        return getBoolean("STREAM_DELETE_FINISH");
    }

    public static boolean isCrossed() {
        return getBoolean("STREAM_IS_CROSS");
    }

    static {
        REPOSITORY = System.getProperty("java.io.tmpdir", File.separator + "tmp" + File.separator + "upload-repository");
        new Configurations();
    }
}
