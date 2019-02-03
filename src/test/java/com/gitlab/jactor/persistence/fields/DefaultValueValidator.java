package com.gitlab.jactor.persistence.fields;

import com.gitlab.jactor.persistence.builder.FieldValidation;
import com.gitlab.jactor.persistence.builder.MissingFields;
import com.gitlab.jactor.persistence.builder.ValidInstance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

class DefaultValueValidator {
    private final Collection<FieldValue> defaultValues;

    DefaultValueValidator(List<FieldValue> defaultValues) {
        this.defaultValues = defaultValues;
    }

    <T> Optional<MissingFields> provideDefaultValues(ValidInstance<T> validInstance, T bean) {
        Map<String, FieldValue> fieldValuesByFieldName = defaultValues.stream()
                .collect(Collectors.toMap(FieldValue::getName, fv -> fv));

        FieldValidation.performWithStandardValidation(validInstance, bean)
                .ifPresent(missingFields ->
                        missingFields.forEach(fieldName -> addDefaultValue(fieldValuesByFieldName.get(fieldName), bean, fieldName))
                );

        return Optional.empty();
    }

    private void addDefaultValue(FieldValue fieldValue, Object bean, String fieldName) {
        if (fieldValue == null) {
            throw new AssertionError(String.format("No FieldValue provided for %s.%s - default values: %s", bean.getClass().getSimpleName(), fieldName, defaultValues));
        }

        String setterName = fieldValue.fetchNameOfSetter();
        List<Method> methods = Arrays.asList(bean.getClass().getMethods());
        Object value = fieldValue.fetchValue();
        Optional<Method> possibleSetter = find(setterName, methods, value);

        if (possibleSetter.isPresent()) {
            Method setter = possibleSetter.get();

            invoke(setter, bean, value);
        } else {
            List<String> methodNames = methods.stream()
                    .map(Method::getName)
                    .collect(toList());

            throw new IllegalStateException(String.format("Unable to find '%s' for '%s' amongst metods: %s", setterName, value, methodNames));
        }
    }

    private Optional<Method> find(String setterName, List<Method> methods, Object value) {
        return methods.stream()
                .filter(method -> setterName.equals(method.getName()))
                .filter(method -> method.getParameterCount() == 1)
                .filter(method -> method.getParameterTypes()[0].equals(value.getClass()))
                .findFirst();
    }

    private void invoke(Method setter, Object bean, Object value) {
        try {
            setter.invoke(bean, value);
        } catch (RuntimeException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(String.format("Unable to invoke %s on %s with %s", setter, bean, value), e);
        }
    }
}
