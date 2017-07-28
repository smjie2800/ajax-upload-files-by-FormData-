package com.hzg.upload;

import cn.twinkling.stream.config.Configurations;
import cn.twinkling.stream.util.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @from http://www.cnblogs.com/xdp-gacl/p/4200090.html
 * @author: ¹Â°Á²ÔÀÇ
 * @date: 2015-1-3 ÏÂÎç11:35:  *
 */
public class DeleteFileServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = "fail";

        String filePath =  request.getParameter("filePath");

        String authorizeResult = HttpRequest.sendPost(Configurations.getConfig("authorize_url"),
                "sessionId=" + request.getParameter("sessionId") + "&uri=" + request.getRequestURI());
        if (!authorizeResult.contains("success")) {

            message += ",has not privilege to delete file";
            response.setHeader("Access-Control-Allow-Origin", "*");
            writeStringToJson(response, "{\"result\":\"" + message + "\", \"filePath\":\"" + filePath + "\"}");

            return;
        }


        String baseDir = Configurations.getConfig("STREAM_FILE_REPOSITORY");
        String deleteDir = Configurations.getConfig("STREAM_FILE_DELETE_REPOSITORY");

        if (!filePath.contains(".")) {
            message += ",can not delete dir";
            response.setHeader("Access-Control-Allow-Origin", "*");
            writeStringToJson(response, "{\"result\":\"" + message + "\", \"filePath\":\"" + filePath + "\"}");

            return;
        }

        try {
            File file = new File(baseDir + "/" + filePath);
            Path path = Files.move(file.toPath(), file.toPath().resolveSibling(baseDir + "/" + deleteDir + "/" + filePath), new CopyOption[0]);
            if (path != null) {
                message = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message += "," + e.getMessage();
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        writeStringToJson(response, "{\"result\":\"" + message + "\", \"filePath\":\"" + filePath + "\"}");
    }

    public void writeStringToJson(HttpServletResponse response, String string) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(string);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}