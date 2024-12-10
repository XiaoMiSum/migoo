package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

import java.util.Map;

public class Dubbo extends El {

    public Dubbo registry(Map<String, Object> registry) {
        p("registry", registry);
        return this;
    }

    public Dubbo reference(Map<String, Object> reference) {
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

    Map<String, Object> registry() {
        return (Map<String, Object>) properties().get("registry");
    }

    Map<String, Object> reference() {
        return (Map<String, Object>) properties().get("reference");
    }
}
