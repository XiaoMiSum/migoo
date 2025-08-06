package io.github.xiaomisum.ryze.component.assertion.rule;

import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.testelement.KW;

@KW({"equalsAnyIgnoreCase", "AnyIgnoreCase", "anyic"})
public class EqualsAnyIgnoreCase extends AnyBase implements Rule {

    @Override
    public boolean assertThat(Object actual, Object expected) {
        return super.assertThat(actual, expected, new EqualsIgnoreCase());
    }
}
