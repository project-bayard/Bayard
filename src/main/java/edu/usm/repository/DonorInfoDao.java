package edu.usm.repository;

import edu.usm.domain.DonorInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for DonorInfo
 */
public interface DonorInfoDao extends CrudRepository<DonorInfo, String> {

    DonorInfo findByDonations_id(String id);

}
