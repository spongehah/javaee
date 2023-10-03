package com.bjpowernode.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OneServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.通过请求对象，读取【请求行】中【url】信息
         String url = request.getRequestURL().toString();
        //2.通过请求对象，读取【请求行】中【method】信息
         String method = request.getMethod();

        //3.通过请求对象，读取【请求行】中uri信息
        /*
        * URI：资源文件精准定位地址，在请求行并没有URI这个属性。
        *      实际上URL中截取一个字符串，这个字符串格式"/网站名/资源文件名"
        *      URI用于让Http服务器对被访问的资源文件进行定位
        */
        String uri =  request.getRequestURI();// substring

        System.out.println("URL "+url);
        System.out.println("method "+method);
        System.out.println("URI "+uri);

    }
}
