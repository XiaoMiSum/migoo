package coder.xyz.migoo

class Timer extends El {

    Timer(String testClass, int timeout) {
        super(testClass)
        assert timeout > 0
        p("timeout", timeout)
    }

    static Timer withSyncTime(int seconds = 1) {
        return new Timer("SyncTimer", seconds)
    }
}
