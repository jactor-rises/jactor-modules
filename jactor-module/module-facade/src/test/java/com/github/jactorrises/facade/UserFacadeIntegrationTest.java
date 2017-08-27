package com.github.jactorrises.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.facade.config.JactorBeanContext;
import com.github.jactorrises.facade.config.JactorDbContext;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JactorBeanContext.class, JactorDbContext.class}, loader = AnnotationConfigContextLoader.class)
@Transactional
@Ignore("OutOfMemoryError ???")
public class UserFacadeIntegrationTest {

    @Resource(name = "jactor.userFacade")
    private UserFacade testUserFacade;

    @Test public void willFetchStandardUser() {
        assertThat(testUserFacade.findUsing(new UserName("tip"))).isNotNull();
    }
}
