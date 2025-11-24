package com.xiaolin.context;

public class BaseContext {

    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(String username) {threadLocal.set(username);}

    public static String getCurrentUser() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
