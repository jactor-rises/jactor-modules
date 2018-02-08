package com.github.jactor.rises.model.domain.guestbook;

import com.github.jactor.rises.client.dto.GuestBookDto;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.model.domain.user.UserDomain;

import java.util.Optional;

import static com.github.jactor.rises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

public final class GuestBookBuilder extends AbstractBuilder<GuestBookDomain> {
    private final GuestBookDto guestBookDto = new GuestBookDto();

    GuestBookBuilder() {
        super(GuestBookBuilder::validateInstance);
    }

    public GuestBookBuilder withTitle(String title) {
        guestBookDto.setTitle(title);
        return this;
    }

    public GuestBookBuilder with(UserDto userDto) {
        guestBookDto.setUser(userDto);
        return this;
    }

    public GuestBookBuilder with(UserDomain userDomain) {
        with(userDomain.getDto());
        return this;
    }

    @Override protected GuestBookDomain buildBean() {
        return new GuestBookDomain(guestBookDto);
    }

    private static Optional<String> validateInstance(GuestBookDomain guestBookDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("title", guestBookDomain.getTitle()),
                fetchMessageIfFieldNotPresent("user", guestBookDomain.getUser())
        );
    }

    public static GuestBookDomain build(GuestBookDto guestBook) {
        return new GuestBookDomain(guestBook);
    }
}
