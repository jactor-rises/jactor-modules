package nu.hjemme.persistence.client;

import nu.hjemme.client.domain.Blog;

public interface BlogEntity extends Blog {

    void setTitle(String title);

    void setUserEntity(UserEntity userEntity);

    @Override
    UserEntity getUser();
}
