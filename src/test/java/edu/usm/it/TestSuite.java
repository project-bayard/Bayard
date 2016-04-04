package edu.usm.it;

import edu.usm.it.controller.*;
import edu.usm.it.dao.*;
import edu.usm.it.service.*;
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
        FoundationPersistenceTest.class,
        SustainerPeriodPersistenceTest.class,
        GrantServiceTest.class,
        FoundationServiceTest.class,
        InteractionRecordServiceTest.class,
        ConfigControllerTest.class,
        DonationControllerTest.class,
        FoundationControllerTest.class,
        GrantControllerTest.class,
        InteractionRecordControllerTest.class
})
@ActiveProfiles("test")
public class TestSuite {
}
