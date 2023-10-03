package com.spongehah.juc;

import java.util.concurrent.CompletableFuture;

public class Main {
    private static void m1(){
        Object o = new Object();
        synchronized (o){
            System.out.println("come in synchronized + \t" + o.hashCode());
        }
    }
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            new Thread(Main::m1,String.valueOf(i)).start();
        }
    }

    private static void test() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("whenComplete and exceptionally");
            return "whenComplete and exceptionally";
        }).whenComplete((v,e) -> {
            if (e ==null){
                System.out.println("计算结果：" + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("异常：" + e.getCause() + "\t" + e.getMessage());
            return null;
        });
    }

}