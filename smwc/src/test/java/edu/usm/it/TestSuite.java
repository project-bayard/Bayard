package edu.usm.it;

import edu.usm.it.controller.ContactControllerTest;
import edu.usm.it.dao.ContactCommitteesTest;
import edu.usm.it.dao.ContactOrganizationsTest;
import edu.usm.it.dao.DaoPersistenceTest;
import edu.usm.service.CommitteeServiceTest;
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
        OrganizationServiceTest.class,
        ContactCommitteesTest.class,
        CommitteeServiceTest.class
})
public class TestSuite {
}
