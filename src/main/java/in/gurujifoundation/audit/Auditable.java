package in.gurujifoundation.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
public abstract class Auditable {

    @CreatedBy
    @JsonIgnore
    @Column(name = "created_by", nullable = false)
    protected String createdBy;

    @CreatedDate
    @JsonIgnore
    @Temporal(TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    protected Date createdAt;

    @LastModifiedBy
    @JsonIgnore
    @Column(name = "updated_by", nullable = false)
    protected String updatedBy;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    @JsonIgnore
    @Column(name = "updated_at", nullable = false)
    protected Date updatedAt;
}
