package com.whatever.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	// 	Sunt datele de configurare pt a utiliza un serviciu de email (luate de pe www.mailtrap.io) 
	/*	mail.smtp.host=mailtrap.io
		mail.smtp.port=2525
		mail.smtp.user=01e3fb41759602
		mail.smtp.pass=329bdff689cc1b*/
	
	@Value("${mail.smtp.host}") //ce este in interiorul lui @Value se numeste Spring expression language - SPEL, asemanator cu java expression lang din .jsp
	private String host;
	
	@Value("${mail.smtp.port}")
	private Integer port;
	
	@Value("${mail.smtp.user}")
	private String user;
	
	@Value("${mail.smtp.pass}")
	private String password;
	
	@Bean //pentru ca Spring sa creeze un obiect si a putea utiliza @Autowired in clasa unde vrem sa il folosim
	public JavaMailSender mailSender(){ //este o interfata Java pe care o putem folosii pt ca am adugat in pom.xml un dependecy pt el
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(user);
		mailSender.setPassword(password);
		
		return mailSender;
	}
}
