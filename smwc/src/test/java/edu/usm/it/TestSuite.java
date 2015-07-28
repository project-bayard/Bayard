package edu.usm.it;

import edu.usm.it.controller.*;
import edu.usm.it.dao.ContactCommitteesTest;
import edu.usm.it.dao.ContactEncountersTest;
import edu.usm.it.dao.ContactOrganizationsTest;
import edu.usm.it.dao.DaoPersistenceTest;
import edu.usm.it.service.CommitteeServiceTest;
import edu.usm.it.service.ContactServiceTest;
import edu.usm.it.service.OrganizationServiceTest;
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
        CommitteeServiceTest.class,
        ContactEncountersTest.class,
        OrganizationControllerTest.class,
        EventControllerTest.class,
        CommitteeControllerTest.class,
        UserControllerTest.class
})
public class TestSuite {
}
