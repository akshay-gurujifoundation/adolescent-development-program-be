package in.gurujifoundation.domain;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "performance")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "fk_performance_student_id"),
                referencedColumnName = "id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_performance_project_id"),
                referencedColumnName = "id")
    private Project project;

    @Column(name = "attendance_grade", length = 10)
    private String attendanceGrade;
}
