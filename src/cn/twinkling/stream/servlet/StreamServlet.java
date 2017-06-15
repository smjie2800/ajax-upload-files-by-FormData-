//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.twinkling.stream.servlet;

import cn.twinkling.stream.config.Configurations;
import cn.twinkling.stream.servlet.Range;
import cn.twinkling.stream.servlet.StreamException;
import cn.twinkling.stream.util.IoUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @from http://www.twinkling.cn/
 */
public class StreamServlet extends HttpServlet {
    private static final long serialVersionUID = -8619685235661387895L;
    static final int BUFFER_LENGTH = 10240;
    static final String START_FIELD = "start";
    public static final String CONTENT_RANGE_HEADER = "content-range";

    public StreamServlet() {
    }

    public void init() throws ServletException {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doOptions(req, resp);
        String token = req.getParameter("token");
        String size = req.getParameter("size");
        String fileName = req.getParameter("name");
        PrintWriter writer = resp.getWriter();
        JSONObject json = new JSONObject();
        long start = 0L;
        boolean success = true;
        String message = "";

        try {
            File fne = IoUtil.getTokenedFile(token);
            start = fne.length();
            if(token.endsWith("_0") && "0".equals(size) && 0L == start) {
                fne.renameTo(IoUtil.getFile(fileName));
            }
        } catch (FileNotFoundException var21) {
            message = "Error: " + var21.getMessage();
            success = false;
        } finally {
            try {
                if(success) {
                    json.put("start", start);
                }

                json.put("success", success);
                json.put("message", message);
            } catch (JSONException var20) {
                ;
            }

            writer.write(json.toString());
            IoUtil.close(writer);
        }

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doOptions(req, resp);
        String token = req.getParameter("token");
        String fileName = req.getParameter("name");
        Range range = IoUtil.parseRange(req);
        FileOutputStream out = null;
        ServletInputStream content = null;
        PrintWriter writer = resp.getWriter();
        JSONObject json = new JSONObject();
        long start = 0L;
        boolean success = true;
        String message = "";
        File f = IoUtil.getTokenedFile(token);

        try {
            if(f.length() != range.getFrom()) {
                throw new StreamException(StreamException.ERROR_FILE_RANGE_START);
            }

            out = new FileOutputStream(f, true);
            content = req.getInputStream();
            boolean e = false;
            byte[] bytes = new byte[10240];

            int e1;
            while((e1 = content.read(bytes)) != -1) {
                out.write(bytes, 0, e1);
            }

            start = f.length();
        } catch (StreamException var35) {
            success = StreamException.ERROR_FILE_RANGE_START == var35.getCode();
            message = "Code: " + var35.getCode();
        } catch (FileNotFoundException var36) {
            message = "Code: " + StreamException.ERROR_FILE_NOT_EXIST;
            success = false;
        } catch (IOException var37) {
            message = "IO Error: " + var37.getMessage();
            success = false;
        } finally {
            IoUtil.close(out);
            IoUtil.close(content);
            if(range.getSize() == start) {
                try {
                    IoUtil.getFile(fileName).delete();
                    Files.move(f.toPath(), f.toPath().resolveSibling(fileName), new CopyOption[0]);
                    System.out.println("TK: `" + token + "`, NE: `" + fileName + "`");
                    if(Configurations.isDeleteFinished()) {
                        IoUtil.getFile(fileName).delete();
                    }
                } catch (IOException var34) {
                    success = false;
                    message = "Rename file error: " + var34.getMessage();
                }
            }

            try {
                if(success) {
                    json.put("start", start);
                }

                json.put("success", success);
                json.put("message", message);
            } catch (JSONException var33) {
                ;
            }

            writer.write(json.toString());
            IoUtil.close(writer);
        }

    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Range,Content-Type");
        resp.setHeader("Access-Control-Allow-Origin", Configurations.getCrossOrigins());
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    }

    public void destroy() {
        super.destroy();
    }
}
