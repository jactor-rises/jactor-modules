package nu.hjemme.persistence.facade;

import nu.hjemme.persistence.client.AddressEntity;
import nu.hjemme.persistence.client.BlogEntity;
import nu.hjemme.persistence.client.BlogEntryEntity;
import nu.hjemme.persistence.client.GuestBookEntity;
import nu.hjemme.persistence.client.GuestBookEntryEntity;
import nu.hjemme.persistence.client.PersistentEntry;
import nu.hjemme.persistence.client.PersonEntity;
import nu.hjemme.persistence.client.UserEntity;
import nu.hjemme.persistence.client.dao.UserDao;
import nu.hjemme.persistence.orm.dao.DefaultUserDao;
import nu.hjemme.persistence.orm.domain.DefaultAddressEntity;
import nu.hjemme.persistence.orm.domain.DefaultBlogEntity;
import nu.hjemme.persistence.orm.domain.DefaultBlogEntryEntity;
import nu.hjemme.persistence.orm.domain.DefaultGuestBookEntity;
import nu.hjemme.persistence.orm.domain.DefaultGuestBookEntryEntity;
import nu.hjemme.persistence.orm.domain.DefaultPersistentEntry;
import nu.hjemme.persistence.orm.domain.DefaultPersonEntity;
import nu.hjemme.persistence.orm.domain.DefaultUserEntity;

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
        supportedClasses.put(AddressEntity.class, DefaultAddressEntity.class);
        supportedClasses.put(BlogEntity.class, DefaultBlogEntity.class);
        supportedClasses.put(BlogEntryEntity.class, DefaultBlogEntryEntity.class);
        supportedClasses.put(GuestBookEntity.class, DefaultGuestBookEntity.class);
        supportedClasses.put(GuestBookEntryEntity.class, DefaultGuestBookEntryEntity.class);
        supportedClasses.put(PersistentEntry.class, DefaultPersistentEntry.class);
        supportedClasses.put(PersonEntity.class, DefaultPersonEntity.class);
        supportedClasses.put(UserEntity.class, DefaultUserEntity.class);
        supportedClasses.put(UserDao.class, DefaultUserDao.class);

        return supportedClasses;
    }
}
