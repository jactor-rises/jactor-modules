package com.github.jactorrises.model.domain.blog;

import com.github.jactorrises.commons.builder.AbstractBuilder;
import com.github.jactorrises.model.domain.user.UserBuilder;
import com.github.jactorrises.persistence.client.dto.BlogDto;
import com.github.jactorrises.persistence.client.dto.UserDto;

import java.util.Optional;

import static com.github.jactorrises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactorrises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

public final class BlogBuilder extends AbstractBuilder<BlogDomain> {
    private final BlogDto blogDto = new BlogDto();

    BlogBuilder() {
        super(BlogBuilder::validateInstance);
    }

    BlogBuilder withTitleAs(String title) {
        blogDto.setTitle(title);
        return this;
    }

    public BlogBuilder with(UserDto userDto) {
        blogDto.setUser(userDto);
        return this;
    }

    public BlogBuilder with(UserBuilder userBuilder) {
        return with(userBuilder.build().getPersistence());
    }

    @Override protected BlogDomain buildBean() {
        return new BlogDomain(blogDto);
    }

    private static Optional<String> validateInstance(BlogDomain blogDomain) {
        return collectMessages(
                fetchMessageIfStringWithoutValue("title", blogDomain.getTitle()),
                fetchMessageIfFieldNotPresent("user", blogDomain.getUser())
        );
    }

    public static BlogDomain build(BlogDto blogDto) {
        return new BlogDomain(blogDto);
    }
}
