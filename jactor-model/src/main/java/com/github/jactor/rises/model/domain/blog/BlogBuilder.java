package com.github.jactor.rises.model.domain.blog;

import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.client.dto.UserDto;
import com.github.jactor.rises.commons.builder.AbstractBuilder;
import com.github.jactor.rises.model.domain.user.UserBuilder;

import java.util.Optional;

import static com.github.jactor.rises.commons.builder.ValidInstance.collectMessages;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfFieldNotPresent;
import static com.github.jactor.rises.commons.builder.ValidInstance.fetchMessageIfStringWithoutValue;

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
        return with(userBuilder.build().getDto());
    }

    public BlogBuilder with(BlogEntryBuilder blogEntryBuilder) {
        blogEntryBuilder.with(blogDto);
        blogDto.getEntries().add(blogEntryBuilder.build().getDto());

        return this;
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
