package com.bjpowernode;

/**
 * 2020/4/25
 */
public class Father {


     //service方法出现this指向调用service方法实例对象
     public void service(String method){

         if("get".equals(method)){
             this.doGet();   // son.service()  this--->子类对象
         }else if("post".equals(method)){
             this.doPost();
         }
     }

     public void doGet(){
         System.out.println("Father doGet is run..");
     }

     public void doPost(){
         System.out.println("Father doPost is run...");
     }
}
