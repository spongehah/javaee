package com.bjpowernode.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OneServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

         Map map = new HashMap();
         //int num =(int)map.get("key1");//抛出NullPointerException
         Integer num =(Integer)map.get("key1");
         System.out.println("heheh.......");
        //int a = null; // null值不可能赋值给int
         Integer b = null; //所有高级引用类型都可以赋值给null
    }
}
