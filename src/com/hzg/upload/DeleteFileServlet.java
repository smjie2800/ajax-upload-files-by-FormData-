package com.hzg.upload;

import cn.twinkling.stream.config.Configurations;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @from http://www.cnblogs.com/xdp-gacl/p/4200090.html
 * @author: ¹Â°Á²ÔÀÇ
 * @date: 2015-1-3 ÏÂÎç11:35:  *
 */
public class DeleteFileServlet extends HttpServlet {

    private static long fileSizeMax = 1024*1024;
    private static long sizeMax = 1024*1024*10;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = "fail";

        String baseDir = Configurations.getConfig("STREAM_FILE_REPOSITORY");
        String filePath =  request.getParameter("filePath");
        try {
            boolean result = new File(baseDir + "/" + filePath).delete();
            if (result) {
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