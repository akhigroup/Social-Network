package com.whatever;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;


@EnableAsync /** Enable @Async annotation on method level*/
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true) /** Enable-Disable method execution*/
public class App extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return super.configure(builder);
	}
	
	@Bean
	public TilesConfigurer tilesConfigurer(){
		TilesConfigurer tilesConfigurer = new TilesConfigurer();		
		String[] defs = {"/WEB-INF/tiles.xml"};
		tilesConfigurer.setDefinitions(defs);
		return tilesConfigurer;
	}
		
	@Bean
	public UrlBasedViewResolver tilesViewresolver(){
		UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
		tilesViewResolver.setViewClass(TilesView.class);
		return tilesViewResolver;	
	}
	
	@Bean
	public PasswordEncoder getEncoder(){
		return new BCryptPasswordEncoder();
	}
	/** For error page*/
	@Bean
	EmbeddedServletContainerCustomizer errorHamdler(){
		return new EmbeddedServletContainerCustomizer(){

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
			}
			
		};
		
	}
	
	/** Used to sanitize html*/
	@Bean
	PolicyFactory getUserHtmlPolicy(){
		
		return new HtmlPolicyBuilder()
					.allowCommonBlockElements()
					.allowCommonInlineFormattingElements()
					.toFactory();
	}
}
