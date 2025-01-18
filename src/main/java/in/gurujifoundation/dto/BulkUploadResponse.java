package in.gurujifoundation.dto;

import in.gurujifoundation.response.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BulkUploadResponse {
    private List<ResponseMessage> messages;
    private UploadStats stats;
}