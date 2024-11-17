package in.gurujifoundation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
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
@EqualsAndHashCode(callSuper = false, exclude = {"projectCoordinators", "topics", "schoolProjects"})
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

    @ManyToMany
    @JoinTable(
            name = "project_project_coordinator",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_project_coordinator_project_id")),
            inverseJoinColumns = @JoinColumn(name = "coordinator_id", foreignKey = @ForeignKey(name = "fk_project_project_coordinator_coordinator_id"))
    )
    @Builder.Default
    private Set<ProjectCoordinator> projectCoordinators = new HashSet<>();

    @Column(length = 50)
    private String status;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    @Column()
    @JsonIgnoreProperties({"project"})
    private Set<Topic> topics = new HashSet<>();

    @NotAudited
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SchoolProjectMapping> schoolProjects = new HashSet<>();

}
