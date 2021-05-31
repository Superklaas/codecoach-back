package com.switchfully.spectangular.services.mailing;

import com.switchfully.spectangular.domain.User;
import com.switchfully.spectangular.domain.session.Session;
import com.switchfully.spectangular.exceptions.UnableToSendEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    private static final String SENDER_MAIL = "spectangular.codecoach@gmail.com";
    private static final String REGARDS = "Kind regards,";
    private static final String SENDER_NAME = "The CodeCoach Team";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Autowired
    public EmailService(JavaMailSender mailSender, SpringTemplateEngine thymeleafTemplateEngine) {
        this.mailSender = mailSender;
        this.thymeleafTemplateEngine = thymeleafTemplateEngine;
    }

    @Async
    void sendEmail(MimeMessage email) {
        mailSender.send(email);
    }

    public void mailToResetPassword(User user, String url) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", user.getProfileName());
        templateModel.put("url", url + "/reset-password?token=" + user.getResetToken());
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("reset-password-template.html", thymeleafContext);

        sendHtmlMessage(user.getEmail(), "Password Reset Request", htmlBody);
    }

    public void mailForRegistering(User user) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("recipientName", user.getProfileName());
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("register-template.html", thymeleafContext);

        sendHtmlMessage(user.getEmail(), "Welcome to CodeCoach!", htmlBody);
    }

    public void mailCoacheeForSessionRequest(Session session) {
        Map<String, Object> templateModel = new HashMap<>();
        provideSessionDetails(templateModel, session);
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("coachee-session-request-template.html", thymeleafContext);

        sendHtmlMessage(session.getCoachee().getEmail(), "Your Session Request", htmlBody);
    }

    public void mailCoachForSessionRequest(Session session) {
        Map<String, Object> templateModel = new HashMap<>();
        provideSessionDetails(templateModel, session);
        sign(templateModel);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = thymeleafTemplateEngine.process("coach-session-request-template.html", thymeleafContext);

        sendHtmlMessage(session.getCoach().getEmail(), "New Session Request", htmlBody);
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody){
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(SENDER_MAIL);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            sendEmail(message);
        } catch(MessagingException ex) {
            throw new UnableToSendEmailException();
        }
    }

    private void sign(Map<String, Object> templateModel) {
        templateModel.put("regards", REGARDS);
        templateModel.put("senderName", SENDER_NAME);
    }

    private void provideSessionDetails(Map<String, Object> templateModel, Session session) {
        templateModel.put("coach", session.getCoach());
        templateModel.put("coachee", session.getCoachee());
        templateModel.put("subject", session.getSubject());
        templateModel.put("date", session.getDate());
        templateModel.put("time", session.getStartTime());
        templateModel.put("location", session.getLocation());
        templateModel.put("remarks", session.getRemarks());
    }

}
