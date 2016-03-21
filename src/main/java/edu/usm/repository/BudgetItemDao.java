package edu.usm.repository;

import edu.usm.domain.BudgetItem;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;

/**
 * Created by andrew on 3/7/16.
 */
public interface BudgetItemDao extends CrudRepository<BudgetItem, String> {

    @Override
    HashSet<BudgetItem> findAll();

}
