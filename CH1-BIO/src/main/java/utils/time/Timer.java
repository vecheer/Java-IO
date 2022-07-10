package utils.time;

import java.util.concurrent.TimeUnit;

public class Timer {

    public static void sleep(int millis){
        if (millis == -1)
            millis = Integer.MAX_VALUE;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleepForever(){
        sleep(-1);
    }


    public static class SECOND{
        public static void sleep(int duration){
            try {
                TimeUnit.SECONDS.sleep(duration);
            } catch (InterruptedException e) {
                System.err.println("interrupted!");
            }
        }
    }

    public static class MILLISECONDS{
        public static void sleep(int duration){
            try {
                TimeUnit.MILLISECONDS.sleep(duration);
            } catch (InterruptedException e) {
                System.err.println("interrupted!");
            }
        }
    }

    public static class NANOSECONDS{
        public static void sleep(int duration){
            try {
                TimeUnit.NANOSECONDS.sleep(duration);
            } catch (InterruptedException e) {
                System.err.println("interrupted!");
            }
        }
    }

}
