package component.xyz.migoo.assertion.rule;

import core.xyz.migoo.assertion.Rule;
import core.xyz.migoo.testelement.Alias;

@Alias({"containAny", "ct_any", "cta"})
public class ContainsAny extends AnyBase implements Rule {

    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new Contains());
    }

}
