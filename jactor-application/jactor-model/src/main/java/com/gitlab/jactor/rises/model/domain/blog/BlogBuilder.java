package com.gitlab.jactor.rises.model.domain.blog;

import com.gitlab.jactor.rises.commons.builder.AbstractBuilder;
import com.gitlab.jactor.rises.commons.builder.MissingFields;
import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.UserDto;
import com.gitlab.jactor.rises.model.domain.user.UserBuilder;

import java.util.Optional;

public final class BlogBuilder extends AbstractBuilder<BlogDomain> {
    private final BlogDto blogDto = new BlogDto();

    BlogBuilder() {
        super(BlogBuilder::validateInstance);
    }

    BlogBuilder withTitle(String title) {
        blogDto.setTitle(title);
        return this;
    }

    public BlogBuilder with(UserDto userDto) {
        blogDto.setUser(userDto);
        return this;
    }

    public BlogBuilder with(UserBuilder userBuilder) {
        return with(userBuilder.build().getDto());
    }

    @Override protected BlogDomain buildBean() {
        return new BlogDomain(blogDto);
    }

    private static Optional<MissingFields> validateInstance(BlogDomain blogDomain, MissingFields missingFields) {
        missingFields.addInvalidFieldWhenNoValue(BlogDomain.class.getSimpleName(), "title", blogDomain.getTitle());
        missingFields.addInvalidFieldWhenNoValue(BlogDomain.class.getSimpleName(), "user", blogDomain.getUser());

        return missingFields.presentWhenFieldsAreMissing();
    }

    public static BlogDomain build(BlogDto blogDto) {
        return new BlogDomain(blogDto);
    }
}
