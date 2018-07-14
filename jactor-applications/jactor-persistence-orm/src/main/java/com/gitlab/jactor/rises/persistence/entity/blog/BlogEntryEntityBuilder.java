package com.gitlab.jactor.rises.persistence.entity.blog;

import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;

import java.util.Optional;

public class BlogEntryEntityBuilder extends AbstractBuilder<BlogEntryEntity> {
    private BlogEntity blogEntity;
    private String entry;
    private String name;

    BlogEntryEntityBuilder() {
        super(BlogEntryEntityBuilder::validate);
    }

    public BlogEntryEntityBuilder with(BlogEntity blogEntity) {
        this.blogEntity = blogEntity;
        return this;
    }

    public BlogEntryEntityBuilder with(BlogEntityBuilder blogEntityBuilder) {
        return with(blogEntityBuilder.build());
    }

    public BlogEntryEntityBuilder withEntry(String entry) {
        this.entry = entry;
        return this;
    }

    public BlogEntryEntityBuilder withCreatorName(String name) {
        this.name = name;
        return this;
    }

    @Override protected BlogEntryEntity buildBean() {
        BlogEntryEntity blogEntryEntity = new BlogEntryEntity();
        blogEntryEntity.create(entry);
        blogEntryEntity.setBlog(blogEntity);
        blogEntryEntity.setCreatorName(name);

        return blogEntryEntity;
    }

    private static Optional<MissingFields> validate(BlogEntryEntity blogEntryEntity, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenBlank("entry", blogEntryEntity.getEntry());
        missingFields.addInvalidFieldWhenBlank("name", blogEntryEntity.getCreatorName());
        missingFields.addInvalidFieldWhenNoValue("blog", blogEntryEntity.getBlog());

        return missingFields.presentWhenFieldsAreMissing();
    }
}
