package component.xyz.migoo.assertion.rule;

import core.xyz.migoo.assertion.Rule;
import core.xyz.migoo.testelement.Alias;

@Alias({"any", "eqa", "equalAny", "eq_any"})
public class EqualsAny extends AnyBase implements Rule {

    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new Equals());
    }
}
