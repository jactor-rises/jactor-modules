package com.github.jactorrises.model.business.persistence.entity.blog;

public class BlogEntryEntityBuilder {
    private BlogEntity blogEntity;
    private String entry;
    private String name;

    BlogEntryEntityBuilder() {
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
}
