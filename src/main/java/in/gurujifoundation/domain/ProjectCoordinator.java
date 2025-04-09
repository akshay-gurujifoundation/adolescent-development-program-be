package in.gurujifoundation.domain;


import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EqualsAndHashCode(exclude = {"projects"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project_coordinator")
public class ProjectCoordinator extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "area_of_expertise")
    private String areaOfExpertise;

    @Column(name = "availability")
    private String availability;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "address")
    private String address;

    @ManyToMany(mappedBy = "projectCoordinators")
    private Set<Project> projects = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
