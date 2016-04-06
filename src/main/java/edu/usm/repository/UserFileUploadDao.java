package edu.usm.repository;

import edu.usm.domain.UserFileUpload;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for user file uploads.
 */
public interface UserFileUploadDao extends CrudRepository<UserFileUpload, String> {
}
