package com.github.jactorrises.model.business.domain.builder;

import com.github.jactorrises.model.business.domain.GuestBookDomain;
import com.github.jactorrises.model.business.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.business.persistence.entity.user.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class GuestBookDomainBuilder extends DomainBuilder<GuestBookDomain> {
    static final String THE_GUEST_BOOK_MUST_BELONG_TO_A_USER = "The guest book must belong to a user";
    static final String THE_TITLE_CANNOT_BE_BLANK = "The title cannot be blank";

    private final GuestBookEntity guestBookEntity = new GuestBookEntity();

    private GuestBookDomainBuilder() {
        super(asList(
                domain -> StringUtils.isNotBlank(domain.getTitle()) ? Optional.empty() : Optional.of(THE_TITLE_CANNOT_BE_BLANK),
                domain -> domain.getUser() != null ? Optional.empty() : Optional.of(THE_GUEST_BOOK_MUST_BELONG_TO_A_USER)
        ));
    }

    public GuestBookDomainBuilder withTitleAs(String title) {
        guestBookEntity.setTitle(title);
        return this;
    }

    public GuestBookDomainBuilder with(UserEntity userEntity) {
        guestBookEntity.setUser(userEntity);
        return this;
    }

    @Override protected GuestBookDomain addRequiredFields() {
        return new GuestBookDomain(guestBookEntity);
    }

    public static GuestBookDomainBuilder init() {
        return new GuestBookDomainBuilder();
    }
}
