package com.pp1.salve.model.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendWelcomeEmail(String to, String firstName) {
    	SimpleMailMessage mailMessage = new SimpleMailMessage();
    	mailMessage.setTo(to);
    	mailMessage.setSubject("Bem-vindo ao Salve Food!");
    	mailMessage.setText("Olá " + firstName + ",\n\n" +
            	"Sua conta foi criada com sucesso! Estamos felizes em tê-lo conosco.\n\n" +
            	"Equipe Salve Food");
    	mailSender.send(mailMessage);
	}
}
