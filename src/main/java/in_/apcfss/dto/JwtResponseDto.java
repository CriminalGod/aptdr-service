package in_.apcfss.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponseDto {

	private String jwtToken;
	private String jwtRefreshToken;
	private String userType;
	private String orgName;

}
