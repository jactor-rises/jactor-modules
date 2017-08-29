package com.github.jactorrises.persistence.boot.entity.guestbook;

final class GuestBookEntryMetadata {
    private GuestBookEntryMetadata() {}

    static final String GUEST_BOOK_ENTRY_TABLE = "T_GUEST_BOOK_ENTRY";

    /** The creation time of an entry */
    static final String CREATED_TIME = "CREATED_TIME";
    /** The name to the creator of this entry */
    static final String GUEST_NAME = "GUEST_NAME";
    /** The entry */
    static final String ENTRY = "ENTRY";
    /** The guest book which is parent of the entry */
    static final String GUEST_BOOK_ID = "GUEST_BOOK_ID";
}
