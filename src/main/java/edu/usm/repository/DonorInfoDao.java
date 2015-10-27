package edu.usm.repository;

import edu.usm.domain.DonorInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by scottkimball on 2/22/15.
 */
public interface DonorInfoDao extends CrudRepository<DonorInfo, String> {
}
