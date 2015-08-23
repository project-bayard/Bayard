package edu.usm.service.impl;

import edu.usm.domain.EncounterType;
import edu.usm.repository.EncounterTypeDao;
import edu.usm.service.EncounterTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by scottkimball on 8/17/15.
 */

@Service
public class EncounterTypeServiceImpl implements EncounterTypeService {

    @Autowired
    EncounterTypeDao encounterTypeDao;

    @Override
    public EncounterType findById(String id) {
        return encounterTypeDao.findOne(id);
    }

    @Override
    public EncounterType findByName(String name) {
        return encounterTypeDao.findByName(name);
    }

    @Override
    public String create(EncounterType encounterType) {
        encounterTypeDao.save(encounterType);
        return encounterType.getId();
    }

    @Override
    public void delete(EncounterType encounterType) {
        encounterTypeDao.delete(encounterType);
    }

    @Override
    public void deleteAll() {
        encounterTypeDao.deleteAll();
    }

    @Override
    public Set<EncounterType> findAll() {
        return encounterTypeDao.findAll();
    }

    @Override
    public void delete(String id) {
        encounterTypeDao.delete(id);
    }
}
