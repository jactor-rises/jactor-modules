package com.github.jactorrises.model.business.persistence.entity.guestbook;

final class GuestBookMetadata {
    private GuestBookMetadata() {}

    static final String GUEST_BOOK_TABLE = "T_GUEST_BOOK";

    /** The title for the guest book */
    static final String TITLE = "TITLE";
    /** The user which the guest book belongs to */
    static final String USER = "USER_ID";
}
