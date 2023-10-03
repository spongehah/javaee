package com.spongehah.juc.CompletableFuture;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureAPI3Demo {

    public static void main(String[] args) {
        /**
         * 演示几种结果消费方法
         */
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenRun(() -> {}).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenAccept(System.out::println).join());
        System.out.println(CompletableFuture.supplyAsync(() -> "resultA").thenApply(f -> f + "resultB").join());
    }
}
