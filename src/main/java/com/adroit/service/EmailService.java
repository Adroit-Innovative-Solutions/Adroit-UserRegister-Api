package com.adroit.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private static Logger logger= LoggerFactory.getLogger(EmailService.class);

    public void sendEmail(String to,String subject,String body){

        logger.info("Sending Email to "+to);
        MimeMessage  mimeMessage=javaMailSender.createMimeMessage();
        try{
            MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true,"UTF-8");

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body,true);
            mimeMessageHelper.setFrom("notificationsdataqinc@gmail.com");

            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            logger.error("Failed to Sent Email To :"+to);
            e.printStackTrace();
            throw new RuntimeException("Failed to send Email");
        }
    }

}
