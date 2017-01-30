package com.whatever.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.whatever.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http
			.authorizeRequests()
				.antMatchers("/", 
							"/search",
							"/about", 
							"/profilephoto/*",
							"/register", 
							"/registrationconfirmed",
							"/invaliduser",
							"/expiredtoken",
							"/verifyemail",
							"/confirmregister")
					.permitAll()
				.antMatchers(
							"/js/*", 
							"/css/*", 
							"/img/*") 
					.permitAll()
				.antMatchers("/addstatus",
							"/editstatus", 
							"/deletestatus", 
						 	"/viewstatus")
			.hasRole("ADMIN") 
				.antMatchers("/profile",
							"/profile/*",
							"/edit-profile-about",
							"/upload-profile-photo",
							"/save-interest",
							"/delete-interest"
										) 
					.authenticated()
				.anyRequest()
				.denyAll()
				.and()
			.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.permitAll()
				.and()
			.logout()
				.permitAll();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
	}
	
}
