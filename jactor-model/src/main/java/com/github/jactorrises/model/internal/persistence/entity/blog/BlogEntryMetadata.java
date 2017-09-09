package com.github.jactorrises.model.internal.persistence.entity.blog;

/**
 * The meta data of a blog entry.
 */
final class BlogEntryMetadata {
    private BlogEntryMetadata() {}

    static final String BLOG_ENTRY_TABLE = "T_BLOG_ENTRY";

    /** The creation time of an entry */
    static final String CREATED_TIME = "CREATED_TIME";
    /** The name to the creator of this entry */
    static final String CREATOR_NAME = "CREATOR_NAME";
    /** The entry */
    static final String ENTRY = "ENTRY";
    /** The primary key of the blog which the entry belongs to */
    static final String BLOG = "BLOG_ID";
}
