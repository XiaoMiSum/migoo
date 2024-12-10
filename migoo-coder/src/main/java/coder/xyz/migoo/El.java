package coder.xyz.migoo;

import java.util.*;

public abstract class El {

    private final Map<String, Object> properties = HashMap.newHashMap(16);

    protected El() {
    }

    protected El(String testClass) {
        if (Objects.nonNull(testClass) && !testClass.trim().isEmpty()) {
            p("testclass", testClass);
        }
    }

    public Map<String, Object> properties() {
        return properties;
    }

    protected void p(String key, Object value) {
        if (Objects.nonNull(value)) {
            if (value instanceof Map<?, ?> map) {
                map.remove("testclass");
            }
            properties.put(key, value);
        }
    }

    protected void p(Map<String, Object> properties) {
        if (Objects.nonNull(properties)) {
            properties.remove("testclass");
            this.properties.putAll(properties);
        }
    }

    protected void p(String key, Object[] objects) {
        if (Objects.nonNull(objects) && objects.length > 0) {
            List<Map<String, Object>> value = new ArrayList<>();
            for (Object object : objects) {
                if (object instanceof El el) {
                    value.add(el.properties());
                }
            }
            properties.put(key, value);
        }
    }
}
