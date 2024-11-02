package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false, exclude = {"school"})
@ToString(exclude = {"school"})
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

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_school_id"),
            referencedColumnName = "id")
    private School school;
}
