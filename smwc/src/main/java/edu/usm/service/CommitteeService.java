package edu.usm.service;

import edu.usm.domain.Committee;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Set;

/**
 * Created by scottkimball on 4/15/15.
 */
public interface CommitteeService {

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    Committee findById (String id);

    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    Set<Committee> findAll();

    @PreAuthorize(value = "hasAuthority('ROLE_ELEVATED')")
    void delete (Committee committee);

    @PreAuthorize(value = "hasAuthority('ROLE_ELEVATED')")
    void update (Committee committee);

    @PreAuthorize(value = "hasAuthority('ROLE_ELEVATED')")
    String create (Committee committee);

    @PreAuthorize(value = "hasAuthority('ROLE_SUPERUSER')")
    void deleteAll();
}
