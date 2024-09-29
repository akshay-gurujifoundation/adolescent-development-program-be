package in.gurujifoundation.domain;

import in.gurujifoundation.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = false, exclude = {"teachers"})
@ToString(exclude = {"teachers"})
@Entity
@Table(name = "school")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Audited
@EntityListeners(AuditingEntityListener.class)
@Builder
public class School extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "principal_name")
    private String principalName;

    @Column(name = "principal_contact_no")
    private String principalContactNo;

    @Column(name = "managing_trustee")
    private String managingTrustee;

    @Column(name = "trustee_contact_info")
    private String trusteeContactInfo;

    @Column(name = "website")
    private String website;

    @OneToMany(mappedBy = "school")
    @Builder.Default
    private List<Teacher> teachers = new ArrayList<>();
}
