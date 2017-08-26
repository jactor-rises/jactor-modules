package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.builder.FieldValidator.ValidateField;
import nu.hjemme.persistence.facade.PersistentDataService;

import java.util.List;

/**
 * A builder which does not return a bean instance before all fields are validated using
 * {@link ValidateField}
 *
 * @param <T> type of domain to build
 */
public abstract class DomainBuilder<T> {
    private static FieldValidator fieldValidator;
    private final List<ValidateField<T>> validateFields;

    protected DomainBuilder(List<ValidateField<T>> validateFields) {
        this.validateFields = validateFields;
    }

    protected abstract T initWithRequiredFields();

    public T build() {
        T bean = initWithRequiredFields();
        fieldValidator.validate(bean, validateFields);

        return bean;
    }

    protected static void useFieldValidator(FieldValidator fieldValidator) {
        DomainBuilder.fieldValidator = fieldValidator;
    }

    static <T> T newInstanceOf(Class<T> persistentClass) {
        return PersistentDataService.getInstance().provideInstanceFor(persistentClass);
    }

    static {
        useFieldValidator(new FieldValidator());
    }
}
