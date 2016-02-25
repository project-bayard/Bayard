package edu.usm.repository;

import edu.usm.domain.UserFileUpload;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by andrew on 1/24/16.
 */
public interface UserFileUploadDao extends CrudRepository<UserFileUpload, String> {
}
