package in.gurujifoundation.domain;


import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "performance")
public class Performance extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_performance_student_id"),
            referencedColumnName = "id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false, foreignKey = @ForeignKey(name = "fk_performance_topic_id"))
    private Topic topic;

    @Column(name = "before_intervention_mark")
    private Float beforeInterventionMark;

    @Column(name = "after_intervention_mark")
    private Float afterInterventionMark;

}
