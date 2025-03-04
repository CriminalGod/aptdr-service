package in_.apcfss.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "cors")
@Getter
@Setter
public class CorsConfigProperties {

    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    private List<String> allowedHeaders;

    private boolean allowCredentials;

    private Long maxAge;

}