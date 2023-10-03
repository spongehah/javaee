package com.bjpoewrnode.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 2020/4/29
 */
public class OneListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("恭喜恭喜，来世走一朝");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("兄弟不要怕，二十年之后你还是一条好汉");
    }
}
