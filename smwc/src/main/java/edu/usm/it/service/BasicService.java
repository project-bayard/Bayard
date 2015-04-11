package edu.usm.it.service;

import edu.usm.domain.BasicEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by scottkimball on 4/11/15.
 */
public abstract class BasicService {

    protected void updateLastModified ( BasicEntity entity) {
        entity.setLastModified(LocalDateTime.now());

    }

    protected void updateLastModifiedCollection ( List<? extends BasicEntity> entities) {
        entities.stream().forEach(e -> e.setLastModified(LocalDateTime.now()));
    }
}
