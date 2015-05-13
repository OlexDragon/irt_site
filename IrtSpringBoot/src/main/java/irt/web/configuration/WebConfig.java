package irt.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
public class WebConfig extends WebMvcConfigurerAdapter {
}
