package irt.stock.web.configs;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	UserDetailsService userDetailsService;

	public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/").setViewName("home");
//        registry.addViewController("/index").setViewName("home");
//        registry.addViewController("/login").setViewName("home");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }
     
    @Bean
    public PasswordEncoder encoder() {
        return new PasswordEncoder() {
			
			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				final String password = new String(Base64.getDecoder().decode(encodedPassword));
//				LogManager.getLogger().error("rawPassword {}; password {}", rawPassword, password);
				return password.equals(rawPassword);
			}
			
			@Override
			public String encode(CharSequence rawPassword) {
		        return rawPassword==null || rawPassword.equals("?")
		        		? "?"
		        				: Base64.getEncoder().encodeToString(rawPassword.toString().getBytes());
		    }
		};
    }
}