package irt.stock.web.configs;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import irt.stock.data.jpa.services.UserService;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//	private final static Logger logger = LogManager.getLogger();

	@Autowired
	UserDetailsService userDetailsService;
	@Autowired UserService userService;

	   @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http
	            .authorizeRequests()
	            .antMatchers("/log_file/scan").hasAnyAuthority("ADMIN")
	            .antMatchers("/webjars/**", "/css/**", "/js/**", "/images/**", "/barcode/**", "/sm/**").permitAll()
	                .anyRequest()
	                	.authenticated()

	                .and()

	                .formLogin()
	                .loginPage("/login")
	                .permitAll()
	                .and()
	            .logout()
	            	.logoutSuccessUrl("/")
	            .and()
	            	.rememberMe().userDetailsService(userDetailsService);
//	            .and()
//	            .httpBasic()
	            ;
	    }

	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {

	    	DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userService);
	        authProvider.setPasswordEncoder(encoder());
	        return authProvider;
	    }
	     
	    @Bean
	    public PasswordEncoder encoder() {
	        return new PasswordEncoder() {
				
				@Override
				public boolean matches(CharSequence userEntry, String encodedPassword) {

					if(encodedPassword.equals("?"))
						return  true;

					final String dbPassword = new String(Base64.getDecoder().decode(encodedPassword));

					return dbPassword.equals(userEntry);
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
