package edu.usm.it;

import edu.usm.domain.SustainerPeriod;
import edu.usm.it.controller.*;
import edu.usm.it.dao.*;
import edu.usm.it.service.*;
import edu.usm.service.EventService;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ActiveProfiles;

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
        UserControllerTest.class,
        EncounterServiceTest.class,
        EventServiceTest.class,
        EncounterTypeControllerTest.class,
        ControllerExceptionHandlerTest.class,
        ContactEventsTest.class,
        GroupServiceTest.class,
        GroupControllerTest.class,
        DemographicCategoryServiceTest.class,
        DemographicCategoryControllerTest.class,
        UserServiceTest.class,
        DonationsDaoTest.class,
        SustainerPeriodPersistenceTest.class
})
@ActiveProfiles("test")
public class TestSuite {
}
