package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.BlogEntryDomain;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.persistence.db.BlogEntityImpl;
import nu.hjemme.persistence.db.BlogEntryEntityImpl;
import nu.hjemme.persistence.db.PersonEntityImpl;
import org.apache.commons.lang.Validate;

public class BlogEntryDomainBuilder extends DomainBuilder<BlogEntryDomain> {
    static final String THE_ENTRY_MUST_BELONG_TO_A_BLOG = "The blog entry must belong to a blog";
    static final String THE_ENTRY_CANNOT_BE_EMPTY = "The entry field cannot be empty";
    static final String THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE = "The entry must be created by someone";

    private BlogEntryEntityImpl blogEntryEntity = new BlogEntryEntityImpl();

    public BlogEntryDomainBuilder withCreatorNameAs(String creatorName) {
        blogEntryEntity.setCreatorName(new Name(creatorName));
        return this;
    }

    public BlogEntryDomainBuilder with(PersonEntityImpl creator) {
        blogEntryEntity.setCreator(creator);
        blogEntryEntity.setCreatorName(creator.getFirstName());
        return this;
    }

    public BlogEntryDomainBuilder withEntryAs(String entry) {
        blogEntryEntity.setEntry(entry);
        return this;
    }

    public BlogEntryDomainBuilder with(BlogEntityImpl blogEntity) {
        blogEntryEntity.setBlog(blogEntity);
        return this;
    }

    @Override protected BlogEntryDomain initDomain() {
        return new BlogEntryDomain(blogEntryEntity);
    }

    @Override protected void validate() {
        Validate.notEmpty(blogEntryEntity.getEntry(), THE_ENTRY_CANNOT_BE_EMPTY);
        Validate.notNull(blogEntryEntity.getCreatorName(), THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE);
        Validate.notNull(blogEntryEntity.getBlog(), THE_ENTRY_MUST_BELONG_TO_A_BLOG);
    }

    static BlogEntryDomainBuilder init() {
        return new BlogEntryDomainBuilder();
    }
}
