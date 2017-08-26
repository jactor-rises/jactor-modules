package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.GuestBookDomain;
import nu.hjemme.persistence.client.GuestBookEntity;
import nu.hjemme.persistence.client.UserEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public final class GuestBookDomainBuilder extends DomainBuilder<GuestBookDomain> {
    static final String THE_GUEST_BOOK_MUST_BELONG_TO_A_USER = "The guest book must belong to a user";
    static final String THE_TITLE_CANNOT_BE_BLANK = "The title cannot be blank";

    private final GuestBookEntity guestBookEntity = newInstanceOf(GuestBookEntity.class);

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

    @Override protected GuestBookDomain initWithRequiredFields() {
        return new GuestBookDomain(guestBookEntity);
    }

    public static GuestBookDomainBuilder init() {
        return new GuestBookDomainBuilder();
    }
}
