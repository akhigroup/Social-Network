package com.whatever.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.whatever.service.UserService;

@Configuration //Spring va creea un bean cu specificatiile de configurare
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// "/**" inseamna match orice url si sub-url de dupa "/" si da acces la toti utilizatorii
		
		http
			.authorizeRequests() //pentru cererile autorizate catre "/" ,"/js/*"... este permis accesul la oricine
				.antMatchers("/", 
							"/search",
							"/about", 
							"/profilephoto/*",
							"/register", 
							"/registrationconfirmed",
							"/invaliduser",
							"/expiredtoken",
							"/verifyemail",
							"/confirmregister")//dam acces la toata lumea si pe "/register"
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
			.hasRole("ADMIN") //pentru a putea accesa linkurile de mai sus trebuie sa ai role="ROLE_ADMIN"
				.antMatchers("/profile",
							"/profile/*",
							"/edit-profile-about",
							"/upload-profile-photo",
							"/save-interest",
							"/delete-interest"
										) //doar pt cei autentificati
					.authenticated()
				.anyRequest()
				.denyAll() //orice alte url-uri accesate vor avea deny
				.and()
			.formLogin()
				.loginPage("/login") //daca nu suntem autentificati ne redirectioneaza spre "/login"
				.defaultSuccessUrl("/")
				.permitAll()
				.and()
			.logout() //Dam permisiunea la toata lumea sa se poata da logout
				.permitAll();
	}
	
	/*@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		
		auth
			.inMemoryAuthentication()
			.withUser("me") //am creeat un user si parola cu care ne putem autentifica
			.password("hy")
			.roles("USER");
	}*/

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder); //encodam si parola pentru a o putea compara cu cea din baza de date
	}
	
	

}
