package in.gurujifoundation.request;

import lombok.Data;

@Data
public class CreateOrUpdateTeacherRequest {

    private String name;
    private Integer experience;
    private Long schoolId;
}
