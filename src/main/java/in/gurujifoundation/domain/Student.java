package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "student")
@EqualsAndHashCode(callSuper = false, exclude = {"parent"})
@ToString(exclude = {"parent"})
public class Student extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "alternative_number")
    private String alternativeNumber;

    @Column(name = "email", nullable = false)
    private String email;
}
