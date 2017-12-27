package com.github.jactorrises.persistence.builder;

import com.github.jactorrises.persistence.entity.blog.BlogEntryOrm;
import com.github.jactorrises.persistence.entity.blog.BlogOrm;

public class BlogEntryEntityBuilder {
    private BlogOrm blogOrm;
    private String entry;
    private String name;

    private BlogEntryEntityBuilder() {
    }

    public BlogEntryEntityBuilder with(BlogOrm blogEntity) {
        blogOrm = blogEntity;
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

    public BlogEntryOrm build() {
        BlogEntryOrm blogEntryOrm = new BlogEntryOrm();
        blogEntryOrm.setBlog(blogOrm);
        blogEntryOrm.setCreatorName(name);
        blogEntryOrm.setEntry(entry);

        return blogEntryOrm;
    }

    public static BlogEntryEntityBuilder aBlogEntry() {
        return new BlogEntryEntityBuilder();
    }
}
