package com.github.jactorrises.model.persistence.entity.blog;

import com.github.jactorrises.persistence.client.entity.BlogEntity;

public class BlogEntryEntityBuilder {
    private BlogOrm blogOrm;
    private String entry;
    private String name;

    private BlogEntryEntityBuilder() {
    }

    public BlogEntryEntityBuilder with(BlogEntity blogEntity) {
        blogOrm = (BlogOrm) blogEntity;
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
