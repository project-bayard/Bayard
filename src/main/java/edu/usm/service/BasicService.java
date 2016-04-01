package edu.usm.service;

import edu.usm.domain.BasicEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by scottkimball on 4/11/15.
 */
public abstract class BasicService {

    @Autowired
    EntityManager entityManager;
    private Logger logger = LoggerFactory.getLogger(BasicService.class);

    protected void updateLastModified ( BasicEntity entity) {
        entity.setLastModified(LocalDateTime.now().toString());
    }

    protected void emptyStringToNull (BasicEntity entity) {

        try {
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Class clazz = field.getType();

                if (clazz == String.class) {
                    String s = (String) field.get(entity);
                    if (s != null && s.equals("")) {
                        field.set(entity, null);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            logger.error(e.toString());
        }
    }

    protected void updateLastModifiedCollection ( List<? extends BasicEntity> entities) {
        entities.stream().forEach(e -> e.setLastModified(LocalDateTime.now().toString()));
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
