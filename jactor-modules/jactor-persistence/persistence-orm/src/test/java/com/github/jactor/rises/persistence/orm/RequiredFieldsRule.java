package com.github.jactor.rises.persistence.orm;

import com.github.jactor.rises.test.extension.validate.fields.AbstractRequiredFieldsExtension;
import com.github.jactor.rises.test.rule.AbstractRequiredFieldsRule;

public class RequiredFieldsRule extends AbstractRequiredFieldsRule {
    @Override protected AbstractRequiredFieldsExtension getExtension() {
        return new RequiredFieldsExtension();
    }

    public static RequiredFieldsRule provideValues() {
        return new RequiredFieldsRule();
    }
}
