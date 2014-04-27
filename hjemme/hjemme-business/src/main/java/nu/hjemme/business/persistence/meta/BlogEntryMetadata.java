package nu.hjemme.business.persistence.meta;

/**
 * The meta data of a blog entry.
 * @author Tor Egil Jacobsen
 */
public final class BlogEntryMetadata {
    private BlogEntryMetadata() {
    }

    /** The creation time of an entry */
    public static final String CREATION_TIME = "CREATION_TIME";
    /** The name to the creator of this entry */
    public static final String CREATED_BY = "CREATED_BY";
    /** The creator of this entry */
    public static final String CREATOR = "CREATOR";
    /** The entry */
    public static final String ENTRY = "ENTRY";
    /** The primary key of an entry */
    public static final String ENTRY_ID = "ENTRY_ID";
    /** The primary key of the blog which the entry belongs to */
    public static final String BLOG = "BLOG_ID";

}