package edu.usm.repository;

import edu.usm.domain.BudgetItem;
import edu.usm.domain.Donation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface DonationDao extends CrudRepository<Donation, String> {

    @Override
    HashSet<Donation> findAll();

    Set<Donation> findByBudgetItem(BudgetItem budgetItem);

}
