package component.xyz.migoo.assertion.rule;

import core.xyz.migoo.assertion.Rule;

import java.util.List;

public class AnyBase {

    public boolean assertThat(Object actual, Object expected, Rule rule) {
        var objects = switch (expected) {
            case Object[] os -> List.of(os);
            case List<?> ls -> ls;
            case null -> List.of(""); // 这里要给个值，以便进入循环
            default -> List.of(expected);
        };
        for (Object object : objects) {
            if (rule.assertThat(actual, object)) {
                return true;
            }
        }
        return false;
    }
}
