package component.xyz.migoo.assertion.rule;

import core.xyz.migoo.assertion.Rule;
import core.xyz.migoo.testelement.Alias;

@Alias({"equalsAnyIgnoreCase", "AnyIgnoreCase"})
public class EqualsAnyIgnoreCase extends AnyBase implements Rule {

    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new EqualsIgnoreCase());
    }
}
