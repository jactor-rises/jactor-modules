package com.github.jactorrises.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.persistence.HibernateConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {JactorModule.class, HibernateConfiguration.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserFacadeIntegrationTest {

    @Resource(name = "jactor.userFacade")
    private UserFacade testUserFacade;

    @Test public void willFetchStandardUser() {
        assertThat(testUserFacade.findUsing(new UserName("tip"))).isNotNull();
    }
}
