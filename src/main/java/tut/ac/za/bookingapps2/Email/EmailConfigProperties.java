package tut.ac.za.bookingapps2.Email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "mail")
@PropertySource(value = "classpath:email-config.properties")
public class EmailConfigProperties {
    private Map<String, String> smtp;
}