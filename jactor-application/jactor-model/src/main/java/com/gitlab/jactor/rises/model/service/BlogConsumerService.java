package com.gitlab.jactor.rises.model.service;

import com.gitlab.jactor.rises.commons.dto.BlogDto;
import com.gitlab.jactor.rises.commons.dto.BlogEntryDto;

import java.io.Serializable;

public interface BlogConsumerService {
    BlogDto saveOrUpdate(BlogDto blogDto);

    BlogEntryDto saveOrUpdate(BlogEntryDto blogEntryDto);

    BlogDto fetch(Serializable id);

    BlogEntryDto fetchEntry(Serializable entryId);
}
