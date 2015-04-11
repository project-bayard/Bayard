package edu.usm;

import edu.usm.controller.ContactControllerTest;
import edu.usm.dao.ContactOrganizationsTest;
import edu.usm.dao.DaoPersistenceTest;
import edu.usm.service.ContactServiceTest;
import edu.usm.service.OrganizationServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by scottkimball on 4/11/15.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContactControllerTest.class,
        ContactOrganizationsTest.class,
        DaoPersistenceTest.class,
        ContactServiceTest.class,
        OrganizationServiceTest.class
})
public class TestSuite {
}
