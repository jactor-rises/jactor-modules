package com.gitlab.jactor.rises.persistence.entity.guestbook;

import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntity;
import com.gitlab.jactor.rises.persistence.entity.user.UserEntityBuilder;

import java.util.Optional;

public class GuestBookEntityBuilder extends AbstractBuilder<GuestBookEntity> {

    private String title;
    private UserEntity userEntity;

    GuestBookEntityBuilder() {
        super(GuestBookEntityBuilder::validate);
    }

    public GuestBookEntityBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public GuestBookEntityBuilder with(UserEntityBuilder userEntityBuilder) {
        return with(userEntityBuilder.build());
    }

    @Override protected GuestBookEntity buildBean() {
        GuestBookEntity guestBookEntity = new GuestBookEntity();
        guestBookEntity.setTitle(title);
        guestBookEntity.setUser(userEntity);

        return guestBookEntity;
    }

    private static Optional<MissingFields> validate(GuestBookEntity guestBookEntity, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenNoValue(GuestBookEntity.class.getSimpleName(), "title", guestBookEntity.getTitle());
        missingFields.addInvalidFieldWhenNoValue(GuestBookEntity.class.getSimpleName(), "user", guestBookEntity.getUser());

        return missingFields.presentWhenFieldsAreMissing();
    }
}
