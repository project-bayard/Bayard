package edu.usm.service;

import edu.usm.domain.Donation;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by andrew on 2/25/16.
 */
public interface DonationService {

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Donation findById(String id);

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    Set<Donation> findAll();

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    String create(Donation donation);

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Donation donation);

    @PreAuthorize(value = "hasAnyRole('ROLE_DEVELOPMENT','ROLE_ELEVATED','ROLE_SUPERUSER')")
    void update(Donation oldDonation, Donation newDonation);

    @PreAuthorize(value = "hasAnyRole('ROLE_ELEVATED','ROLE_SUPERUSER')")
    void delete(Donation donation);

    @PreAuthorize(value = "hasAnyRole('ROLE_SUPERUSER')")
    void deleteAll();

}
