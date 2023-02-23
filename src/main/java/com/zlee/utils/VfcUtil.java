package com.zlee.utils;

import java.util.Random;

/**
 * @author z-Lee
 * @date 2023-02-15-16:31
 */
public class VfcUtil {

    /**
     * @param num 几位数的验证码
     * */
    public static String generateVfc(int num){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
