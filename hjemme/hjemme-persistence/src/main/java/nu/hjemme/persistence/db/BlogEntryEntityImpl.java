package nu.hjemme.persistence.db;

import nu.hjemme.client.datatype.Name;
import nu.hjemme.persistence.BlogEntity;
import nu.hjemme.persistence.BlogEntryEntity;
import nu.hjemme.persistence.meta.BlogEntryMetadata;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.hash;

/** @author Tor Egil Jacobsen */
public class BlogEntryEntityImpl extends PersistentEntryImpl implements BlogEntryEntity {

    @Id
    @Column(name = BlogEntryMetadata.ENTRY_ID)
    @SuppressWarnings(value = "unused")
    void setBlogEntryId(Long blogEntryId) {
        setId(blogEntryId);
    }

    @ManyToOne()
    @Column(name = BlogEntryMetadata.BLOG)
    private BlogEntity blogEntity;

    @Column(name = BlogEntryMetadata.CREATION_TIME)
    // @Type(type = PersistenceTypes.PERSISTENT_LOCAL_DATE_TIME)
    public void setCreationTime(/**/LocalDateTime created) {
        super.setCreationTime(created);
    }

    @Column(name = BlogEntryMetadata.ENTRY)
    public void setEntry(String entry) {
        super.setEntry(entry);
    }

    @Column(name = BlogEntryMetadata.CREATED_BY)
    public void setCreatorName(String creatorName) {
        super.setCreatorName(new Name(creatorName));
    }

    @OneToMany
    @Column(name = BlogEntryMetadata.CREATOR)
    public void setCreator(PersonEntityImpl creator) {
        super.setCreator(creator);
    }

    public BlogEntryEntityImpl() {
    }

    public BlogEntryEntityImpl(BlogEntryEntity blogEntryEntity) {
        super(blogEntryEntity);
        blogEntity = blogEntryEntity.getBlog();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BlogEntryEntityImpl that = (BlogEntryEntityImpl) o;

        return harSammePersonSkrevetEnTeksSomErLikTekstenTil(that) && Objects.equals(getBlog(), that.getBlog());
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return hash(super.hashCode(), getBlog());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getBlog())
                .toString();
    }

    @Override
    public BlogEntity getBlog() {
        return blogEntity;
    }

    public void setBlogEntity(BlogEntityImpl blogEntity) {
        this.blogEntity = blogEntity;
    }
}
