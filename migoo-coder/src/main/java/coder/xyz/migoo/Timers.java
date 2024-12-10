package coder.xyz.migoo;

public class Timers extends El {

    private Timers(String testClass, int timeout) {
        super(testClass);
        p("timeout", Math.max(timeout, 1));
    }

    public static Timers syncTimer(int seconds) {
        return new Timers("sync_timer", seconds);
    }
}
