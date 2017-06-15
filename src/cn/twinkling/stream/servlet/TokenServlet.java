//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.twinkling.stream.servlet;

import cn.twinkling.stream.config.Configurations;
import cn.twinkling.stream.util.TokenUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class TokenServlet extends HttpServlet {
    private static final long serialVersionUID = 2650340991003623753L;
    static final String FILE_NAME_FIELD = "name";
    static final String FILE_SIZE_FIELD = "size";
    static final String TOKEN_FIELD = "token";
    static final String SERVER_FIELD = "server";
    static final String SUCCESS = "success";
    static final String MESSAGE = "message";

    public TokenServlet() {
    }

    public void init() throws ServletException {
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String size = req.getParameter("size");
        String token = TokenUtil.generateToken(name, size);
        PrintWriter writer = resp.getWriter();
        JSONObject json = new JSONObject();

        try {
            json.put("token", token);
            if(Configurations.isCrossed()) {
                json.put("server", Configurations.getCrossServer());
            }

            json.put("success", true);
            json.put("message", "");
        } catch (JSONException var9) {
            ;
        }

        writer.write(json.toString());
    }

    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    public void destroy() {
        super.destroy();
    }
}
