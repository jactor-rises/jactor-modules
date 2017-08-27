package com.github.jactorrises.persistence.orm.meta;

public final class GuestBookEntryMetadata {
    private GuestBookEntryMetadata() {}

    public static final String GUEST_BOOK_ENTRY_TABLE = "T_GUEST_BOOK_ENTRY";

    /** The creation time of an entry */
    public static final String CREATED_TIME = "CREATED_TIME";
    /** The name to the creator of this entry */
    public static final String GUEST_NAME = "GUEST_NAME";
    /** The entry */
    public static final String ENTRY = "ENTRY";
    /** The guest book which is parent of the entry */
    public static final String GUEST_BOOK_ID = "GUEST_BOOK_ID";
}
