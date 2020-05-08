package com.stylefeng.guns.rest.common;

public class CurrentUser {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void saveUserId(String userId){
        threadLocal.set(userId);
    }

    public static String getCurrentUserId(){
        return threadLocal.get();
    }
}
