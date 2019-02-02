package com.gitlab.jactor.rises.test.extension.validate.fields;

import com.gitlab.jactor.rises.commons.builder.FieldValidation;
import com.gitlab.jactor.rises.commons.builder.MissingFields;
import com.gitlab.jactor.rises.commons.builder.ValidInstance;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequiredFieldsExtension implements BeforeEachCallback, AfterEachCallback {

    private final Map<Class<?>, List<FieldValue>> fieldValuesPerClass;

    public RequiredFieldsExtension(Map<Class<?>, List<FieldValue>> fieldValuesPerClass) {
        if (!fieldValuesPerClass.isEmpty()) {
            this.fieldValuesPerClass = fieldValuesPerClass;
        } else {
            throw new AssertionError("No fieldvalues are given to the extension!");
        }

    }

    @Override public void afterEach(ExtensionContext extensionContext) {
        FieldValidation.useStandardValidation();
    }

    @Override public void beforeEach(ExtensionContext extensionContext) {
        FieldValidation.applyFieldValidator(this::applyDefaultValues);
    }

    private <T> Optional<MissingFields> applyDefaultValues(ValidInstance<T> validInstance, T bean) {
        if (fieldValuesPerClass.containsKey(bean.getClass())) {
            DefaultValueValidator defaultValueValidator = new DefaultValueValidator(fieldValuesPerClass.get(bean.getClass()));
            defaultValueValidator.provideDefaultValues(validInstance, bean);
        } else {
            return FieldValidation.performWithStandardValidation(validInstance, bean);
        }

        return Optional.empty();
    }
}
