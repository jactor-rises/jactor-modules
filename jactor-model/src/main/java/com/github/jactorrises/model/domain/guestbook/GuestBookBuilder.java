package com.github.jactorrises.model.domain.guestbook;

import com.github.jactorrises.model.Builder;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntityBuilder;
import com.github.jactorrises.model.persistence.entity.user.UserEntity;
import com.github.jactorrises.model.persistence.entity.user.UserEntityBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.github.jactorrises.model.persistence.entity.guestbook.GuestBookEntity.aGuestBook;
import static java.util.Arrays.asList;

public final class GuestBookBuilder extends Builder<GuestBookDomain> {
    static final String THE_GUEST_BOOK_MUST_BELONG_TO_A_USER = "The guest book must belong to a user";
    static final String THE_TITLE_CANNOT_BE_BLANK = "The title cannot be blank";

    private final GuestBookEntityBuilder guestBookEntity = aGuestBook();

    GuestBookBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getTitle()) ? Optional.empty() : Optional.of(THE_TITLE_CANNOT_BE_BLANK),
                domain -> domain.getUser() != null ? Optional.empty() : Optional.of(THE_GUEST_BOOK_MUST_BELONG_TO_A_USER)
        ));
    }

    public GuestBookBuilder withTitle(String title) {
        guestBookEntity.withTitle(title);
        return this;
    }

    public GuestBookBuilder with(UserEntity userEntity) {
        guestBookEntity.with(userEntity);
        return this;
    }

    public GuestBookBuilder with(UserEntityBuilder userEntityBuilder) {
        return with(userEntityBuilder.build());
    }

    public GuestBookBuilder with(UserDomain userDomain) {
        with(userDomain.getEntity());
        return this;
    }

    @Override protected GuestBookDomain buildBean() {
        return new GuestBookDomain(guestBookEntity.build());
    }

    public static GuestBookDomain build(GuestBookEntity guestBook) {
        return new GuestBookDomain(guestBook);
    }
}
