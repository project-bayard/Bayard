package edu.usm.repository;

import edu.usm.domain.MemberInfo;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by scottkimball on 2/22/15.
 */
public interface MemberInfoDao extends CrudRepository<MemberInfo, Long> {
}
