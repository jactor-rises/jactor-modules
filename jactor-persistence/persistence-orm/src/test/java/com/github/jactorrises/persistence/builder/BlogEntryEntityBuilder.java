package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.blog.BlogEntity;
import com.github.jactorrises.persistence.entity.blog.BlogEntryEntity;

public class BlogEntryEntityBuilder {
    private BlogEntity blogEntity;
    private String entry;
    private String name;

    private BlogEntryEntityBuilder() {
    }

    public BlogEntryEntityBuilder with(BlogEntity blogEntity) {
        this.blogEntity = blogEntity;
        return this;
    }

    public BlogEntryEntityBuilder withEntry(String entry) {
        this.entry = entry;
        return this;
    }

    public BlogEntryEntityBuilder withCreatorName(String name) {
        this.name = name;
        return this;
    }

    public BlogEntryEntity build() {
        BlogEntryEntity blogEntryEntity = new BlogEntryEntity();
        blogEntryEntity.setBlog(blogEntity);
        blogEntryEntity.setCreatorName(name);
        blogEntryEntity.setEntry(entry);

        return blogEntryEntity;
    }

    public static BlogEntryEntityBuilder aBlogEntry() {
        return new BlogEntryEntityBuilder();
    }
}
