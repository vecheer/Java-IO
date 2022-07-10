package utils.concurrent.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class YqAtomicInteger {

    private static Unsafe unsafe;
    private static long intValueOffset; // the Value field of MyAtomicInteger class mode


    // reflect to get the Unsafe instance
    static {
        Field field;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);

            unsafe = (Unsafe) field.get(null);
            intValueOffset = unsafe.objectFieldOffset(YqAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException | IllegalAccessException  ignored) {
        }
    }

    private volatile int value;

    public YqAtomicInteger(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public int getAndIncrement(){
        int valueCached = value;
        int lastValue;
        while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue+1)){}
        return valueCached;
    }

    public int incrementAndGet (){
        int lastValue;
        while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue+1)){}
        return lastValue+1;
    }

    public int getAndDecrement(){
        int valueCached = value;
        int lastValue;
        while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue-1)){}
        return valueCached;
    }

    public int decrementAndGet(){
        int lastValue;
        while (!unsafe.compareAndSwapInt(this,intValueOffset,(lastValue=value),lastValue-1)){}
        return lastValue-1;
    }


    @Override
    public String toString() {
        return "MyAtomicInteger{" +
                "value=" + value +
                '}';
    }
}