package com.hzg.upload;

import cn.twinkling.stream.config.Configurations;
import cn.twinkling.stream.util.HttpRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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


        String baseDir = Configurations.getFileRepository();
        String deleteDir = Configurations.getConfig("STREAM_FILE_DELETE_REPOSITORY");

        if (!filePath.contains(".")) {
            message += ",can not delete dir";
            response.setHeader("Access-Control-Allow-Origin", "*");
            writeStringToJson(response, "{\"result\":\"" + message + "\", \"filePath\":\"" + filePath + "\"}");

            return;
        }

        try {
            String osFilePath = filePath.replace("/", File.separator);

            String fileName = osFilePath.substring(osFilePath.lastIndexOf(File.separator)+1);
            String targetParentDir = baseDir + File.separator + deleteDir + File.separator + osFilePath.substring(0, osFilePath.lastIndexOf(File.separator));

            if (!Files.exists(Paths.get(targetParentDir))) {
                Files.createDirectories(Paths.get(targetParentDir));
            }

            System.out.println("--------------"+ baseDir + File.separator + osFilePath);
            System.out.println("========="+ targetParentDir + File.separator + fileName);
            Path path = Files.move(Paths.get(baseDir + File.separator + osFilePath),
                    Paths.get(targetParentDir + File.separator + fileName), REPLACE_EXISTING, ATOMIC_MOVE);

            if (path != null) {
                message = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "fail, system error" ;
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