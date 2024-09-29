package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "parent")
@EqualsAndHashCode(callSuper = false)
@ToString
public class Parent extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
}
