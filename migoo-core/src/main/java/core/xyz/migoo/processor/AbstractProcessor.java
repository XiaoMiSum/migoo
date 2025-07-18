package core.xyz.migoo.processor;

import core.xyz.migoo.testelement.TestElementConfigure;

public abstract class AbstractProcessor implements Processor {

    protected TestElementConfigure config;

    protected boolean disabled;

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
