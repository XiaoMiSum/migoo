package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

import java.util.HashMap;
import java.util.Map;

public class Dubbo extends El {

    public Dubbo registry() {
        var registry = new Registry(this);
        p("registry", registry);
        return this;
    }

    public Dubbo reference() {
        var reference = new Reference(this);
        p("reference", reference);
        return this;
    }

    public Dubbo interfaceClass(String interfaceClass) {
        p("interfaceClass", interfaceClass);
        return this;
    }

    public Dubbo method(String method) {
        p("method", method);
        return this;
    }

    public Dubbo parameterTypes(String... parameterTypes) {
        p("parameter_types", parameterTypes);
        return this;
    }

    public Dubbo parameters(Object... parameters) {
        p("parameters", parameters);
        return this;
    }

    public Dubbo attachmentArgs(Map<String, Object> attachmentArgs) {
        p("attachment_args", attachmentArgs);
        return this;
    }

    public static class Registry extends HashMap<String, Object> {

        private final Dubbo dubbo;

        private Registry(Dubbo dubbo) {
            this.dubbo = dubbo;
        }

        public Registry protocol(String protocol) {
            put("protocol", protocol);
            return this;
        }

        public Registry address(String address) {
            put("address", address);
            return this;
        }

        public Registry username(String username) {
            put("username", username);
            return this;
        }

        public Registry password(String password) {
            put("password", password);
            return this;
        }

        public Registry version(String version) {
            put("version", version);
            return this;
        }

        public Dubbo and() {
            return dubbo;
        }

    }

    public static class Reference extends HashMap<String, Object> {

        private final Dubbo dubbo;

        private Reference(Dubbo dubbo) {
            this.dubbo = dubbo;
        }

        public Reference version(String version) {
            put("version", version);
            return this;
        }

        public Reference retries(int retries) {
            put("retries", retries);
            return this;
        }

        public Reference timeout(long timeout) {
            put("timeout", timeout);
            return this;
        }

        public Reference group(String group) {
            put("group", group);
            return this;
        }

        public Reference async(boolean async) {
            put("async", async);
            return this;
        }

        public Reference loadBalance(String loadBalance) {
            put("load_balance", loadBalance);
            return this;
        }

        public Dubbo and() {
            return dubbo;
        }

    }
}
