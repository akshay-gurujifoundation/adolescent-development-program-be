package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true, exclude = {"school"})
@ToString(exclude = {"school"})
@Data
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "teacher")
public class Teacher extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "experience", nullable = false)
    private Integer experience;
}
