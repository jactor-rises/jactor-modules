package nu.hjemme.persistence.orm.meta;

public final class GuestBookMetadata {
    private GuestBookMetadata() {}

    public static final String GUEST_BOOK_TABLE = "T_GUEST_BOOK";

    /** The title for the guest book */
    public static final String TITLE = "TITLE";
    /** The user which the guest book belongs to */
    public static final String USER = "USER_ID";

}
