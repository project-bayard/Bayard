package edu.usm.service.impl;

import edu.usm.domain.DemographicCategory;
import edu.usm.domain.DemographicOption;
import edu.usm.domain.exception.ConstraintMessage;
import edu.usm.domain.exception.ConstraintViolation;
import edu.usm.repository.DemographicCategoryDao;
import edu.usm.service.DemographicCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by andrew on 10/16/15.
 */
@Service
public class DemographicCategoryServiceImpl implements DemographicCategoryService {

    @Autowired
    private DemographicCategoryDao dao;

    @Override
    public String create(DemographicCategory demographicCategory) throws ConstraintViolation{

        if (null == demographicCategory.getName()) {
            throw new ConstraintViolation(ConstraintMessage.DEMOGRAPHIC_CATEGORY_REQUIRED_NAME);
        }

        if (findByName(demographicCategory.getName()) != null) {
            throw new ConstraintViolation(ConstraintMessage.DEMOGRAPHIC_CATEGORY_DUPLICATE);
        }

        for (DemographicOption option: demographicCategory.getOptions()) {
            option.setCategory(demographicCategory);
        }
        validate(demographicCategory);
        dao.save(demographicCategory);
        return demographicCategory.getId();
    }

    @Override
    public void addOption(DemographicOption option, DemographicCategory category) throws ConstraintViolation{

        if (null == option.getName()) {
            throw new ConstraintViolation(ConstraintMessage.DEMOGRAPHIC_OPTION_REQUIRED_NAME);
        }

        for (DemographicOption o: category.getOptions()) {
            if (o.getName().equalsIgnoreCase(option.getName())) {
                return;
            }
        }
        option.setCategory(category);
        category.getOptions().add(option);
        validate(category);
        update(category);
    }

    private void validate(DemographicCategory category) throws ConstraintViolation{
        if (null == category.getName()) {
            throw new ConstraintViolation(ConstraintMessage.DEMOGRAPHIC_CATEGORY_REQUIRED_NAME);
        }
    }

    @Override
    public DemographicCategory findById(String id) {
        return dao.findOne(id);
    }

    @Override
    public DemographicCategory findByName(String category) {
        return dao.findByName(category);
    }

    @Override
    public void update(DemographicCategory demographicCategory) throws ConstraintViolation{
        validate(demographicCategory);
        dao.save(demographicCategory);
    }

    @Override
    public void delete(DemographicCategory demographicCategory) {
        dao.delete(demographicCategory);
    }

    @Override
    public Set<DemographicCategory> findAll() {
        return dao.findAll();
    }

    @Override
    public void deleteAll() {
        Set<DemographicCategory> groups = findAll();
        groups.stream().forEach(this::delete);
    }

}
