package com.pp1.salve.model.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.properties.mail.smtp.auth}")
	private String smtpAuth;

	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private String starttls;

	@Bean
	public JavaMailSender javaMailSender() {
    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    	mailSender.setHost(this.host);
    	mailSender.setPort(this.port);
    	mailSender.setUsername(this.username);
    	mailSender.setPassword(this.password);

    	Properties props = mailSender.getJavaMailProperties();
    	props.put("mail.smtp.auth", this.smtpAuth);
    	props.put("mail.smtp.starttls.enable", this.starttls);
    	return mailSender;
	}
}
