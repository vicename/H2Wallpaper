package com.dc.hwallpaper.utils;

import android.os.Looper;

/**
 * Created by Li DaChang on 16/12/25.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class CommonUtils {
    // 递归法求最大公约数
    public static int maxCommonDivisor(int m, int n) {
        if (m < n) {// 保证m>n,若m<n,则进行数据交换
            int temp = m;
            m = n;
            n = temp;
        }
        if (m % n == 0) {// 若余数为0,返回最大公约数
            return n;
        } else { // 否则,进行递归,把n赋给m,把余数赋给n
            return maxCommonDivisor(n, m % n);
        }
    }

    /**
     * 判断是否为主线程
     *
     * @return 是否
     */
    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
