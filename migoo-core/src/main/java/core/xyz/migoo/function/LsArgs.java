package core.xyz.migoo.function;

import com.alibaba.fastjson2.JSONArray;
import core.xyz.migoo.context.ContextWrapper;

public class LsArgs extends JSONArray implements Args {


    private final ContextWrapper context;

    public LsArgs(ContextWrapper context) {
        this.context = context;
    }

    @Override
    public boolean add(Object parameter) {
        return super.add(parameter instanceof String string ? getParameterValue(string.trim()) : parameter);
    }

    @Override
    public ContextWrapper getContext() {
        return context;
    }
}
