package spring.boot.pay.common;

import java.util.concurrent.ThreadLocalRandom;

public class RandomCodeHelper {

    /**
     * 随机生成指定长度字符串
     */
    public static String randomCharCode(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append((char) (ThreadLocalRandom.current().nextInt(33, 128)));
        }
        String code = builder.toString();
        return code;
    }

    /**
     * 随机生成一串指定长度的数字
     */
    public static String randomNumCode(int length) {
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append( ThreadLocalRandom.current().nextInt(0,10));
        }
        String code = builder.toString();
        return code;
    }
}
