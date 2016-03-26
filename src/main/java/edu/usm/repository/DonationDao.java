package edu.usm.repository;

import edu.usm.domain.BudgetItem;
import edu.usm.domain.Donation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottkimball on 2/22/15.
 */

@Repository
public interface DonationDao extends PagingAndSortingRepository<Donation, String> {

    @Override
    HashSet<Donation> findAll();

    Set<Donation> findByBudgetItem(BudgetItem budgetItem);

    Page<Donation> findByDateOfDepositBetween(LocalDate from, LocalDate to, Pageable pageable);

    Page<Donation> findByDateOfReceiptBetween(LocalDate from, LocalDate to, Pageable pageable);

    Page<Donation> findByBudgetItem(BudgetItem item, Pageable pageable);

}
