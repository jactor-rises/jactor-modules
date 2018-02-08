package com.github.jactor.rises.model.domain.blog;

import com.github.jactor.rises.client.converter.FieldConverter;
import com.github.jactor.rises.client.domain.Blog;
import com.github.jactor.rises.client.domain.BlogEntry;
import com.github.jactor.rises.client.dto.BlogDto;
import com.github.jactor.rises.model.domain.PersistentDomain;
import com.github.jactor.rises.model.domain.user.UserDomain;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class BlogDomain extends PersistentDomain implements Blog {

    private final BlogDto blogDto;

    public BlogDomain(BlogDto blogDto) {
        this.blogDto = blogDto;
    }

    @Override public String getTitle() {
        return blogDto.getTitle();
    }

    @Override public UserDomain getUser() {
        return blogDto.getUser() != null ? new UserDomain(blogDto.getUser()) : null;
    }

    @Override public LocalDate getCreated() {
        return FieldConverter.convertDate(getDto().getCreated());
    }

    @Override public Set<BlogEntry> getEntries() {
        return blogDto.getEntries().stream().map(BlogEntryDomain::new).collect(Collectors.toSet());
    }

    @Override public BlogDto getDto() {
        return blogDto;
    }

    static BlogBuilder aBlog() {
        return new BlogBuilder();
    }
}
