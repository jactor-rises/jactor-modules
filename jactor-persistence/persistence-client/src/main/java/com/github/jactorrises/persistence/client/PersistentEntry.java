package com.github.jactorrises.persistence.client;

import com.github.jactorrises.client.domain.Entry;

public interface PersistentEntry extends Entry {

    void setEntry(String entry);

    void setCreatorName(String creatorName);
}
