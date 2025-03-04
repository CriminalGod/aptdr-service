package in_.apcfss.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private long timestamp;
    private String uri;
    private List<String> errors;
    private int errorCode;
    private T data;
}