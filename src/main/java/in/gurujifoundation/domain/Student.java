package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
@Table(name = "student")
@EqualsAndHashCode(callSuper = false, exclude = {"parent", "projects", "school"})
@ToString(exclude = {"parent"})
@NoArgsConstructor
@AllArgsConstructor
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


    @ManyToMany
    @JoinTable(
            name = "student_project",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @Builder.Default
    private Set<Project> projects = new HashSet<>();

}
