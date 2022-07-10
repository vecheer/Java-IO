package utils.data;

import utils.concurrent.atomic.YqAtomicInteger;

import java.util.Random;
import java.util.UUID;

public class Generator {


    // 获取UUID
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    // 生成随机名称: baseName-atomicInt
    private static YqAtomicInteger count = new YqAtomicInteger(0);
    public static String getRandomName(String baseName){
        return baseName + "-" + count.getAndDecrement();
    }

    // 获取随机数(0,upper]
    // 并发场景下调用，会生成大量的Random()对象，需要gc优化
    public static int getRandomInt(int upper){
        return new Random().nextInt(upper) + 1;
    }
}
