package edu.usm.config;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

/**
 * Created by scottkimball on 2/22/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        ApplicationConfig.class,
        JpaConfig.class,
        EmbeddedDataSourceConfig.class

})
public abstract class DaoConfig {
    @Inject
    protected WebApplicationContext wac;


}
