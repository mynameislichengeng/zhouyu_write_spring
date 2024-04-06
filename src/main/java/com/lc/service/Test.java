package com.lc.service;

import com.lc.spring.ZhouyuApplicationContext;

/**
 * @author by licheng01
 * @date 2024/2/26 9:05
 * @description
 */
public class Test {

    public static void main(String[] args) {
        ZhouyuApplicationContext context = new ZhouyuApplicationContext(Config.class);


        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
        System.out.println(context.getBean("userService"));
    }
}
