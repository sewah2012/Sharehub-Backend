package com.luslusdawmpfe.PFEBackent.utils;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
@Slf4j
//@ConfigurationProperties(prefix = "email.prop")
public class EmailSender {

    private final JavaMailSender mailSender;
    private String toAddress;
    private String fromAddress = "support@sharehub.test";
    private String senderName = "Share Hub";
    private String subject = "Please verify your registration";
    private String content = "Dear [[name]],<br>"
            + "Please click the link below to verify your registration:<br>"
            + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
            + "Thank you,<br>"
            + "Your company name.";

  public void sendEmail(AppUser user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
    log.info("Sending email to "+user.getEmail());
      toAddress = user.getEmail();

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(fromAddress, senderName);
    helper.setTo(toAddress);
    helper.setSubject(subject);

    content = content.replace("[[name]]",user.getFirstName());
    String verifyURL = siteUrl+"/verify?code="+user.getVerificationCode();
    content = content.replace("[[URL]]",verifyURL);

    helper.setText(content, true);

    mailSender.send(message);
    log.info("Email send successfully: "+content);

  }
}
