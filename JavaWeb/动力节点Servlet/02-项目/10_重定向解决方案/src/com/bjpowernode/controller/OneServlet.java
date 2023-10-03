package com.bjpowernode.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OneServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("OneServlet 负责 洗韭菜");

        //重定向解决方案:
        //response.sendRedirect("/myWeb/two");// [地址格式: /网站名/资源文件名]
          response.sendRedirect("http://www.baidu.com");
    }
}
