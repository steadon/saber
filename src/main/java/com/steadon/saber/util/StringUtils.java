package com.steadon.saber.util;

public class StringUtils {

    public static final String EMPTY = "";

    private static boolean empty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isEmpty(String str) {
        return empty(str);
    }

    public static boolean isNotEmpty(String str) {
        return !empty(str);
    }

    /**
     * 暗提示处理
     *
     * @apiNote 保留原始长度
     */
    public static String coverSecret(String str) {
        int len = str.length();
        return str.substring(0, 3) + "x".repeat(len - 6) + str.substring(len - 3);
    }
}
