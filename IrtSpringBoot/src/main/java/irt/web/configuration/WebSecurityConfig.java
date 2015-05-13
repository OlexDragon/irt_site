package irt.web.configuration;

import irt.web.entities.all.repository.UserRepository;
import irt.web.workers.IrtPasswordEncoder;
import irt.web.workers.beans.IrtUserDetailsServiceImpl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DataSource dataSource;

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	    // @formatter:off
	    auth
	        .userDetailsService(userDetailsService())
	        .passwordEncoder(passwordEncoder());
//	    auth
//	    	.jdbcAuthentication().dataSource(dataSource);
	    // @formatter:on
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    // @formatter:off
		http
			.authorizeRequests()
			.antMatchers(
				"/**")
			.permitAll()
			.anyRequest()
			.authenticated();

		http
			.formLogin()
			.loginPage("/login")
			.permitAll()
		.and()
			.rememberMe();
	    // @formatter:off

//		http.apply(new SpringSocialConfigurer());

		http.csrf().disable();
	}

	@Bean
    public PasswordEncoder passwordEncoder() {
        return new IrtPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new IrtUserDetailsServiceImpl();
    }
}
