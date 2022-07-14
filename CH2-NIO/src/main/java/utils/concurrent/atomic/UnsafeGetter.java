package utils.concurrent.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeGetter {
    private static Unsafe unsafe;

    static {
        Field field;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);

            unsafe = (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException  ignored) {
            throw new RuntimeException("反射获取unsafe对象失败!");
        }
    }

    public static Unsafe getUnsafe(){
        return unsafe;
    }

    public static long getFieldOffset(String fieldName){
        try {
            return unsafe.objectFieldOffset(YqAtomicInt.class.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("输入了错误的字段名!");
        }
    }
}
