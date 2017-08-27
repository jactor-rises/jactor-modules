package com.github.jactorrises.persistence.orm.meta;

/**
 * The meta data of a blog entry.
 */
public final class BlogEntryMetadata {
    private BlogEntryMetadata() {}

    public static final String BLOG_ENTRY_TABLE = "T_BLOG_ENTRY";

    /** The creation time of an entry */
    public static final String CREATED_TIME = "CREATED_TIME";
    /** The name to the creator of this entry */
    public static final String CREATOR_NAME = "CREATOR_NAME";
    /** The entry */
    public static final String ENTRY = "ENTRY";
    /** The primary key of the blog which the entry belongs to */
    public static final String BLOG = "BLOG_ID";

}
