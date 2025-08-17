package io.github.xiaomisum.ryze.component.assertion.rule;

import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.testelement.KW;

@KW({"containAny", "ct_any", "cta"})
public class ContainsAny extends AnyBase implements Rule {

    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new Contains());
    }

}
