package edu.usm.service.impl;

import edu.usm.domain.SustainerPeriod;
import edu.usm.repository.SustainerPeriodDao;
import edu.usm.service.SustainerPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by scottkimball on 4/4/16.
 */

@Service
public class SustainerPeriodServiceImpl implements SustainerPeriodService {

    @Autowired
    SustainerPeriodDao sustainerPeriodDao;

    @Override
    public SustainerPeriod findById(String id) {
        return sustainerPeriodDao.findOne(id);
    }

    @Override
    public void delete(String id) {
        sustainerPeriodDao.delete(id);
    }

    @Override
    public Set<SustainerPeriod> findAll() {
        return sustainerPeriodDao.findAll();
    }
}
