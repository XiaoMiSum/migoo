package coder.xyz.migoo

class Timers extends El {

    private Timers(String testClass, int timeout) {
        super(testClass)
        assert timeout > 0
        p("timeout", timeout)
    }

    static Timers syncTimer(int seconds = 1) {
        return new Timers("SyncTimer", seconds)
    }
}
