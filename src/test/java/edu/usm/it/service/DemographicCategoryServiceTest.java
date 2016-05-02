package edu.usm.it.service;

import edu.usm.config.WebAppConfigurationAware;
import edu.usm.domain.DemographicCategory;
import edu.usm.domain.DemographicOption;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.service.DemographicCategoryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by andrew on 10/16/15.
 */
public class DemographicCategoryServiceTest extends WebAppConfigurationAware {

    @Autowired
    private DemographicCategoryService service;

    private Set<DemographicOption> options;
    private DemographicOption option1;
    private DemographicOption option2;

    @Before
    public void setup() {

        option1 = new DemographicOption();
        option1.setName("Option 1");

        option2 = new DemographicOption();
        option2.setName("Option 2");

        options = new HashSet<>();
        options.add(option1);
        options.add(option2);
    }

    @After
    public void tearDown() {
        service.deleteAll();
    }

    @Test
    public void testCreate() throws Exception{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        DemographicOption option = new DemographicOption();
        option.setName("An Option");
        demographicCategory.getOptions().add(option);
        String id = service.create(demographicCategory);

        DemographicCategory fromDb = service.findById(id);
        assertNotNull(fromDb);
        assertEquals(demographicCategory.getName(), fromDb.getName());
        assertEquals(demographicCategory.getOptions().iterator().next(), fromDb.getOptions().iterator().next());

    }

    @Test
    public void testDelete() throws Exception{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        demographicCategory.setOptions(options);
        String id = service.create(demographicCategory);

        demographicCategory = service.findById(id);
        assertNotNull(demographicCategory);

        service.delete(demographicCategory);

        demographicCategory = service.findById(id);
        assertNull(demographicCategory);
    }

    @Test
    public void testAddOption() throws Exception{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        demographicCategory.setOptions(options);
        String id = service.create(demographicCategory);

        DemographicOption option = new DemographicOption();
        option.setName("Another Option");

        DemographicCategory fromDb = service.findById(id);
        service.addOption(option, fromDb);

        fromDb = service.findById(id);
        assertEquals(demographicCategory.getOptions().size() + 1, fromDb.getOptions().size());
    }

    @Test(expected = ConstraintViolation.class)
    public void testAddOptionSameName() throws Exception{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        demographicCategory.setOptions(options);
        String id = service.create(demographicCategory);

        DemographicOption option = new DemographicOption();
        option.setName("Another Option");

        DemographicCategory fromDb = service.findById(id);
        service.addOption(option, fromDb);

        fromDb = service.findById(id);
        assertEquals(demographicCategory.getOptions().size() + 1, fromDb.getOptions().size());

        DemographicOption optionDuplicateName = new DemographicOption();
        optionDuplicateName.setName(option.getName());
        service.addOption(optionDuplicateName, fromDb);
    }

    @Test(expected = ConstraintViolation.class)
    public void testAddOptionWithoutName() throws ConstraintViolation{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        demographicCategory.setOptions(options);
        String id = service.create(demographicCategory);

        DemographicOption option = new DemographicOption();

        DemographicCategory fromDb = service.findById(id);
        service.addOption(option, fromDb);
    }

    @Test(expected = ConstraintViolation.class)
    public void testCreateCategoryWithoutName() throws ConstraintViolation{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(null);
        demographicCategory.setOptions(options);
        service.create(demographicCategory);
    }

    @Test(expected = ConstraintViolation.class)
    public void testUpdateCategoryWithoutName() throws ConstraintViolation {
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        demographicCategory.setOptions(options);
        String id = service.create(demographicCategory);
        DemographicCategory fromDb = service.findById(id);
        fromDb.setName(null);
        service.update(fromDb);
    }

    @Test
    public void testFindByCategory() throws Exception{
        DemographicCategory demographicCategory = new DemographicCategory();
        demographicCategory.setName(DemographicCategory.CATEGORY_ETHNICITY);
        demographicCategory.setOptions(options);
        String id = service.create(demographicCategory);

        DemographicCategory fromDb = service.findByName(demographicCategory.getName());
        assertEquals(id, fromDb.getId());
    }

}
