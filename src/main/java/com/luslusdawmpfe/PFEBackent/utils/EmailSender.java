package com.luslusdawmpfe.PFEBackent.utils;

import com.luslusdawmpfe.PFEBackent.entities.AppUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final String fromAddress = "support@sharehub.test";
    private final String senderName = "Share Hub";
    private final String website = "http://localhost:3000/";


@Async
  public void sendVerificationEmail(AppUser user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
      final String emailTemplate = "Dear [[name]],<br>"
              + "Please find below your Sharehub email verification code:<br>"
              + "<h2>Email Verification Code</h2> : <h3>[[code]]</h3>"
              + "Thank you,<br>"
              + "Emmanuel from <span>ShareHub</span>";

    log.info("Sending email to "+user.getEmail());
      String subject= "Please verify your registration";
      String content = emailTemplate;

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setFrom(fromAddress, senderName);
    helper.setTo(user.getEmail());
    helper.setSubject(subject);

    content = content.replace("[[name]]", user.getFirstName());
//    String verifyURL = siteUrl+"/auth/verify?code=" + user.getVerificationCode();
    content = content.replace("[[code]]", user.getVerificationCode());

    helper.setText(content, true);
      log.info("Email to be sent: "+content);

    mailSender.send(message);


    log.info("Email sent successfully: "+content);


    //clear reset content
      content = emailTemplate;

  }

  @Async
  public void sendUserCredentials (AppUser user, String defaultPassword) throws MessagingException, UnsupportedEncodingException {
        final String emailTemplate = "Dear [[name]],<br>"
                + "A new Sharehub account has successfully been created for you:<br>"
                +"<h3> Below are your initial login credentials: </h3><br>"
                +"username: <b>[[username]]</b><br>"
                +"password: <b>[[password]]</b><br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Click to continue to sharehub website</a></h3>"
                + "Thank you,<br>"
                + "Emmanuel from <span>ShareHub</span>";

        log.info("Sending email to "+user.getEmail());
        String subject= "ShareHub Login Credentials:";
        String content = emailTemplate;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        content = content.replace("[[URL]]", this.website);
        content = content.replace("[[username]]",user.getUsername());
        content = content.replace("[[password]]", defaultPassword);

        helper.setText(content, true);
        log.info("Email to be sent: "+content);

        mailSender.send(message);


        log.info("Email send successfully: "+content);
    }

    @Async
    public void sendResetPasswordLink (AppUser user) throws MessagingException, UnsupportedEncodingException {
        final String emailTemplate = "Dear [[name]],<br><br>"
                + "Please find below your Sharehub password reset code:<br>"
                + "<h2>Reset Password Code</h2> : <h3>[[code]]</h3>"
                + "Thank you,<br>"
                + "Emmanuel from <span>ShareHub</span>";

        log.info("Sending email to "+user.getEmail());
        String subject= "ShareHub Password Reset";
        String content = emailTemplate;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        content = content.replace("[[code]]", user.getVerificationCode());

        helper.setText(content, true);
        log.info("Email to be sent: "+content);

        mailSender.send(message);


        log.info("Email send successfully: "+content);
    }

    @Async
    public void sendResetPasswordCredentials(AppUser user, String defaultPassword) throws MessagingException, UnsupportedEncodingException {
        final String emailTemplate = "Dear [[name]],<br>"
                + "Your share-hub password has been reset successfully:<br>"
                +"<h3> Below are new initial login credentials.</h3><br>"
                +"username: <b>[[username]]</b><br>"
                +"password: <b>[[password]]</b><br><br>"
                +"Please change your password on your next logon"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Click to continue to share-hub website</a></h3>"
                + "Thank you,<br>"
                + "Emmanuel from <span>ShareHub</span>";

        log.info("Sending email to "+user.getEmail());
        String subject= "ShareHub Login Credentials:";
        String content = emailTemplate;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName());
        content = content.replace("[[URL]]", this.website);
        content = content.replace("[[username]]",user.getUsername());
        content = content.replace("[[password]]", defaultPassword);

        helper.setText(content, true);
        log.info("Email to be sent: "+content);

        mailSender.send(message);


        log.info("Email send successfully: "+content);
    }
}
