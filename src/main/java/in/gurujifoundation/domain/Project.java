package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false, exclude = {"schools", "students"})
@ToString()
@Entity
@Table(name = "project", indexes = @Index(name = "idx_school_id", columnList = "school_id"))
public class Project extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(length = 50)
    private String status;

    @ManyToMany
    @JoinTable(
            name = "school_project",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_school_project_project_id")),
            inverseJoinColumns = @JoinColumn(name = "school_id", foreignKey = @ForeignKey(name = "fk_school_project_school_id"))
    )
    @Builder.Default
    private Set<School> schools = new HashSet<>();

    @ManyToMany(mappedBy = "projects")
    @Builder.Default
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Topic> topics = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SchoolProjectMapping> schoolProjects = new HashSet<>();

}
