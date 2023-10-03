package com.spongehah.juc.CompletableFuture;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureMallDemo {
    public static void main(String[] args) {
        //链式调用
        student student = new student();
        
        student.setId(1).setStudentName("zs").setMajor("it");

        System.out.println(student);
        
        //join()方法与get()方法
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return "hello 1234";
        });
//        System.out.println(completableFuture.get());      //需要抛出异常
        System.out.println(completableFuture.join());       //不用抛出异常，运行时出现异常就出异常就好
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
class student{
    private Integer id;
    private String studentName;
    private String major;
}
