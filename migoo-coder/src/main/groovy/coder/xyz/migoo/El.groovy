package coder.xyz.migoo

import com.google.common.base.Strings

abstract class El {

    private final def properties = [:]

    protected El(String testClass = "") {
        if (!Strings.isNullOrEmpty(testClass)) {
            properties["testclass"] = testClass
        }
    }

    protected El(Map<String, Object> properties) {
        p(properties)
    }

    Map<String, Object> customize() {
        return properties as Map<String, Object>
    }

    protected void p(String key, Object value) {
        if (Objects.nonNull(value)) {
            properties[key] = value
        }
    }

    protected void p(Map<String, Object> properties) {
        properties.remove("testclass")
        this.properties.putAll(properties)
    }

    protected void p(String key, Object[] objects) {
        if (Objects.nonNull(objects) && objects.length > 0) {
            List<Map<String, Object>> value = []
            for (Object object : objects) {
                if (object instanceof El) {
                    value.add(((El) object).customize())
                }
            }
            properties[key] = value
        }
    }
}
