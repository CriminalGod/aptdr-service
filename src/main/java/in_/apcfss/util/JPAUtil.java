package in_.apcfss.util;

import in_.apcfss.dto.ApiResponse;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

public class JPAUtil {

    private JPAUtil() {
    }

    public static <T> ApiResponse<List<T>> pageToAPIResponse(Page<T> page) {
        ApiResponse<List<T>> apiResponse = ApiResponseUtil.success(page.getContent(), "", null);
        apiResponse.setMetadata(PageMetadata.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build());
        return apiResponse;
    }

}

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
class PageMetadata {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
