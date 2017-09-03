package com.github.jactorrises.persistence.facade;

import com.github.jactorrises.persistence.entity.address.AddressEntityImpl;
import com.github.jactorrises.persistence.entity.blog.BlogEntityImpl;
import com.github.jactorrises.persistence.entity.blog.BlogEntryEntityImpl;
import com.github.jactorrises.persistence.entity.entry.PersistentEntryImpl;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntityImpl;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryEntityImpl;
import com.github.jactorrises.persistence.entity.person.PersonEntityImpl;
import com.github.jactorrises.persistence.entity.user.UserEntityImpl;
import com.github.jactorrises.persistence.client.AddressEntity;
import com.github.jactorrises.persistence.client.BlogEntity;
import com.github.jactorrises.persistence.client.BlogEntryEntity;
import com.github.jactorrises.persistence.client.GuestBookEntity;
import com.github.jactorrises.persistence.client.GuestBookEntryEntity;
import com.github.jactorrises.persistence.client.PersistentEntry;
import com.github.jactorrises.persistence.client.PersonEntity;
import com.github.jactorrises.persistence.client.UserEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;

public final class PersistentDataService {
    private static final Map<Class<?>, Class<?>> SUPPORTED_CLASSES = initMapOfSupportedClasses();
    private static final PersistentDataService INSTACHE = new PersistentDataService();

    private PersistentDataService() {
        // final singleton
    }

    public <T> T provideInstanceFor(Class<T> interfaceToInitiate, Object... arguments) {
        if (SUPPORTED_CLASSES.containsKey(interfaceToInitiate)) {
            if (arguments == null || arguments.length == 0) {
                return cast(initWithNoArgumentConstructor(interfaceToInitiate));
            }

            return cast(initWithArgumentConstructor(SUPPORTED_CLASSES.get(interfaceToInitiate), arguments));
        }

        throw newIllegalArgumentException(interfaceToInitiate, arguments);
    }

    @SuppressWarnings("unchecked") private <T> T cast(Object object) {
        return (T) object;
    }

    private Object initWithNoArgumentConstructor(Class<?> interfaceToInitiate) {
        try {
            @SuppressWarnings("unchecked") Constructor constructor = SUPPORTED_CLASSES.get(interfaceToInitiate).getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw newIllegalArgumentException(interfaceToInitiate, e);
        }
    }

    private Object initWithArgumentConstructor(Class<?> classToInitialize, Object[] arguments) {
        for (Constructor<?> constructor : classToInitialize.getConstructors()) {
            if (arguments.length == constructor.getParameterCount()) {
                return initWithArgumentConstructor(classToInitialize, arguments, constructor);
            }
        }

        throw newIllegalArgumentException(classToInitialize, arguments);
    }

    private Object initWithArgumentConstructor(Class<?> classToInitialize, Object[] arguments, Constructor<?> constructor) {
        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw newIllegalArgumentException(classToInitialize, e, arguments);
        }
    }

    private IllegalArgumentException newIllegalArgumentException(Class<?> theClass, Object... arguments) {
        return new IllegalArgumentException(
                "unable to create instance of " + theClass + (arguments != null && arguments.length > 0 ? " with arguments like " + asList(arguments) : "")
        );
    }

    private IllegalArgumentException newIllegalArgumentException(Class<?> theClass, Exception e, Object... arguments) {
        return new IllegalArgumentException(
                "unable to create instance of " + theClass + (arguments != null && arguments.length > 0 ? " with arguments like " + asList(arguments) : ""), e
        );
    }

    public static PersistentDataService getInstance() {
        return INSTACHE;
    }

    private static Map<Class<?>, Class<?>> initMapOfSupportedClasses() {
        HashMap<Class<?>, Class<?>> supportedClasses = new HashMap<>();
        supportedClasses.put(AddressEntity.class, AddressEntityImpl.class);
        supportedClasses.put(BlogEntity.class, BlogEntityImpl.class);
        supportedClasses.put(BlogEntryEntity.class, BlogEntryEntityImpl.class);
        supportedClasses.put(GuestBookEntity.class, GuestBookEntityImpl.class);
        supportedClasses.put(GuestBookEntryEntity.class, GuestBookEntryEntityImpl.class);
        supportedClasses.put(PersistentEntry.class, PersistentEntryImpl.class);
        supportedClasses.put(PersonEntity.class, PersonEntityImpl.class);
        supportedClasses.put(UserEntity.class, UserEntityImpl.class);

        return supportedClasses;
    }
}
