package com.github.jactorrises.model.business.persistence.entity;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.datatype.Description;
import com.github.jactorrises.client.datatype.EmailAddress;
import com.github.jactorrises.client.datatype.Name;
import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.model.business.persistence.client.converter.CountryConverter;
import com.github.jactorrises.model.business.persistence.client.converter.DescriptionConverter;
import com.github.jactorrises.model.business.persistence.client.converter.EmailAddressConverter;
import com.github.jactorrises.model.business.persistence.client.converter.LocalDateConverter;
import com.github.jactorrises.model.business.persistence.client.converter.LocalDateTimeConverter;
import com.github.jactorrises.model.business.persistence.client.converter.NameConverter;
import com.github.jactorrises.model.business.persistence.client.converter.TypeConverter;
import com.github.jactorrises.model.business.persistence.client.converter.UserNameConverter;
import com.github.jactorrises.model.business.persistence.client.time.Now;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract class PersistentEntity implements Persistent<Long> {

    private static final Map<Class<?>, TypeConverter> dataTypeConverters = initKnownConverters();

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = PersistentMetadata.ID) Long id;

    @Column(name = PersistentMetadata.CREATION_TIME) @Type(type = "timestamp") private Date creationTime;
    @Column(name = PersistentMetadata.CREATED_BY) private String createdBy;
    @Column(name = PersistentMetadata.UPDATED_TIME) @Type(type = "timestamp") private Date updatedTime;
    @Column(name = PersistentMetadata.UPDATED_BY) private String updatedBy;

    protected PersistentEntity() {
        createdBy = "todo";
        creationTime = Now.asDate();
        updatedBy = "todo";
        updatedTime = Now.asDate();
    }

    @SuppressWarnings("unchecked") protected <T, F> T convertTo(F from, Class<T> classType) {
        if (canConvert(classType)) {
            return cast(dataTypeConverters.get(classType).convertTo(from));
        }

        throw new IllegalArgumentException(classType + " is not a type known for any converter!");
    }

    @SuppressWarnings("unchecked") protected <T, F> F convertFrom(T to, Class<T> classType) {
        if (canConvert(classType)) {
            return cast(dataTypeConverters.get(classType).convertFrom(to));
        }

        throw new IllegalArgumentException(classType + " is not a type known for any converter!");
    }

    private boolean canConvert(Class<?> classType) {
        return !(classType != null && !dataTypeConverters.containsKey(classType));
    }

    @SuppressWarnings("unchecked") private <T> T cast(Object object) {
        return (T) object;
    }

    @Override public String toString() {
        return getClass().getSimpleName() + (id != null ? "#" + id : "");
    }

    @Override public Name getCreatedBy() {
        return convertTo(createdBy, Name.class);
    }

    @Override public LocalDateTime getCreationTime() {
        return convertTo(creationTime, LocalDateTime.class);
    }

    @Override public Long getId() {
        return id;
    }

    @Override public Name getUpdatedBy() {
        return convertTo(updatedBy, Name.class);
    }

    @Override public LocalDateTime getUpdatedTime() {
        return convertTo(updatedTime, LocalDateTime.class);
    }

    private static Map<Class<?>, TypeConverter> initKnownConverters() {
        Map<Class<?>, TypeConverter> knownConverters = new HashMap<>();
        knownConverters.put(Name.class, new NameConverter());
        knownConverters.put(EmailAddress.class, new EmailAddressConverter());
        knownConverters.put(UserName.class, new UserNameConverter());
        knownConverters.put(Description.class, new DescriptionConverter());
        knownConverters.put(LocalDateTime.class, new LocalDateTimeConverter());
        knownConverters.put(Country.class, new CountryConverter());
        knownConverters.put(LocalDate.class, new LocalDateConverter());

        return knownConverters;
    }
}
