package com.github.jactorrises.model.internal.persistence.entity.blog;

/**
 * The meta data of an persistent blog
 */
public final class BlogMetadata {
    private BlogMetadata() {}

    public static final String BLOG_TABLE = "T_BLOG";

    /** The title of the blog */
    public static final String TITLE = "TITLE";
    /** The user who is the creator of the blog */
    public static final String USER = "USER_ID";
    /** The time the blog was crated */
    public static final String CREATED = "CREATED";
}
