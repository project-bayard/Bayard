package edu.usm.domain;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by scottkimball on 4/7/15.
 */

@Entity(name = "basic_entity")
@SQLDelete(sql="UPDATE basic_entity SET deleted = 1 WHERE id = ?")
@Where(clause="deleted <> 1 ")
public abstract class BasicEntity {


    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @JsonView({Views.ContactList.class, Views.OrganizationList.class, Views.CommitteeList.class})
    private String id;

    @Column
    private String created;

    @Column
    private String lastModified;

    @Column
    private boolean deleted;

    public BasicEntity() {
        this.created = LocalDateTime.now().toString();
        this.lastModified = this.created;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BasicEntity) {
            BasicEntity other = (BasicEntity) obj;
            if (this.getId() != null && other.getId() != null)
                return this.getId().equals(other.getId());
            else
                return this.getCreated().equals((other.getCreated()));
        }

        return false;

    }
}
