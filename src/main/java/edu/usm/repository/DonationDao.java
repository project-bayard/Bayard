package edu.usm.repository;

import edu.usm.domain.Donation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Repository for {@link Donation}
 */

@Repository
public interface DonationDao extends CrudRepository<Donation, String> {

    /**
     * Returns all Donations
     * @return {@link java.util.Set} of {@link Donation}
     */
    @Override
    HashSet<Donation> findAll();

}
