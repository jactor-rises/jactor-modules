package com.github.jactorrises.model.internal.domain.guestbook;

import com.github.jactorrises.model.internal.domain.DomainBuilder;
import com.github.jactorrises.model.internal.domain.user.UserDomain;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.internal.persistence.entity.user.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class GuestBookDomainBuilder extends DomainBuilder<GuestBookDomain> {
    static final String THE_GUEST_BOOK_MUST_BELONG_TO_A_USER = "The guest book must belong to a user";
    static final String THE_TITLE_CANNOT_BE_BLANK = "The title cannot be blank";

    private final GuestBookEntity guestBookEntity = new GuestBookEntity();

    GuestBookDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getTitle()) ? Optional.empty() : Optional.of(THE_TITLE_CANNOT_BE_BLANK),
                domain -> domain.getUser() != null ? Optional.empty() : Optional.of(THE_GUEST_BOOK_MUST_BELONG_TO_A_USER)
        ));
    }

    public GuestBookDomainBuilder withTitle(String title) {
        guestBookEntity.setTitle(title);
        return this;
    }

    public GuestBookDomainBuilder with(UserEntity userEntity) {
        guestBookEntity.setUser(userEntity);
        return this;
    }

    public GuestBookDomainBuilder with(UserDomain userDomain) {
        with(userDomain);
        return this;
    }

    @Override protected GuestBookDomain buildBeforeValidation() {
        return new GuestBookDomain(guestBookEntity);
    }

    public static GuestBookDomain build(GuestBookEntity guestBook) {
        return new GuestBookDomain(guestBook);
    }
}
